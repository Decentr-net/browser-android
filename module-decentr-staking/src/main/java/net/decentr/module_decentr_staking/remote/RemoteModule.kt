package net.decentr.module_decentr_staking.remote

import dagger.Module
import dagger.Provides
import net.decentr.module_decentr_staking.remote.services.*
import javax.inject.Singleton

@Module
object RemoteModule {

    @Provides
    @Singleton
    fun provideStakingService(): StakingService {
        return StakingService()
    }

    @Provides
    @Singleton
    fun provideStakingActionsService(): StakingActionsService {
        return StakingActionsService()
    }

    @Provides
    @Singleton
    fun provideDistributionService(): DistributionService {
        return DistributionService()
    }

    @Provides
    @Singleton
    fun provideDistributionActionsService(): DistributionActionsService {
        return DistributionActionsService()
    }

    @Provides
    @Singleton
    fun provideTxService(): TxService {
        return TxService()
    }

}