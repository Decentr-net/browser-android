package net.decentr.module_decentr_domain.usecases.tx

import net.decentr.module_decentr_domain.repository.TxRepository
import javax.inject.Inject

class DelegateUseCaseImpl @Inject constructor(private val repository: TxRepository): DelegateUseCase {
    override suspend fun invoke(
        accountAddress: String,
        toValidatorAddress: String,
        delegateAmount: String,
        fee: String
    ): String {
        return repository.delegate(accountAddress, toValidatorAddress, delegateAmount, fee)
    }
}