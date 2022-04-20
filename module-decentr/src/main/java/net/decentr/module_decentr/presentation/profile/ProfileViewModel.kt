package net.decentr.module_decentr.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.decentr.module_decentr.domain.errors.ErrorHandler
import net.decentr.module_decentr.domain.models.Profile
import net.decentr.module_decentr.domain.state.Result
import net.decentr.module_decentr.domain.usecases.balance.GetBalanceDECUseCase
import net.decentr.module_decentr.domain.usecases.balance.GetBalancePDVUseCase
import net.decentr.module_decentr.domain.usecases.pdv.GetPDVCountUseCase
import net.decentr.module_decentr.domain.usecases.pdv.RemovePDVUseCase
import net.decentr.module_decentr.domain.usecases.signin.GetProfileUseCase
import net.decentr.module_decentr.domain.usecases.signin.RemoveUserUseCase
import net.decentr.module_decentr.domain.usecases.signin.SaveProfileUseCase
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val saveProfileUseCase: SaveProfileUseCase,
    private val removeUserUseCase: RemoveUserUseCase,
    private val removePDVUseCase: RemovePDVUseCase,
    private val getBalancePDVUseCase: GetBalancePDVUseCase,
    private val getBalanceDECUseCase: GetBalanceDECUseCase,
    private val getPDVCountUseCase: GetPDVCountUseCase,
    private val errorHandler: ErrorHandler
): ViewModel() {

    private val _balanceLiveData by lazy { MutableLiveData<Result<Triple<Double, Double, Double>>>() }
    val balanceLiveData: LiveData<Result<Triple<Double, Double, Double>>> = _balanceLiveData

    private val _profileLiveData by lazy { MutableLiveData<Profile>() }
    val profileLiveData: LiveData<Profile> = _profileLiveData

    private val PDV_DEVIDER = 1000000

    fun checkUpdateProfile(address: String) {
        viewModelScope.launch {
            try {
                val result = getProfileUseCase(address)
                if (result != null) {
                    saveProfileUseCase(result)
                    _profileLiveData.postValue(result)
                }
            } catch (e: Exception) {
                //ignore
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            removeUserUseCase.invoke()
//            removePDVUseCase.invoke()
        }
    }

    fun getBalances(address: String) {
        viewModelScope.launch {
            try {
                _balanceLiveData.postValue(Result.Loading)
                val pdvResponse = getBalancePDVUseCase(address)
                val decResponse = getBalanceDECUseCase(address)
                val pdvInProgress = getPDVCountUseCase(address).toDouble() / PDV_DEVIDER
                _balanceLiveData.postValue(Result.Success(Triple(decResponse.decAmount, pdvResponse.pdvAmount, pdvInProgress)))
            } catch (e: Exception) {
                _balanceLiveData.postValue(Result.Error(errorHandler.invoke(e)))
            }
        }
    }
}