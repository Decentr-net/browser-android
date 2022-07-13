package net.decentr.module_decentr_domain.usecases.tx

interface RewardsWithdrawUseCase {
    suspend operator fun invoke(validatorAddresses: ArrayList<String?>, delegatorAddress: String, fee: String): String
}