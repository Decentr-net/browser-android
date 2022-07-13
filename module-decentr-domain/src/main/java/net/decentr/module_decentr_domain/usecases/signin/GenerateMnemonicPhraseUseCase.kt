package net.decentr.module_decentr_domain.usecases.signin

interface GenerateMnemonicPhraseUseCase {
    suspend operator fun invoke(): String
}