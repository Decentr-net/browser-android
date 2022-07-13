package net.decentr.module_decentr_domain.errors

open class DecException(message: String? = null, exception: Throwable? = null, val code: Int? = null) : Exception(message, exception)