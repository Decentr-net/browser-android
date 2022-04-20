package net.decentr.module_decentr.remote.services

import net.decentr.module_decentr.remote.model.BalanceDECResponse
import net.decentr.module_decentr.remote.model.BalancePDVResponse
import net.decentr.module_decentr.remote.model.BlockchainAccountResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface DecentrService {

    @GET("cosmos/bank/v1beta1/balances/{address}")
    suspend fun getBalancePDV(@Path("address") address: String): BalancePDVResponse

    @GET("decentr/token/balance/{address}")
    suspend fun getBalanceDEC(@Path("address") address: String): BalanceDECResponse

    @GET("cosmos/auth/v1beta1/accounts/{address}")
    suspend fun checkAddressInBlockchain(@Path("address") address: String): BlockchainAccountResponse

}