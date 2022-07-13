package net.decentr.module_decentr_domain.usecases.tx

interface DelegateUseCase {
    suspend operator fun invoke(accountAddress: String, toValidatorAddress: String, delegateAmount: String, fee: String): String
}