package net.decentr.module_decentr_domain.usecases.staking

interface GetPoolUseCase {
    suspend operator fun invoke(): Pair<String, String>
}