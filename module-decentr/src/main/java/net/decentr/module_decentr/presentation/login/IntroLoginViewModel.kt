package net.decentr.module_decentr.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.decentr.module_decentr_domain.models.Profile
import net.decentr.module_decentr_domain.usecases.signin.GetProfileUseCase
import javax.inject.Inject

class IntroLoginViewModel @Inject constructor(private val getProfileUseCase: GetProfileUseCase): ViewModel() {

    private val _progressLiveData by lazy { MutableLiveData<Profile?>() }
    val progressLiveData: LiveData<Profile?> = _progressLiveData

    fun checkIsUserContains() {
        viewModelScope.launch {
            val profile = getProfileUseCase(null)
            _progressLiveData.postValue(profile)
        }
    }
}