package net.decentr.module_decentr.domain.usecases.register

import net.decentr.module_decentr.domain.repository.RegisterRepository
import javax.inject.Inject

class TrackInstallUseCaseImpl @Inject constructor(private val repository: RegisterRepository): TrackInstallUseCase {
    override suspend fun invoke(address: String) {
        repository.trackInstall(address)
    }
}