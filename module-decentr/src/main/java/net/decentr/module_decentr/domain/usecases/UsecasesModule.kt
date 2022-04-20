package net.decentr.module_decentr.domain.usecases

import dagger.Module
import net.decentr.module_decentr.domain.usecases.balance.BalanceUseCasesModule
import net.decentr.module_decentr.domain.usecases.pdv.PDVUseCasesModule
import net.decentr.module_decentr.domain.usecases.register.RegisterUseCasesModule
import net.decentr.module_decentr.domain.usecases.server.ServerUseCasesModule
import net.decentr.module_decentr.domain.usecases.signin.SignInUseCasesModule

@Module(includes = [
    ServerUseCasesModule::class,
    SignInUseCasesModule::class,
    PDVUseCasesModule::class,
    BalanceUseCasesModule::class,
    RegisterUseCasesModule::class
])
interface UsecasesModule