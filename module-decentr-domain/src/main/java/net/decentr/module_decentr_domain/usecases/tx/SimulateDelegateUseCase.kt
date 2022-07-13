package net.decentr.module_decentr_domain.usecases.tx

interface SimulateDelegateUseCase {
    suspend operator fun invoke(accountAddress: String, toValidatorAddress: String, delegateAmount: String): Pair<Double, Double>
}