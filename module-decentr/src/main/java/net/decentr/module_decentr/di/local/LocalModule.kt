package net.decentr.module_decentr.di.local

import android.content.Context
import dagger.Binds
import dagger.Lazy
import dagger.Module
import dagger.Provides
import net.decentr.module_decentr.data.provider.MnemonicProviderImpl
import net.decentr.module_decentr.local.provider.KeysProviderImpl
import net.decentr.module_decentr_common.data.provider.KeysProvider
import net.decentr.module_decentr_db.ProjectDatabase
import net.decentr.module_decentr_db.dao.PDVDao
import net.decentr.module_decentr_db.dao.ProfileDao
import net.decentr.module_decentr_domain.provider.MnemonicProvider
import javax.inject.Singleton

@Module(includes = [LocalModule.ProvideModule::class])
interface LocalModule {

    @Binds
    @Singleton
    fun bindMnemonicProvider(provider: MnemonicProviderImpl): MnemonicProvider

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