package net.decentr.module_decentr.di.domain

import dagger.Module
import net.decentr.module_decentr.di.domain.usecases.BalanceUseCasesModule
import net.decentr.module_decentr.di.domain.usecases.PDVUseCasesModule
import net.decentr.module_decentr.di.domain.usecases.RegisterUseCasesModule
import net.decentr.module_decentr.di.domain.usecases.SignInUseCasesModule

@Module(includes = [
    SignInUseCasesModule::class,
    PDVUseCasesModule::class,
    BalanceUseCasesModule::class,
    RegisterUseCasesModule::class
])
interface UsecasesModule