package net.decentr.module_decentr.remote.errors

import com.google.gson.Gson
import net.decentr.module_decentr.domain.errors.DecException
import net.decentr.module_decentr.domain.errors.ERROR_CODE_TIME_OUT
import net.decentr.module_decentr.domain.errors.ErrorHandler
import net.decentr.module_decentr.remote.model.ErrorResponse
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class ErrorHandlerImpl @Inject constructor(private val gson: Gson) : ErrorHandler {

    override fun invoke(throwable: Throwable, defaultError: String?): DecException {
        return when (throwable) {
            is IOException -> DecException(defaultError ?: throwable.message, throwable, ERROR_CODE_TIME_OUT)
            is HttpException -> {
                val code = throwable.code()
                convertErrorBody(throwable)?.let {
                    when (it.code) {
                        5 -> return DecException("Profile not found by this address", throwable, code)
                    }
                    return DecException(defaultError ?: it.message, throwable, code)
                }
                DecException(defaultError ?: throwable.message, throwable, code)
            }
            is DecException -> throwable
            else -> {
                DecException(defaultError ?: throwable.message, throwable)
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