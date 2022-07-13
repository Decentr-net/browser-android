package net.decentr.module_decentr_staking.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.decentr.module_decentr_common.data.provider.KeysProvider
import net.decentr.module_decentr_domain.models.staking.Reward
import net.decentr.module_decentr_domain.repository.DistributionRepository
import net.decentr.module_decentr_staking.data.datasource.DistributionDataSource
import javax.inject.Inject

class DistributionRepositoryImpl @Inject constructor(
    private val remoteDataSource: DistributionDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): DistributionRepository {

    override suspend fun getTotalRewards(address: String): List<Reward> {
        return withContext(ioDispatcher) {
            remoteDataSource.delegatorTotalRewards(address)
        }
    }

    override fun closeChannel() {
        remoteDataSource.closeChannel()
    }
}