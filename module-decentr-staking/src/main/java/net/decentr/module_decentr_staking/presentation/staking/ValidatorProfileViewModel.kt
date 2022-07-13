package net.decentr.module_decentr_staking.presentation.staking

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.decentr.module_decentr_domain.errors.DecException
import net.decentr.module_decentr_domain.errors.ErrorHandler
import net.decentr.module_decentr_domain.models.Profile
import net.decentr.module_decentr_domain.state.Result
import net.decentr.module_decentr_domain.usecases.signin.GetProfileUseCase
import net.decentr.module_decentr_domain.usecases.staking.CloseGrpcChannelUseCase
import net.decentr.module_decentr_domain.usecases.staking.GetDelegatorDelegatedUseCase
import net.decentr.module_decentr_domain.usecases.staking.GetPoolUseCase
import net.decentr.module_decentr_domain.usecases.staking.GetValidatorProfileUseCase
import net.decentr.module_decentr_staking.presentation.staking.mapper.toViewState
import net.decentr.module_decentr_staking.presentation.staking.viewstates.ValidatorViewState
import javax.inject.Inject

class ValidatorProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val getValidatorProfileUseCase: GetValidatorProfileUseCase,
    private val getPoolUseCase: GetPoolUseCase,
    private val getDelegatorDelegatedUseCase: GetDelegatorDelegatedUseCase,
    private val closeGrpcChannelUseCase: CloseGrpcChannelUseCase,
    private val errorHandler: ErrorHandler
) : ViewModel() {

    private val _validatorViewState by lazy { MutableLiveData<Result<ValidatorViewState>>() }
    val validatorViewStates: LiveData<Result<ValidatorViewState>>
        get() = _validatorViewState

    private var profile: Profile? = null

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

    fun close() {
        try {
            closeGrpcChannelUseCase.invoke()
        } catch (e: DecException) {
            Log.d("Close", e.message ?: "Close GRPC channel error")
        }
    }
}