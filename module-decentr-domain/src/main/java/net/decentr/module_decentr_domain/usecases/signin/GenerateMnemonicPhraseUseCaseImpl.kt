package net.decentr.module_decentr_domain.usecases.signin

import net.decentr.module_decentr_domain.repository.SignInRepository
import javax.inject.Inject

class GenerateMnemonicPhraseUseCaseImpl @Inject constructor(private val repository: SignInRepository):
    GenerateMnemonicPhraseUseCase {

    override suspend fun invoke(): String {
        return repository.generateMnemonic()
    }
}