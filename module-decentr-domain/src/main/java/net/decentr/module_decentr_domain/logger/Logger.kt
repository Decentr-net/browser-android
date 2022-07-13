package net.decentr.module_decentr_domain.logger

interface Logger {
    fun w(t: Throwable?)
    fun e(t: Throwable?)
    fun log(message: String?)
    fun log(tag: String, message: String?)
}