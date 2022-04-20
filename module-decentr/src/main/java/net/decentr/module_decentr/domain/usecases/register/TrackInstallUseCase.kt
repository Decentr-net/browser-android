package net.decentr.module_decentr.domain.usecases.register

interface TrackInstallUseCase {
    suspend operator fun invoke(address: String)
}