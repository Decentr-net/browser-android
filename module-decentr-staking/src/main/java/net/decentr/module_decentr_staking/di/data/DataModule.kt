package net.decentr.module_decentr_staking.di.data

import dagger.Binds
import dagger.Module
import net.decentr.module_decentr_staking.data.datasource.DistributionDataSource
import net.decentr.module_decentr_staking.data.datasource.StakingDataSource
import net.decentr.module_decentr_staking.data.datasource.TxDataSource
import net.decentr.module_decentr_staking.di.qualifier.RemoteSource
import net.decentr.module_decentr_staking.remote.datasource.DistributionDataSourceImpl
import net.decentr.module_decentr_staking.remote.datasource.StakingDataSourceImpl
import net.decentr.module_decentr_staking.remote.datasource.TxDataSourceImpl

@Module
interface DataModule {

    @Binds
    @RemoteSource
    fun bindRemoteStakingDataSource(dataSource: StakingDataSourceImpl): StakingDataSource

    @Binds
    @RemoteSource
    fun bindRemoteDistributionDataSource(dataSource: DistributionDataSourceImpl): DistributionDataSource

    @Binds
    @RemoteSource
    fun bindRemoteTxDataSource(dataSource: TxDataSourceImpl): TxDataSource

}