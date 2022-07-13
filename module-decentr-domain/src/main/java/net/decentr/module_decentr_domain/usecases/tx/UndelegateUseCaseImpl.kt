package net.decentr.module_decentr_domain.usecases.tx

import net.decentr.module_decentr_domain.repository.TxRepository
import javax.inject.Inject

class UndelegateUseCaseImpl @Inject constructor(private val repository: TxRepository): UndelegateUseCase {
    override suspend fun invoke(
        accountAddress: String,
        fromValidator: String,
        delegateAmount: String,
        fee: String
    ): String {
        return repository.undelegate(accountAddress, fromValidator, delegateAmount, fee)
    }
}