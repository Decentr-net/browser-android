package net.decentr.module_decentr.presentation.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import net.decentr.module_decentr_common.di.keys.keys.ViewModelKey
import net.decentr.module_decentr.presentation.login.*
import net.decentr.module_decentr.presentation.login.barcode.QRLoginFragment
import net.decentr.module_decentr.presentation.login.barcode.QRLoginViewModel

@Module
interface LoginModule {
    @ContributesAndroidInjector
    fun bindLoginActivity(): LoginActivity

    @ContributesAndroidInjector
    fun contributeQRLoginFragment(): QRLoginFragment

    @ContributesAndroidInjector
    fun contributeSignInPasswordFragment(): SignInPasswordFragment

    @ContributesAndroidInjector
    fun contributeSignInSeedPhraseFragment(): SignInSeedPhraseFragment

    @ContributesAndroidInjector
    fun contributeSignInEmailFragment(): SignInEmailFragment

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
}