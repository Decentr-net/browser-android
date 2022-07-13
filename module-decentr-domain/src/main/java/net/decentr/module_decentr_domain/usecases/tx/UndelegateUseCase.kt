package net.decentr.module_decentr_domain.usecases.tx

interface UndelegateUseCase {
    suspend operator fun invoke(accountAddress: String, fromValidator: String, delegateAmount: String, fee: String): String
}