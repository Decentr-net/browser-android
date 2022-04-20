package net.decentr.module_decentr.domain.errors

interface ErrorHandler {
    operator fun invoke(throwable: Throwable, defaultError: String? = null): DecException
}