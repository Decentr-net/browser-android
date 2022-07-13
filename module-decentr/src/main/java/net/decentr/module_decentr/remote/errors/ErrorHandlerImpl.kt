package net.decentr.module_decentr.remote.errors

import com.google.gson.Gson
import net.decentr.module_decentr.remote.model.ErrorResponse
import net.decentr.module_decentr_domain.errors.*
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class ErrorHandlerImpl @Inject constructor(private val gson: Gson) :
    ErrorHandler {

    override fun invoke(throwable: Throwable, defaultError: String?): DecException {
        return when (throwable) {
            is IOException -> DecException(
                defaultError ?: throwable.message,
                throwable,
                ERROR_CODE_TIME_OUT
            )
            is HttpException -> {
                val code = throwable.code()
                convertErrorBody(throwable)?.let {
                    when (it.code) {
                        PROFILE_NOT_FOUND -> return DecException(
                            "Profile not found by this address",
                            throwable,
                            code
                        )
                        PDV_INVALID_CODE,
                        PDV_FRAUD_OR_BANNED -> return DecException(
                            throwable.message(),
                            throwable,
                            code
                        )
                    }
                    return DecException(
                        defaultError ?: it.message, throwable, code
                    )
                }
                DecException(
                    defaultError ?: throwable.message, throwable, code
                )
            }
            is DecException ->
                when {
                    throwable.cause?.cause?.message?.contains(ERROR_CODE_GRPC_REDELEGATION_NOT_READY_SUBSTRING) == true ||
                    throwable.code == ERROR_CODE_GRPC_REDELEGATION_NOT_READY -> DecException(
                        "Previous redelegation in process",
                        throwable,
                        throwable.code
                    )
                    else -> DecException(exception = throwable)
                }
            else -> {
                DecException(
                    defaultError ?: throwable.message, throwable
                )
            }
        }
    }

    private fun convertErrorBody(throwable: HttpException): ErrorResponse? {
        return try {
            throwable.response()?.errorBody()?.string()?.let {
                gson.fromJson(it, ErrorResponse::class.java)
            }
        } catch (exception: Exception) {
            null
        }
    }
}