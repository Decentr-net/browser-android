package net.decentr.module_decentr.di.domain.usecases

import dagger.Binds
import dagger.Module
import net.decentr.module_decentr_domain.usecases.balance.GetBalanceDECUseCase
import net.decentr.module_decentr_domain.usecases.balance.GetBalanceDecUseCaseImpl
import net.decentr.module_decentr_domain.usecases.balance.GetBalancePDVUseCase
import net.decentr.module_decentr_domain.usecases.balance.GetBalancePDVUseCaseImpl

@Module
interface BalanceUseCasesModule {
    @Binds
    fun bindGetBalanceDECUseCase(useCase: GetBalanceDecUseCaseImpl): GetBalanceDECUseCase
    @Binds
    fun bindGetBalancePDVUseCase(useCase: GetBalancePDVUseCaseImpl): GetBalancePDVUseCase
}