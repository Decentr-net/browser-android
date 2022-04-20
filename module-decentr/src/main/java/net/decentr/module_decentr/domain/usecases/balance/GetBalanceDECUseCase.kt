package net.decentr.module_decentr.domain.usecases.balance

import net.decentr.module_decentr.domain.models.BalanceDEC

interface GetBalanceDECUseCase {
    suspend operator fun invoke(address: String): BalanceDEC
}