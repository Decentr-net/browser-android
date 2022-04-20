package net.decentr.module_decentr.domain.usecases.pdv

import dagger.Binds
import dagger.Module

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