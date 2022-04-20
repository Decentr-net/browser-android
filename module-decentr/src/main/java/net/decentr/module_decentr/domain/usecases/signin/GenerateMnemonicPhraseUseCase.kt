package net.decentr.module_decentr.domain.usecases.signin

interface GenerateMnemonicPhraseUseCase {
    suspend operator fun invoke(): String
}