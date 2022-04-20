package net.decentr.module_decentr.domain

import dagger.Binds
import dagger.Module
import net.decentr.module_decentr.domain.errors.ErrorHandler
import net.decentr.module_decentr.domain.repository.RepositoryModule
import net.decentr.module_decentr.domain.usecases.UsecasesModule
import net.decentr.module_decentr.remote.errors.ErrorHandlerImpl
import javax.inject.Singleton

@Module(includes = [
    RepositoryModule::class,
    UsecasesModule::class
])
interface DomainModule {

    @Binds
    @Singleton
    fun bindErrorHandler(handler: ErrorHandlerImpl): ErrorHandler
}