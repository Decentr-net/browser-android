package net.decentr.module_decentr_domain.usecases.tx

import net.decentr.module_decentr_domain.repository.TxRepository
import javax.inject.Inject

class SimulateRewardsWithdrawUseCaseImpl @Inject constructor(private val repository: TxRepository) :
    SimulateRewardsWithdrawUseCase {
    override suspend fun invoke(
        validatorAddresses: ArrayList<String?>,
        delegatorAddress: String
    ): Pair<Double, Double> {
        return repository.simulateRewardsWithdraw(validatorAddresses, delegatorAddress)
    }

}