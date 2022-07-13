package net.decentr.module_decentr_domain.usecases.balance

import net.decentr.module_decentr_domain.models.BalanceDEC

interface GetBalanceDECUseCase {
    suspend operator fun invoke(address: String): BalanceDEC
}