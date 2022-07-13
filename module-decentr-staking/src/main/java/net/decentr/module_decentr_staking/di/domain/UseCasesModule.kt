package net.decentr.module_decentr_staking.di.domain

import dagger.Module
import net.decentr.module_decentr_staking.di.domain.usecases.DistributionUseCasesModule
import net.decentr.module_decentr_staking.di.domain.usecases.StakingUseCasesModule
import net.decentr.module_decentr_staking.di.domain.usecases.TxUseCasesModule

@Module(includes = [
    StakingUseCasesModule::class,
    DistributionUseCasesModule::class,
    TxUseCasesModule::class
])
interface UseCasesModule