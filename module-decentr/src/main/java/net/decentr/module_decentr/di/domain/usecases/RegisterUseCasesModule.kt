package net.decentr.module_decentr.di.domain.usecases

import dagger.Binds
import dagger.Module
import net.decentr.module_decentr_domain.usecases.register.*

@Module
interface RegisterUseCasesModule {
    @Binds
    fun bindRegisterUseCase(useCase: RegisterUseCaseImpl): RegisterUseCase
    @Binds
    fun bindConfirmUseCase(useCase: ConfirmUseCaseImpl): ConfirmUseCase
    @Binds
    fun bindTrackInstallUseCase(useCase: TrackInstallUseCaseImpl): TrackInstallUseCase
}