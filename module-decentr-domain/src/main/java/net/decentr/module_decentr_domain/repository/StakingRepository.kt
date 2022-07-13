package net.decentr.module_decentr_domain.repository

import net.decentr.module_decentr_domain.models.staking.Validator

interface StakingRepository {
    suspend fun getDelegatorDelegated(address: String): List<Pair<String, String>>
    suspend fun getPool(): Pair<String, String>
    suspend fun getValidators(status: Validator.Status): List<Validator>
    suspend fun getValidator(address: String): Validator

    fun closeChannel()
}