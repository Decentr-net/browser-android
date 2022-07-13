package net.decentr.module_decentr.di

import dagger.Module
import net.decentr.module_decentr.data.DataModule
import net.decentr.module_decentr.di.domain.DomainModule
import net.decentr.module_decentr.di.local.LocalModule
import net.decentr.module_decentr.presentation.di.PresentationModule
import net.decentr.module_decentr.remote.RemoteModule

@Module(
    includes = [
        DomainModule::class,
        DataModule::class,
        LocalModule::class,
        RemoteModule::class,
        PresentationModule::class
    ]
)
interface ModelModule