package net.decentr.module_decentr_staking.remote.datasource

import cosmos.base.query.v1beta1.Pagination.PageRequest
import cosmos.staking.v1beta1.QueryOuterClass
import net.decentr.module_decentr_domain.models.staking.Validator
import net.decentr.module_decentr_staking.data.datasource.StakingDataSource
import net.decentr.module_decentr_staking.data.utils.configNetworkHosts
import net.decentr.module_decentr_staking.data.utils.configNetworkPort
import net.decentr.module_decentr_staking.remote.mapper.*
import net.decentr.module_decentr_staking.remote.services.StakingActionsService
import net.decentr.module_decentr_staking.remote.services.StakingService
import javax.inject.Inject

class StakingDataSourceImpl @Inject constructor(
    private val service: StakingService,
    private val actionsService: StakingActionsService
) : StakingDataSource {

    companion object {
        private const val DEFAULT_PAGE_SIZE = 100L
    }

    init {
        val host = configNetworkHosts.random()
        val port = configNetworkPort
        service.init(host, port)
        actionsService.init(host, port)
    }

    override suspend fun getDelegatorDelegated(address: String): List<Pair<String, String>> {
        val pageRequest = PageRequest.newBuilder().setLimit(DEFAULT_PAGE_SIZE).build()
        val request =
            QueryOuterClass.QueryDelegatorDelegationsRequest.newBuilder().setDelegatorAddr(address)
                .setPagination(pageRequest).build()
        return service.delegatorDelegations(request).delegationResponsesList.map {
            it.delegation.validatorAddress to
                    if (!it.balance.amount.isNullOrEmpty())
                        it.balance.amount.toDouble().div(DIVIDER_SMALL).toString()
                    else "0"
        }
    }

    override suspend fun getPool(): Pair<String, String> {
        val request = QueryOuterClass.QueryPoolRequest.newBuilder().build()
        val response = service.pool(request)
        return ((response.pool.bondedTokens.toLongOrNull()
            ?: DIVIDER_SMALL_LONG) / DIVIDER_SMALL_LONG).toString() to ((response.pool.notBondedTokens.toLongOrNull()
            ?: DIVIDER_SMALL_LONG) / DIVIDER_SMALL_LONG).toString()
    }

    override suspend fun getValidators(status: Int): List<Validator> {
        val pageRequest = PageRequest.newBuilder().setLimit(DEFAULT_PAGE_SIZE).build()
        val request = QueryOuterClass.QueryValidatorsRequest.newBuilder().setPagination(pageRequest)
            .setStatus(status.toLong()).build()
        return service.validators(request).validatorsList.map { it.toDomain() }
    }

    override suspend fun getValidator(address: String): Validator {
        val request =
            QueryOuterClass.QueryValidatorRequest.newBuilder().setValidatorAddr(address).build()
        return service.validator(request).validator.toDomain()
    }

    override fun closeChannel() {
        service.closeChannel()
        actionsService.closeChannel()
    }
}