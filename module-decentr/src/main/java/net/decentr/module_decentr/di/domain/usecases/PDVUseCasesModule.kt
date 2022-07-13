package net.decentr.module_decentr.di.domain.usecases

import dagger.Binds
import dagger.Module
import net.decentr.module_decentr_domain.usecases.pdv.*

@Module
interface PDVUseCasesModule {
    @Binds
    fun bindSendPDVUseCase(useCase: SendPDVUseCaseImpl): SendPDVUseCase
    @Binds
    fun bindSavePDVUseCase(useCase: SavePDVUseCaseImpl): SavePDVUseCase
    @Binds
    fun bindRemovePDVUseCase(useCase: RemovePDVUseCaseImpl): RemovePDVUseCase
    @Binds
    fun bindGetPDVCountUseCase(useCase: GetPDVCountUseCaseImpl): GetPDVCountUseCase
    @Binds
    fun bindGetAllPDVUseCase(useCase: GetAllPDVUseCaseImpl): GetAllPDVUseCase
    @Binds
    fun bindCheckUnicPDVUseCase(useCase: CheckUnicPDVUseCaseImpl): CheckUnicPDVUseCase
}