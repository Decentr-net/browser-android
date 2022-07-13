package net.decentr.module_decentr_staking.di.domain.usecases

import dagger.Binds
import dagger.Module
import net.decentr.module_decentr_domain.usecases.tx.*

@Module
interface TxUseCasesModule {
    @Binds
    fun bindRewardsWithdrawUseCase(usesCase: RewardsWithdrawUseCaseImpl): RewardsWithdrawUseCase
    @Binds
    fun bindSimulateRewardsWithdrawUseCase(usesCase: SimulateRewardsWithdrawUseCaseImpl): SimulateRewardsWithdrawUseCase

    @Binds
    fun bindDelegateUseCase(usesCase: DelegateUseCaseImpl): DelegateUseCase
    @Binds
    fun bindSimulateDelegateUseCase(usesCase: SimulateDelegateUseCaseImpl): SimulateDelegateUseCase

    @Binds
    fun bindRedelegateUseCase(usesCase: RedelegateUseCaseImpl): RedelegateUseCase
    @Binds
    fun bindSimulateRedelegateUseCase(usesCase: SimulateRedelegateUseCaseImpl): SimulateRedelegateUseCase

    @Binds
    fun bindUndelegateUseCase(usesCase: UndelegateUseCaseImpl): UndelegateUseCase
    @Binds
    fun bindSimulateUndelegateUseCase(usesCase: SimulateUndelegateUseCaseImpl): SimulateUndelegateUseCase

}