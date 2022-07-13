package net.decentr.module_decentr_staking.remote.errors

import net.decentr.module_decentr_domain.errors.DecException
import net.decentr.module_decentr_domain.errors.ERROR_CODE_TIME_OUT
import net.decentr.module_decentr_domain.errors.ErrorHandler
import java.io.IOException


class ErrorHandlerImpl: ErrorHandler {

    override fun invoke(throwable: Throwable, defaultError: String?): DecException {
        return when (throwable) {
            is IOException -> DecException(
                defaultError ?: throwable.message,
                throwable,
                ERROR_CODE_TIME_OUT
            )
            is DecException -> throwable
            else -> {
                DecException(
                    defaultError ?: throwable.message, throwable
                )
            }
        }
    }
}