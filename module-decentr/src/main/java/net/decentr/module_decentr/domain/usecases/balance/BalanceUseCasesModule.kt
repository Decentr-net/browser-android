package net.decentr.module_decentr.domain.usecases.balance

import dagger.Binds
import dagger.Module

@Module
interface BalanceUseCasesModule {
    @Binds
    fun bindGetBalanceDECUseCase(useCase: GetBalanceDecUseCaseImpl): GetBalanceDECUseCase
    @Binds
    fun bindGetBalancePDVUseCase(useCase: GetBalancePDVUseCaseImpl): GetBalancePDVUseCase
}