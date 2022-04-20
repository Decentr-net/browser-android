package net.decentr.module_decentr.domain.usecases.register

import dagger.Binds
import dagger.Module

@Module
interface RegisterUseCasesModule {
    @Binds
    fun bindRegisterUseCase(useCase: RegisterUseCaseImpl): RegisterUseCase
    @Binds
    fun bindConfirmUseCase(useCase: ConfirmUseCaseImpl): ConfirmUseCase
    @Binds
    fun bindTrackInstallUseCase(useCase: TrackInstallUseCaseImpl): TrackInstallUseCase
}