package net.decentr.module_decentr.presentation.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import net.decentr.module_decentr_common.di.keys.keys.ViewModelKey
import net.decentr.module_decentr.presentation.profile.ProfileFragment
import net.decentr.module_decentr.presentation.profile.ProfileViewModel
import net.decentr.module_decentr.presentation.profile.SetProfileFragment
import net.decentr.module_decentr.presentation.profile.SetProfileViewModel

@Module
interface ProfileModule {

    @ContributesAndroidInjector
    fun contributeProfileFragment(): ProfileFragment

    @ContributesAndroidInjector
    fun contributeSetProfileFragment(): SetProfileFragment

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    fun bindProfileViewModel(viewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SetProfileViewModel::class)
    fun bindSetProfileViewModel(viewModel: SetProfileViewModel): ViewModel
}