package net.decentr.module_decentr_domain.usecases.tx

interface SimulateUndelegateUseCase {
    suspend operator fun invoke(accountAddress: String, fromValidator: String, delegateAmount: String): Pair<Double, Double>
}