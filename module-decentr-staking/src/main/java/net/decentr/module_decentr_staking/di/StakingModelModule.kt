package net.decentr.module_decentr_staking.di

import dagger.Module
import net.decentr.module_decentr_staking.di.data.DataModule
import net.decentr.module_decentr_staking.di.domain.DomainModule
import net.decentr.module_decentr_staking.di.presentation.PresentationModule
import net.decentr.module_decentr_staking.di.remote.RemoteModule

@Module(includes = [
    DomainModule::class,
    DataModule::class,
    PresentationModule::class,
    RemoteModule::class
])
interface StakingModelModule