package net.decentr.module_decentr.di.domain

import dagger.Binds
import dagger.Module
import net.decentr.module_decentr_domain.errors.ErrorHandler
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