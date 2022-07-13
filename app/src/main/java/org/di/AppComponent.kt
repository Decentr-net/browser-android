package org.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import net.decentr.module_decentr.di.ModelModule
import net.decentr.module_decentr_staking.di.StakingModelModule
import org.mozilla.fenix.FenixApplication
import org.mozilla.fenix.HomeActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    ModelModule::class,
    StakingModelModule::class,
    ExtendedFenixModule::class
//    LoggerModule::class
])
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }

    fun injectApp(application: FenixApplication)
    fun injectActivity(application: HomeActivity)
}
