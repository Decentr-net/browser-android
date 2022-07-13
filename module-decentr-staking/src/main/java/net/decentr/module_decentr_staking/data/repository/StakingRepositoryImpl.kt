package net.decentr.module_decentr_staking.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.decentr.module_decentr_domain.models.staking.Validator
import net.decentr.module_decentr_domain.repository.StakingRepository
import net.decentr.module_decentr_staking.data.datasource.StakingDataSource
import net.decentr.module_decentr_staking.remote.services.StakingService
import javax.inject.Inject

class StakingRepositoryImpl @Inject constructor(
    private val remoteDataSource: StakingDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): StakingRepository {

    override suspend fun getDelegatorDelegated(address: String): List<Pair<String, String>> {
        return withContext(ioDispatcher) {
            remoteDataSource.getDelegatorDelegated(address)
        }
    }

    override suspend fun getPool(): Pair<String, String> {
        return withContext(ioDispatcher) {
            remoteDataSource.getPool()
        }
    }

    override suspend fun getValidators(status: Validator.Status): List<Validator> {
        return withContext(ioDispatcher) {
            remoteDataSource.getValidators(status.value)
        }
    }

    override suspend fun getValidator(address: String): Validator {
        return withContext(ioDispatcher) {
            remoteDataSource.getValidator(address)
        }
    }

    override fun closeChannel() {
        remoteDataSource.closeChannel()
    }

}