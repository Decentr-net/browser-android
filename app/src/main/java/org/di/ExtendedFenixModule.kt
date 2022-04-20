package org.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import net.decentr.module_decentr.di.keys.ViewModelKey
import org.mozilla.fenix.HomeActivity
import org.mozilla.fenix.HomeDecentrViewModel
import org.mozilla.fenix.customtabs.ExternalAppBrowserActivity

@Module
interface ExtendedFenixModule {

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    fun bindHomeActivity(): HomeActivity

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    fun bindExternalAppBrowserActivity(): ExternalAppBrowserActivity

    @Module
    interface ViewModelModule {
        @Binds
        @IntoMap
        @ViewModelKey(HomeDecentrViewModel::class)
        fun bindHomeDecentrViewModel(viewModel: HomeDecentrViewModel): ViewModel
    }
}