package net.decentr.module_decentr_staking.di.domain

import dagger.Module

@Module(includes = [
    RepositoryModule::class,
    UseCasesModule::class
])
interface DomainModule