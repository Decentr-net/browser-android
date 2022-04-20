package net.decentr.module_decentr.di.keys

import android.content.ContentProvider
import dagger.MapKey
import kotlin.reflect.KClass

@MapKey
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ContentProviderKey(val value: KClass<out ContentProvider>)