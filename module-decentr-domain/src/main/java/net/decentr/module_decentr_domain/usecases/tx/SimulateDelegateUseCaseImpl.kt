package net.decentr.module_decentr_domain.usecases.tx

import net.decentr.module_decentr_domain.repository.TxRepository
import javax.inject.Inject

class SimulateDelegateUseCaseImpl @Inject constructor(private val repository: TxRepository): SimulateDelegateUseCase {
    override suspend fun invoke(
        accountAddress: String,
        toValidatorAddress: String,
        delegateAmount: String
    ): Pair<Double, Double> {
        return repository.simulateDelegate(accountAddress, toValidatorAddress, delegateAmount)
    }
}