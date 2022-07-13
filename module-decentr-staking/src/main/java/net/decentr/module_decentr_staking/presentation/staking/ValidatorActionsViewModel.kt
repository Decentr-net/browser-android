package net.decentr.module_decentr_staking.presentation.staking

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.decentr.module_decentr_domain.errors.DecException
import net.decentr.module_decentr_domain.errors.ErrorHandler
import net.decentr.module_decentr_domain.models.BalanceDEC
import net.decentr.module_decentr_domain.models.Profile
import net.decentr.module_decentr_domain.models.staking.Validator
import net.decentr.module_decentr_domain.state.Result
import net.decentr.module_decentr_domain.usecases.balance.GetBalancePDVUseCase
import net.decentr.module_decentr_domain.usecases.signin.GetProfileUseCase
import net.decentr.module_decentr_domain.usecases.staking.*
import net.decentr.module_decentr_domain.usecases.tx.*
import net.decentr.module_decentr_staking.presentation.staking.mapper.toViewState
import net.decentr.module_decentr_staking.presentation.staking.viewstates.ResultViewState
import net.decentr.module_decentr_staking.presentation.staking.viewstates.ValidatorViewState
import javax.inject.Inject

class ValidatorActionsViewModel @Inject constructor(
    private val getValidatorProfileUseCase: GetValidatorProfileUseCase,
    private val getValidatorsUseCase: GetValidatorsUseCase,
    private val getBalanceDECUseCase: GetBalancePDVUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val getPoolUseCase: GetPoolUseCase,
    private val getDelegatorDelegatedUseCase: GetDelegatorDelegatedUseCase,
    private val closeGrpcChannelUseCase: CloseGrpcChannelUseCase,
    private val simulateDelegateUseCase: SimulateDelegateUseCase,
    private val delegateUseCase: DelegateUseCase,
    private val simulateRedelegateUseCase: SimulateRedelegateUseCase,
    private val redelegateUseCase: RedelegateUseCase,
    private val simulateUndelegateUseCase: SimulateUndelegateUseCase,
    private val undelegateUseCase: UndelegateUseCase,
    private val errorHandler: ErrorHandler): ViewModel() {

    private val _validatorViewState by lazy { MutableLiveData<Result<ValidatorViewState>>() }
    val validatorViewStates: LiveData<Result<ValidatorViewState>>
        get() = _validatorViewState

    private val _validatorsList by lazy { MutableLiveData<Result<List<ValidatorViewState>>>() }
    val validatorsList: LiveData<Result<List<ValidatorViewState>>>
        get() = _validatorsList

    private val _actionResult by lazy { MutableLiveData<Result<ResultViewState>>() }
    val actionResult: LiveData<Result<ResultViewState>>
        get() = _actionResult

    private val _balance by lazy { MutableLiveData<BalanceDEC>() }
    val balance: LiveData<BalanceDEC>
        get() = _balance

    private val _fee by lazy { MutableLiveData<Double>() }
    val fee: LiveData<Double>
        get() = _fee

    private var profile: Profile? = null
    private var toValidator: ValidatorViewState? = null
    private var maxAvailable: Double? = null

    init {
        getBalance()
    }

    fun loadValidator(address: String) {
        viewModelScope.launch {
            _validatorViewState.postValue(Result.Loading)
            try {
                profile = getProfileUseCase(null)
                val (bondedTokens, notBoundedTokens) = getPoolUseCase.invoke()
                var delegatorDelegations = emptyList<Pair<String, String>>()
                if (profile != null) delegatorDelegations = getDelegatorDelegatedUseCase(profile!!.address)
                _validatorViewState.postValue(
                    Result.Success(getValidatorProfileUseCase(address).toViewState(
                        bondedTokens.toLongOrNull() ?: 0L, delegatorDelegations)))
            } catch (e: DecException) {
                _validatorViewState.postValue(Result.Error(errorHandler.invoke(e)))
            }
        }
    }


    fun getValidators(status: Validator.Status = Validator.Status.UNSPECIFIED) {
        viewModelScope.launch {
            try {
                _validatorsList.postValue(Result.Loading)
                _validatorsList.postValue(Result.Success(getValidatorsUseCase(status).filter { it.status == Validator.Status.BONDED }.map { it.toViewState() }))
            } catch (e: Exception) {
                _validatorsList.postValue(Result.Error(errorHandler.invoke(e)))
            }
        }
    }

    fun setMaxAvailable(value: Double) {
        maxAvailable = value
    }

    fun getMaxAvailable(): Double = maxAvailable ?: 0.0

    fun setToValidator(validatorViewState: ValidatorViewState) {
        toValidator = validatorViewState
    }

    fun getToValidator(): ValidatorViewState? {
        return toValidator
    }

    fun action(type: ActionType, toValidatorAddress: String?, fromValidatorAddress: String?, amount: Double, fee: Double) {
        viewModelScope.launch {
            try {
                _actionResult.postValue(Result.Loading)
                if (profile != null) {
                    when (type) {
                        ActionType.DELEGATE -> {
                            delegateUseCase(
                                profile!!.address,
                                toValidatorAddress ?: throw IllegalStateException("to validator address should not be null"),
                                amount.toString(),
                                fee.toString()
                            )
                        }
                        ActionType.REDELEGATE -> {
                            redelegateUseCase(
                                profile!!.address,
                                fromValidatorAddress
                                    ?: throw IllegalStateException("from validator address should not be null"),
                                toValidatorAddress
                                    ?: throw IllegalStateException("to validator address should not be null"),
                                amount.toString(),
                                fee.toString()
                            )
                        }
                        ActionType.UNDELEGATE -> {
                            undelegateUseCase(
                                profile!!.address,
                                fromValidatorAddress
                                    ?: throw IllegalStateException("from validator address should not be null"),
                                amount.toString(),
                                fee.toString()
                            )
                        }
                    }
                }
                val resultViewState = ResultViewState(
                    type = type,
                    toValidator = toValidatorAddress,
                    fromValidator = fromValidatorAddress,
                    decAmount = amount.toString()
                )
                _actionResult.postValue(Result.Success(resultViewState))
            } catch (e: DecException) {
                _actionResult.postValue(Result.Error(errorHandler(e)))
            }
        }
    }

    private fun getBalance() {
        viewModelScope.launch {
            profile = getProfileUseCase(null)
            if (profile != null) {
                val balance = BalanceDEC(getBalanceDECUseCase(profile!!.address).pdvAmount)
                _balance.postValue(balance)
            }
        }
    }

    fun calculateFee(type: ActionType, toValidatorAddress: String?, fromValidatorAddress: String?, amount: Double) {
        viewModelScope.launch {
            try {
                if (profile != null) {
                    val (gasUsed, gasWanted) = when (type) {
                        ActionType.DELEGATE ->
                            simulateDelegateUseCase(
                                profile!!.address,
                                toValidatorAddress ?: throw IllegalStateException("to validator address should not be null"),
                                amount.toString()
                            )
                        ActionType.REDELEGATE ->
                            simulateRedelegateUseCase(
                                profile!!.address,
                                fromValidatorAddress ?: throw IllegalStateException("from validator address should not be null"),
                                toValidatorAddress ?: throw IllegalStateException("to validator address should not be null"),
                                amount.toString()
                            )
                        ActionType.UNDELEGATE ->
                            simulateUndelegateUseCase(
                                profile!!.address,
                                fromValidatorAddress ?: throw IllegalStateException("to validator address should not be null"),
                                amount.toString()
                            )
                    }
                    _fee.postValue(gasUsed)
                }
            } catch (e: DecException) {
                _actionResult.postValue(Result.Error(errorHandler.invoke(e)))
            }
        }
    }

    fun close() {
        try {
            closeGrpcChannelUseCase.invoke()
        } catch (e: DecException) {
            Log.d("Close", e.message ?: "Close GRPC channel error")
        }
    }

    enum class ActionType {
        DELEGATE,
        REDELEGATE,
        UNDELEGATE
    }
}