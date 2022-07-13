package net.decentr.module_decentr_staking.di.presentation

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import net.decentr.module_decentr_common.di.keys.keys.ViewModelKey
import net.decentr.module_decentr_staking.presentation.staking.*

@Module
interface StakingModule {

    @ContributesAndroidInjector
    fun contributeStakingViewPagerFragment(): StakingViewPagerFragment

    @ContributesAndroidInjector
    fun contributeValidatorsFragment(): ValidatorsFragment

    @Binds
    @IntoMap
    @ViewModelKey(ValidatorsViewModel::class)
    fun bindValidatorsViewModel(viewModel: ValidatorsViewModel): ViewModel

    @ContributesAndroidInjector
    fun contributeRewardsFragment(): RewardsFragment

    @Binds
    @IntoMap
    @ViewModelKey(RewardsViewModel::class)
    fun bindRewardsViewModel(viewModel: RewardsViewModel): ViewModel

    @ContributesAndroidInjector
    fun contributeValidatorProfileFragment(): ValidatorProfileFragment

    @Binds
    @IntoMap
    @ViewModelKey(ValidatorProfileViewModel::class)
    fun bindValidatorProfileViewModel(viewModel: ValidatorProfileViewModel): ViewModel

    @ContributesAndroidInjector
    fun contributeValidatorActionsFragment(): ValidatorActionsFragment

    @Binds
    @IntoMap
    @ViewModelKey(ValidatorActionsViewModel::class)
    fun bindRValidatorActionsViewModel(viewModel: ValidatorActionsViewModel): ViewModel

    @ContributesAndroidInjector
    fun contributeValidatorActionsResultFragment(): ValidatorActionResult

}