package net.decentr.module_decentr.presentation.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import net.decentr.module_decentr.di.FragmentFactory
import net.decentr.module_decentr.di.ViewModelFactory

@Module(includes = [
    LoginModule::class
])
interface PresentationModule {
    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    fun bindFragmentFactory(factory: FragmentFactory): androidx.fragment.app.FragmentFactory
}