package net.decentr.module_decentr.local

import android.content.Context
import dagger.Binds
import dagger.Lazy
import dagger.Module
import dagger.Provides
import net.decentr.module_decentr.data.provider.MnemonicProviderImpl
import net.decentr.module_decentr.data.provider.KeysProvider
import net.decentr.module_decentr.domain.provider.MnemonicProvider
import net.decentr.module_decentr.domain.provider.ServerManager
import net.decentr.module_decentr.local.database.ProjectDatabase
import net.decentr.module_decentr.local.database.dao.PDVDao
import net.decentr.module_decentr.local.database.dao.ProfileDao
import net.decentr.module_decentr.local.managers.ServerManagerImpl
import net.decentr.module_decentr.local.provider.KeysProviderImpl
import javax.inject.Singleton

@Module(includes = [LocalModule.ProvideModule::class])
interface LocalModule {

    @Binds
    @Singleton
    fun bindMnemonicProvider(provider: MnemonicProviderImpl): MnemonicProvider

    @Binds
    @Singleton
    fun bindServerManager(manager: ServerManagerImpl): ServerManager

    @Binds
    @Singleton
    fun bindTokenProvider(provider: KeysProviderImpl): KeysProvider

    @Module
    object ProvideModule {

        @Provides
        @Singleton
        fun providesDataBase(context: Context): ProjectDatabase {
            return ProjectDatabase.getInstance(context)
        }

        @Provides
        @Singleton
        fun providesProfileDao(projectDatabase: Lazy<ProjectDatabase>): ProfileDao {
            return projectDatabase.get().profileDao()
        }

        @Provides
        @Singleton
        fun providesPDVDao(projectDatabase: Lazy<ProjectDatabase>): PDVDao {
            return projectDatabase.get().pdvDao()
        }
    }
}