package net.decentr.module_decentr.di.keys

import android.app.Activity
import dagger.MapKey
import kotlin.reflect.KClass


@MapKey
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityKey(@Suppress("unused") val value: KClass<out Activity>)
