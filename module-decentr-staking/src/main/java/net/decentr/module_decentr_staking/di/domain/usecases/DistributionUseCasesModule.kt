package net.decentr.module_decentr_staking.di.domain.usecases

import dagger.Binds
import dagger.Module
import net.decentr.module_decentr_domain.usecases.distribution.GetTotalRewardsUseCase
import net.decentr.module_decentr_domain.usecases.distribution.GetTotalRewardsUseCaseImpl

@Module
interface DistributionUseCasesModule {
    @Binds
    fun bindGetTotalRewardsUseCase(useCase: GetTotalRewardsUseCaseImpl): GetTotalRewardsUseCase
}