package net.decentr.module_decentr_domain.usecases.tx

interface SimulateRewardsWithdrawUseCase {
    suspend operator fun invoke(validatorAddresses: ArrayList<String?>, delegatorAddress: String): Pair<Double, Double>
}