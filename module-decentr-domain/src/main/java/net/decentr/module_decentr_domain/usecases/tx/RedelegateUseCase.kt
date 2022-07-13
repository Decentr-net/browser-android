package net.decentr.module_decentr_domain.usecases.tx

interface RedelegateUseCase {
    suspend operator fun invoke(accountAddress: String, fromValidatorAddress: String, toValidatorAddress: String, redelegateAmount: String, fee: String): String
}