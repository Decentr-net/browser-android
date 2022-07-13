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
import net.decentr.module_decentr_domain.usecases.signin.GetProfileUseCase
import net.decentr.module_decentr_domain.usecases.staking.CloseGrpcChannelUseCase
import net.decentr.module_decentr_domain.usecases.staking.GetDelegatorDelegatedUseCase
import net.decentr.module_decentr_domain.usecases.staking.GetPoolUseCase
import net.decentr.module_decentr_domain.usecases.staking.GetValidatorsUseCase
import net.decentr.module_decentr_staking.presentation.staking.mapper.toViewState
import net.decentr.module_decentr_staking.presentation.staking.viewstates.ValidatorViewState
import javax.inject.Inject

class ValidatorsViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val getValidatorsUseCase: GetValidatorsUseCase,
    private val getPoolUseCase: GetPoolUseCase,
    private val getDelegatorDelegatedUseCase: GetDelegatorDelegatedUseCase,
    private val closeGrpcChannelUseCase: CloseGrpcChannelUseCase,
    private val errorHandler: ErrorHandler
) : ViewModel() {

    private val _viewStatesList by lazy { MutableLiveData<Result<List<ValidatorViewState>>>() }
    val viewStatesList: LiveData<Result<List<ValidatorViewState>>>
        get() = _viewStatesList

    var address = String()

    fun getValidators(status: Validator.Status = Validator.Status.UNSPECIFIED) {
        viewModelScope.launch {
            try {
                val profile = getProfileUseCase(null)
                if (profile != null && profile.address.isNotEmpty()) {
                    address = profile.address
                    _viewStatesList.postValue(Result.Loading)
                    val (bondedTokens, notBondedTokens) = getPoolUseCase.invoke()
                    val delegatorDelegations = getDelegatorDelegatedUseCase(address)
                    _viewStatesList.postValue(Result.Success(getValidatorsUseCase(status).map {
                        it.toViewState(bondedTokens.toLongOrNull() ?: 0L, delegatorDelegations)
                    }))
                } else {
                    _viewStatesList.postValue(Result.Error(DecException("No address for request.")))
                }
            } catch (e: Exception) {
                _viewStatesList.postValue(Result.Error(errorHandler.invoke(e)))
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