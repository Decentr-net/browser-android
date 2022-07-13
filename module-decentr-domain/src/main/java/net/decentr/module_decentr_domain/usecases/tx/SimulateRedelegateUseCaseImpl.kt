package net.decentr.module_decentr_domain.usecases.tx

import net.decentr.module_decentr_domain.repository.TxRepository
import javax.inject.Inject

class SimulateRedelegateUseCaseImpl @Inject constructor(private val repository: TxRepository): SimulateRedelegateUseCase {
    override suspend fun invoke(
        accountAddress: String,
        fromValidatorAddress: String,
        toValidatorAddress: String,
        redelegateAmount: String
    ): Pair<Double, Double> {
        return repository.simulateRedelegate(accountAddress, fromValidatorAddress, toValidatorAddress, redelegateAmount)
    }
}