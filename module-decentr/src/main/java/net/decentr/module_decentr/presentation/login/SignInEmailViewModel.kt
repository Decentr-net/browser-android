package net.decentr.module_decentr.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.decentr.module_decentr.domain.errors.ErrorHandler
import javax.inject.Inject
import net.decentr.module_decentr.domain.state.Result
import net.decentr.module_decentr.domain.usecases.register.ConfirmUseCase
import net.decentr.module_decentr.domain.usecases.register.RegisterUseCase

class SignInEmailViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val confirmUseCase: ConfirmUseCase,
    private val errorHandler: ErrorHandler
    ): ViewModel() {

    private val _progressLiveData by lazy { MutableLiveData<Result<Unit>>() }
    val progressLiveData: LiveData<Result<Unit>> = _progressLiveData

    fun sendConfirmEmail(email: String, address: String) {
        viewModelScope.launch {
            _progressLiveData.postValue(Result.Loading)
            try {
                registerUseCase.invoke(address, email)
                _progressLiveData.postValue(Result.Success(Unit))
            } catch (e: Exception) {
                _progressLiveData.postValue(Result.Error(errorHandler.invoke(e)))
            }
        }
    }

    fun sendConfirmCode(email: String, code: String) {
        viewModelScope.launch {
            _progressLiveData.postValue(Result.Loading)
            try {
                confirmUseCase.invoke(email, code)
                _progressLiveData.postValue(Result.Success(Unit))
            } catch (e: Exception) {
                _progressLiveData.postValue(Result.Error(errorHandler.invoke(e)))
            }
        }
    }

}