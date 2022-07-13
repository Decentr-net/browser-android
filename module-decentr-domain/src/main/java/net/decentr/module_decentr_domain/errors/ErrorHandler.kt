package net.decentr.module_decentr_domain.errors

interface ErrorHandler {
    operator fun invoke(throwable: Throwable, defaultError: String? = null): DecException
}