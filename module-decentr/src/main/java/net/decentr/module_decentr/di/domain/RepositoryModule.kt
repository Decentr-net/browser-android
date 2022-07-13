package net.decentr.module_decentr.di.domain

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import net.decentr.module_decentr.data.datasource.cerberus.BalancesDataSource
import net.decentr.module_decentr.data.datasource.cerberus.PDVDataSource
import net.decentr.module_decentr.data.datasource.cerberus.ProfileDataSource
import net.decentr.module_decentr.data.datasource.cerberus.RegisterDataSource
import net.decentr.module_decentr.data.repository.BalanceRepositoryImpl
import net.decentr.module_decentr.data.repository.PDVRepositoryImpl
import net.decentr.module_decentr.data.repository.RegisterRepositoryImpl
import net.decentr.module_decentr.data.repository.SignInRepositoryImpl
import net.decentr.module_decentr.di.qualifier.LocalSource
import net.decentr.module_decentr.di.qualifier.RemoteSource
import net.decentr.module_decentr_common.data.provider.KeysProvider
import net.decentr.module_decentr_domain.provider.MnemonicProvider
import net.decentr.module_decentr_domain.repository.BalanceRepository
import net.decentr.module_decentr_domain.repository.PDVRepository
import net.decentr.module_decentr_domain.repository.RegisterRepository
import net.decentr.module_decentr_domain.repository.SignInRepository

@Module(includes = [RepositoryModule.ProvideModule::class])
interface RepositoryModule {

    @Module
    object ProvideModule {
        @Provides
        fun providesIODispatcher(): CoroutineDispatcher {
            return Dispatchers.IO
        }

        @Provides
        fun providesAuthorizationRepository(
            mnemonicProvider: MnemonicProvider,
            keysProvider: KeysProvider,
            @LocalSource localProfileDataSource: ProfileDataSource,
            @RemoteSource remoteProfileDataSource: ProfileDataSource,
            ioDispatcher: CoroutineDispatcher
        ): SignInRepository {

            return SignInRepositoryImpl(mnemonicProvider, keysProvider, localProfileDataSource, remoteProfileDataSource, ioDispatcher)
        }

        @Provides
        fun providesPDVRepository(
            @LocalSource localProfileDataSource: PDVDataSource,
            @RemoteSource remoteProfileDataSource: PDVDataSource,
            ioDispatcher: CoroutineDispatcher
        ): PDVRepository {

            return PDVRepositoryImpl(localProfileDataSource, remoteProfileDataSource, ioDispatcher)
        }

        @Provides
        fun providesBalancesRepository(
            @RemoteSource remoteBalancesDataSource: BalancesDataSource,
            ioDispatcher: CoroutineDispatcher
        ): BalanceRepository {

            return BalanceRepositoryImpl(remoteBalancesDataSource, ioDispatcher)
        }

        @Provides
        fun providesRegisterRepository(
            @RemoteSource remoteRegisterDataSource: RegisterDataSource,
            ioDispatcher: CoroutineDispatcher
        ): RegisterRepository {

            return RegisterRepositoryImpl(remoteRegisterDataSource, ioDispatcher)
        }
    }
}