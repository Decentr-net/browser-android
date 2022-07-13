package net.decentr.module_decentr_domain.usecases.balance

import net.decentr.module_decentr_domain.models.BalancePDV
import net.decentr.module_decentr_domain.repository.BalanceRepository
import javax.inject.Inject

class GetBalancePDVUseCaseImpl @Inject constructor(private val repository: BalanceRepository):
    GetBalancePDVUseCase {

    override suspend fun invoke(address: String): BalancePDV {
        return repository.getBalancePDV(address)
    }
}