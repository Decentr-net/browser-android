package net.decentr.module_decentr.domain.usecases.server

import dagger.Binds
import dagger.Module

@Module
interface ServerUseCasesModule {

    @Binds
    fun bindSetServerUseCase(useCase: SetCurrentServerUseCaseImpl): SetCurrentServerUseCase
}