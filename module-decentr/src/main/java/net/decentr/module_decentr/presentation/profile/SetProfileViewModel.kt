package net.decentr.module_decentr.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.decentr.module_decentr_domain.errors.ErrorHandler
import net.decentr.module_decentr_domain.models.PDVProfile
import net.decentr.module_decentr_domain.models.Profile
import net.decentr.module_decentr_domain.state.Result
import net.decentr.module_decentr_domain.usecases.pdv.SendPDVUseCase
import net.decentr.module_decentr_domain.usecases.signin.SaveProfileUseCase
import javax.inject.Inject

class SetProfileViewModel @Inject constructor(
    private val sendPDVUseCase: SendPDVUseCase,
    private val saveProfileUseCase: SaveProfileUseCase,
    private val errorHandler: ErrorHandler
): ViewModel() {

    private val _progressLiveData by lazy { MutableLiveData<Result<Profile>>() }
    val progressLiveData: LiveData<Result<Profile>> = _progressLiveData

    val timespampPattern = "yyyy-MM-dd"

    fun setProfile(profile: PDVProfile, address: String) {
        _progressLiveData.postValue(Result.Loading)
        viewModelScope.launch {
            try {
                val result = sendPDVUseCase.invoke(listOf(profile))
                if (result > 0) {
                    val profileLocal = Profile(
                        address = address,
                        avatar = profile.avatar,
                        firstName = profile.firstName,
                        lastName = profile.lastName ?: String(),
                        isBanned = false,
                        bio = profile.bio,
                        birthDate = profile.birthday,
                        emails = profile.emails,
                        createdAt = null,
                        gender = profile.gender
                    )
                    saveProfileUseCase.invoke(profileLocal)
                    _progressLiveData.postValue(Result.Success(profileLocal))
                }
            } catch (e: Exception) {
                _progressLiveData.postValue(Result.Error(errorHandler.invoke(e)))
            }
        }
    }
}