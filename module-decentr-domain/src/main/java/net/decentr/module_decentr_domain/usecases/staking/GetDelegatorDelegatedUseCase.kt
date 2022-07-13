package net.decentr.module_decentr_domain.usecases.staking

interface GetDelegatorDelegatedUseCase {
    suspend operator fun invoke(address: String): List<Pair<String, String>>
}