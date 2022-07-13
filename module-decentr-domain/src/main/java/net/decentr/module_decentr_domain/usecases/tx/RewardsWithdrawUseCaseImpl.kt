package net.decentr.module_decentr_domain.usecases.tx

import net.decentr.module_decentr_domain.repository.TxRepository
import javax.inject.Inject

class RewardsWithdrawUseCaseImpl @Inject constructor(private val repository: TxRepository) :
    RewardsWithdrawUseCase {
    override suspend fun invoke(
        validatorAddresses: ArrayList<String?>,
        delegatorAddress: String,
        fee: String
    ): String {
        return repository.rewardsWithdraw(validatorAddresses, delegatorAddress, fee)
    }

}