package net.decentr.module_decentr_staking.presentation.staking

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.decentr.module_decentr_domain.errors.DecException
import net.decentr.module_decentr_domain.errors.ErrorHandler
import net.decentr.module_decentr_domain.models.staking.Validator
import net.decentr.module_decentr_domain.state.Result
import net.decentr.module_decentr_domain.usecases.distribution.GetTotalRewardsUseCase
import net.decentr.module_decentr_domain.usecases.signin.GetProfileUseCase
import net.decentr.module_decentr_domain.usecases.staking.CloseGrpcChannelUseCase
import net.decentr.module_decentr_domain.usecases.staking.GetDelegatorDelegatedUseCase
import net.decentr.module_decentr_domain.usecases.staking.GetValidatorsUseCase
import net.decentr.module_decentr_domain.usecases.tx.RewardsWithdrawUseCase
import net.decentr.module_decentr_domain.usecases.tx.SimulateRewardsWithdrawUseCase
import net.decentr.module_decentr_staking.presentation.staking.mapper.toViewState
import net.decentr.module_decentr_staking.presentation.staking.viewstates.RewardViewState
import net.decentr.module_decentr_staking.presentation.staking.viewstates.RewardsViewState
import javax.inject.Inject

class RewardsViewModel @Inject constructor(
    private val getTotalRewardsUseCase: GetTotalRewardsUseCase,
    private val simulateRewardsWithdrawUseCase: SimulateRewardsWithdrawUseCase,
    private val rewardsWithdrawUseCase: RewardsWithdrawUseCase,
    private val getValidatorsUseCase: GetValidatorsUseCase,
    private val getDelegatorDelegatedUseCase: GetDelegatorDelegatedUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val closeGrpcChannelUseCase: CloseGrpcChannelUseCase,
    private val errorHandler: ErrorHandler
) : ViewModel() {

    private val _viewStatesList by lazy { MutableLiveData<Result<List<RewardsViewState>>>() }
    val viewStatesList: LiveData<Result<List<RewardsViewState>>>
        get() = _viewStatesList

    private val _delegatorDelegations by lazy { MutableLiveData<List<Pair<String, String>>>() }
    val delegatorDelegations: LiveData<List<Pair<String, String>>>
        get() = _delegatorDelegations

    private val _fee by lazy { MutableLiveData<String>() }
    val fee: LiveData<String>
        get() = _fee

    private val _withdrawResult by lazy { MutableLiveData<Result<String>>() }
    val withdrawResult: LiveData<Result<String>>
        get() = _withdrawResult

    var address = String()

    init {
        viewModelScope.launch {
            val profile = getProfileUseCase(null)
            if (profile != null && profile.address.isNotEmpty()) {
                address = profile.address
                getRewards(address)
            } else {
                _viewStatesList.postValue(Result.Error(DecException("No address for request.")))
            }
        }
    }

    fun getRewards(address: String) {
        viewModelScope.launch {
            try {
                _viewStatesList.postValue(Result.Loading)
                val totalRewards = getTotalRewardsUseCase(address)
                if (totalRewards.isNotEmpty()) {
                    val validators = getValidatorsUseCase(Validator.Status.UNSPECIFIED)
                    val delegations = getDelegatorDelegatedUseCase(address)
                    val viewStates = totalRewards.map { reward ->
                        val validator = validators.firstOrNull { reward.validatorAddress == it.address }
                        val delegated = delegations.firstOrNull { reward.validatorAddress == it.first }
                        reward.toViewState(
                            validator?.name ?: String(),
                            delegated?.second ?: String()
                        )
                    }
                    _viewStatesList.postValue(Result.Success(viewStates))
                } else {
                    _viewStatesList.postValue(Result.Success(emptyList()))
                }
            } catch (e: Exception) {
                _viewStatesList.postValue(Result.Error(errorHandler.invoke(e)))
            }
        }
    }

    fun getDelegations(address: String) {
        viewModelScope.launch {
            try {
                _delegatorDelegations.postValue(getDelegatorDelegatedUseCase(address))
            } catch (e: DecException) {
                _delegatorDelegations.postValue(emptyList())
            }
        }
    }

    fun calculateFee(validatorAddresses: ArrayList<String?>) {
        viewModelScope.launch {
            try {
                val (gasUsed, gasWanted) = simulateRewardsWithdrawUseCase.invoke(validatorAddresses, address)
                _fee.postValue(gasUsed.toString())
            } catch (e: DecException) {
                Log.d("Fee error", errorHandler.invoke(e).message ?: "Unhandled Error")
            }
        }
    }

    fun withdraw(validatorAddresses: ArrayList<String?>, fee: String) {
        viewModelScope.launch {
            try {
                _withdrawResult.postValue(Result.Loading)
                _withdrawResult.postValue(Result.Success(rewardsWithdrawUseCase.invoke(validatorAddresses, address, fee)))

                if (viewStatesList.value is Result.Success) {
                    val viewStatesList = ((viewStatesList.value as Result.Success).data).toMutableList()
                    validatorAddresses.forEach { address ->
                        (viewStatesList.find { (it as? RewardViewState)?.validatorAddress == address } as? RewardViewState)?.rewardAmount = String()
                    }
//                    val updatedList = previousList.map { reward ->
//                        if (reward !is RewardViewState) return@map
//                        if (validatorAddresses.contains(reward.validatorAddress)) reward.copy(rewardAmount = String())
//                        else reward
//                    }
                    _viewStatesList.postValue(Result.Success(viewStatesList))
                }
            } catch (e: DecException) {
                _withdrawResult.postValue(Result.Error(errorHandler.invoke(e)))
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
}