package net.decentr.module_decentr.presentation.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import net.decentr.module_decentr.di.keys.ViewModelKey
import net.decentr.module_decentr.presentation.login.*
import net.decentr.module_decentr.presentation.login.barcode.QRLoginFragment
import net.decentr.module_decentr.presentation.login.barcode.QRLoginViewModel
import net.decentr.module_decentr.presentation.profile.ProfileFragment
import net.decentr.module_decentr.presentation.profile.ProfileViewModel
import net.decentr.module_decentr.presentation.profile.SetProfileFragment
import net.decentr.module_decentr.presentation.profile.SetProfileViewModel

@Module
interface LoginModule {
    @ContributesAndroidInjector
    fun bindLoginActivity(): LoginActivity

    @ContributesAndroidInjector
    fun contributeIntroLoginFragment(): IntroLoginFragment

    @ContributesAndroidInjector
    fun contributeQRLoginFragment(): QRLoginFragment

    @ContributesAndroidInjector
    fun contributeSignInPasswordFragment(): SignInPasswordFragment

    @ContributesAndroidInjector
    fun contributeSignInSeedPhraseFragment(): SignInSeedPhraseFragment

    @ContributesAndroidInjector
    fun contributeSignInEmailFragment(): SignInEmailFragment

    @ContributesAndroidInjector
    fun contributeProfileFragment(): ProfileFragment

    @ContributesAndroidInjector
    fun contributeSetProfileFragment(): SetProfileFragment

//    @Binds
//    @IntoMap
//    @FragmentKey(IntroLoginFragment::class)
//    fun bindIntroLoginFragment(fragment: IntroLoginFragment): Fragment

    @Binds
    @IntoMap
    @ViewModelKey(IntroLoginViewModel::class)
    fun bindIntroLoginViewModel(viewModel: IntroLoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(QRLoginViewModel::class)
    fun bindQRLoginViewModel(viewModel: QRLoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignInSeedViewModel::class)
    fun bindSignInSeedViewModel(viewModel: SignInSeedViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignInPasswordViewModel::class)
    fun bindSignInPasswordViewModel(viewModel: SignInPasswordViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignInEmailViewModel::class)
    fun bindSignInEmailViewModel(viewModel: SignInEmailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    fun bindProfileViewModel(viewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SetProfileViewModel::class)
    fun bindSetProfileViewModel(viewModel: SetProfileViewModel): ViewModel
}