package net.decentr.module_decentr_domain.usecases.tx

import net.decentr.module_decentr_domain.repository.TxRepository
import javax.inject.Inject

class RedelegateUseCaseImpl @Inject constructor(private val repository: TxRepository): RedelegateUseCase {
    override suspend fun invoke(
        accountAddress: String,
        fromValidatorAddress: String,
        toValidatorAddress: String,
        redelegateAmount: String,
        fee: String
    ): String {
        return repository.redelegate(accountAddress, fromValidatorAddress, toValidatorAddress, redelegateAmount, fee)
    }
}