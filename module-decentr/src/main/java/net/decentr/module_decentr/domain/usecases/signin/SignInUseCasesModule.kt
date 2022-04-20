package net.decentr.module_decentr.domain.usecases.signin

import dagger.Binds
import dagger.Module

@Module
interface SignInUseCasesModule {

    @Binds
    fun bindGenerateMnemonicPhraseUseCase(useCase: GenerateMnemonicPhraseUseCaseImpl): GenerateMnemonicPhraseUseCase

    @Binds
    fun bindGetProfileUseCase(useCase: GetProfileUseCaseImpl): GetProfileUseCase

    @Binds
    fun bindCheckAddressInBlockchaineUseCase(useCase: CheckAddressInBlockchainUseCaseImpl): CheckAddressInBlockchainUseCase

    @Binds
    fun bindGetProfileFlowUseCase(useCase: GetProfileFlowUseCaseImpl): GetProfileFlowUseCase

    @Binds
    fun bindSaveProfileUseCase(useCase: SaveProfileUseCaseImpl): SaveProfileUseCase

    @Binds
    fun bindRemoveUserUseCase(useCase: RemoveUserUseCaseImpl): RemoveUserUseCase

    @Binds
    fun bindClearKeysUseCase(useCase: ClearKeysUseCaseImpl): ClearKeysUseCase

    @Binds
    fun bindStoreKeysUseCase(useCase: StoreKeysUseCaseImpl): StoreKeysUseCase
}