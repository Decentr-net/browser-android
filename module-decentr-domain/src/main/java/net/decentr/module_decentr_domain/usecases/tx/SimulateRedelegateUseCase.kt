package net.decentr.module_decentr_domain.usecases.tx

interface SimulateRedelegateUseCase {
    suspend operator fun invoke(accountAddress: String, fromValidatorAddress: String, toValidatorAddress: String, redelegateAmount: String): Pair<Double, Double>
}