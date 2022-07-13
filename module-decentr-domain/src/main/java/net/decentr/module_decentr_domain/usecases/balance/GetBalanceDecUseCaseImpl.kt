package net.decentr.module_decentr_domain.usecases.balance

import net.decentr.module_decentr_domain.models.BalanceDEC
import net.decentr.module_decentr_domain.repository.BalanceRepository
import javax.inject.Inject

class GetBalanceDecUseCaseImpl @Inject constructor(private val repository: BalanceRepository):
    GetBalanceDECUseCase {

    override suspend fun invoke(address: String): BalanceDEC {
        return repository.getBalanceDEC(address)
    }
}