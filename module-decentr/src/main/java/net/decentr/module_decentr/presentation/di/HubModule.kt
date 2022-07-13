package net.decentr.module_decentr.presentation.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import net.decentr.module_decentr_common.di.keys.keys.ViewModelKey
import net.decentr.module_decentr.presentation.hub.HubFragment
import net.decentr.module_decentr.presentation.hub.HubViewModel
import net.decentr.module_decentr.presentation.login.IntroLoginFragment
import net.decentr.module_decentr.presentation.login.IntroLoginViewModel

@Module
interface HubModule {

    @ContributesAndroidInjector
    fun contributeIntroLoginFragment(): IntroLoginFragment

    @ContributesAndroidInjector
    fun contributeHubFragment(): HubFragment

    @Binds
    @IntoMap
    @ViewModelKey(IntroLoginViewModel::class)
    fun bindIntroLoginViewModel(viewModel: IntroLoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HubViewModel::class)
    fun bindHubViewModel(viewModel: HubViewModel): ViewModel
}