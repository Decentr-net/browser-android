package net.decentr.module_decentr.data

import dagger.Binds
import dagger.Module
import net.decentr.module_decentr.data.datasource.cerberus.BalancesDataSource
import net.decentr.module_decentr.data.datasource.cerberus.PDVDataSource
import net.decentr.module_decentr.data.datasource.cerberus.ProfileDataSource
import net.decentr.module_decentr.data.datasource.cerberus.RegisterDataSource
import net.decentr.module_decentr.di.qualifier.LocalSource
import net.decentr.module_decentr.di.qualifier.RemoteSource
import net.decentr.module_decentr.local.datasource.cerberus.LocalPDVDataSourceImpl
import net.decentr.module_decentr.local.datasource.cerberus.LocalProfileDataSourceImpl
import net.decentr.module_decentr.remote.datasource.cerberus.RemotePDVDataSourceImpl
import net.decentr.module_decentr.remote.datasource.cerberus.RemoteProfileDataSourceImpl
import net.decentr.module_decentr.remote.datasource.decentr.RemoteBalancesDataSourceImpl
import net.decentr.module_decentr.remote.datasource.vulcan.RemoteRegisterDataSourceImpl

@Module(includes = [DataModule.ProvidesModule::class])
interface DataModule {

    @Binds
    @RemoteSource
    fun bindRemoteStocksIpoProgressDataSource(dataSource: RemoteProfileDataSourceImpl): ProfileDataSource

    @Binds
    @LocalSource
    fun bindsLocalProfileDataSource(dataSource: LocalProfileDataSourceImpl): ProfileDataSource

    @Binds
    @LocalSource
    fun bindsLocalPDVDataSource(dataSource: LocalPDVDataSourceImpl): PDVDataSource

    @Binds
    @RemoteSource
    fun bindsRemotePDVDataSource(dataSource: RemotePDVDataSourceImpl): PDVDataSource

    @Binds
    @RemoteSource
    fun bindsRemoteBalancesDataSource(dataSource: RemoteBalancesDataSourceImpl): BalancesDataSource

    @Binds
    @RemoteSource
    fun bindsRemoteRegisterDataSource(dataSource: RemoteRegisterDataSourceImpl): RegisterDataSource

    @Module
    object ProvidesModule {
    }
}