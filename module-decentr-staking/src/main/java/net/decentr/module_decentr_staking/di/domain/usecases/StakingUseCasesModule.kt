package net.decentr.module_decentr_staking.di.domain.usecases

import dagger.Binds
import dagger.Module
import net.decentr.module_decentr_domain.usecases.staking.*

@Module
interface StakingUseCasesModule {
    @Binds
    fun bindGetValidatorsUseCase(useCase: GetValidatorsUseCaseImpl): GetValidatorsUseCase
    @Binds
    fun bindPoolUseCase(useCase: GetPoolUseCaseImpl): GetPoolUseCase
    @Binds
    fun bindGetDelegatorDelegationsUseCase(useCase: GetDelegatorDelegatedUseCaseImpl): GetDelegatorDelegatedUseCase
    @Binds
    fun bindGetValidatorProfileUseCase(useCase: GetValidatorProfileUseCaseImpl): GetValidatorProfileUseCase
    @Binds
    fun bindCloseGrpcChannelUseCase(useCase: CloseGrpcChannelUseCaseImpl): CloseGrpcChannelUseCase
}