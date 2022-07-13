package net.decentr.module_decentr_staking.data.datasource

import net.decentr.module_decentr_domain.models.staking.Validator

interface StakingDataSource {
    suspend fun getDelegatorDelegated(address: String): List<Pair<String, String>>
    suspend fun getPool(): Pair<String, String>
    suspend fun getValidators(status: Int): List<Validator>
    suspend fun getValidator(address: String): Validator

    fun closeChannel()
}