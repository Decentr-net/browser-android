package net.decentr.module_decentr_domain.usecases.staking

import net.decentr.module_decentr_domain.repository.DistributionRepository
import net.decentr.module_decentr_domain.repository.StakingRepository
import net.decentr.module_decentr_domain.repository.TxRepository
import javax.inject.Inject

class CloseGrpcChannelUseCaseImpl @Inject constructor(
    private val stakingRepository: StakingRepository,
    private val distributionRepository: DistributionRepository,
    private val txRepository: TxRepository
    ): CloseGrpcChannelUseCase {

    override fun invoke() {
        stakingRepository.closeChannel()
        distributionRepository.closeChannel()
        txRepository.closeChannel()
    }
}