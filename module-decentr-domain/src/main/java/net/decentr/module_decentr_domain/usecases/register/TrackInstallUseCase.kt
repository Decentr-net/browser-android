package net.decentr.module_decentr_domain.usecases.register

interface TrackInstallUseCase {
    suspend operator fun invoke(address: String)
}