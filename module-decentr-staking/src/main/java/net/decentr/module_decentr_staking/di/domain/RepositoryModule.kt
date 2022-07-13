package net.decentr.module_decentr_staking.di.domain

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import net.decentr.module_decentr_common.data.provider.KeysProvider
import net.decentr.module_decentr_domain.repository.DistributionRepository
import net.decentr.module_decentr_domain.repository.StakingRepository
import net.decentr.module_decentr_domain.repository.TxRepository
import net.decentr.module_decentr_staking.data.datasource.DistributionDataSource
import net.decentr.module_decentr_staking.data.datasource.StakingDataSource
import net.decentr.module_decentr_staking.data.datasource.TxDataSource
import net.decentr.module_decentr_staking.data.repository.DistributionRepositoryImpl
import net.decentr.module_decentr_staking.data.repository.StakingRepositoryImpl
import net.decentr.module_decentr_staking.data.repository.TxRepositoryImpl
import net.decentr.module_decentr_staking.di.qualifier.RemoteSource

@Module(includes = [RepositoryModule.ProvideModule::class])
interface RepositoryModule {

    @Module
    object ProvideModule {

        @Provides
        fun provideStakingRepository(
            @RemoteSource stakingDataSource: StakingDataSource,
            ioDispatcher: CoroutineDispatcher
        ): StakingRepository {
            return StakingRepositoryImpl(stakingDataSource, ioDispatcher)
        }

        @Provides
        fun provideDistributionRepository(
            @RemoteSource distributionDataSource: DistributionDataSource,
            ioDispatcher: CoroutineDispatcher
        ): DistributionRepository {
            return DistributionRepositoryImpl(distributionDataSource, ioDispatcher)
        }

        @Provides
        fun provideTxRepository(
            @RemoteSource txDataSource: TxDataSource,
            keysProvider: KeysProvider,
            ioDispatcher: CoroutineDispatcher
        ): TxRepository {
            return TxRepositoryImpl(txDataSource, keysProvider, ioDispatcher)
        }
    }
}