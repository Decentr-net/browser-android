package net.decentr.module_decentr_domain.usecases.tx

import net.decentr.module_decentr_domain.repository.TxRepository
import javax.inject.Inject

class SimulateUndelegateUseCaseImpl @Inject constructor(private val repository: TxRepository): SimulateUndelegateUseCase {
    override suspend fun invoke(
        accountAddress: String,
        fromValidator: String,
        delegateAmount: String
    ): Pair<Double, Double> {
        return repository.simulateUndelegate(accountAddress, fromValidator, delegateAmount)
    }
}