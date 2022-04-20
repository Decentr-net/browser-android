package net.decentr.module_decentr.data.loger

import net.decentr.module_decentr.domain.logger.Logger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoggerImpl @Inject constructor() : Logger {

    override fun w(t: Throwable?) {
//        Timber.w(t)
    }

    override fun e(t: Throwable?) {
//        Timber.e(t)
    }

    override fun log(message: String?) {
//        Timber.tag("TAG").d(message)
    }

    override fun log(tag: String, message: String?) {
//        Timber.tag(tag).d(message)
    }

}