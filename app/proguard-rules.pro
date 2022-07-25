-dontobfuscate

####################################################################################################
# Sentry
####################################################################################################

# Recommended config via https://docs.sentry.io/clients/java/modules/android/#manual-integration
# Since we don't obfuscate, we don't need to use their Gradle plugin to upload ProGuard mappings.
-keepattributes LineNumberTable,SourceFile
-dontwarn org.slf4j.**
-dontwarn javax.**

# Our addition: this class is saved to disk via Serializable, which ProGuard doesn't like.
# If we exclude this, upload silently fails (Sentry swallows a NPE so we don't crash).
# I filed https://github.com/getsentry/sentry-java/issues/572
#
# If Sentry ever mysteriously stops working after we upgrade it, this could be why.
-keep class io.sentry.event.Event { *; }

####################################################################################################
# Android and GeckoView built-ins
####################################################################################################

-dontwarn android.**
-dontwarn androidx.**
-dontwarn com.google.**
-dontwarn org.mozilla.geckoview.**

# Raptor now writes a *-config.yaml file to specify Gecko runtime settings (e.g. the profile dir). This
# file gets deserialized into a DebugConfig object, which is why we need to keep this class
# and its members.
-keep class org.mozilla.gecko.util.DebugConfig { *; }

####################################################################################################
# kotlinx.coroutines: use the fast service loader to init MainDispatcherLoader by including a rule
# to rewrite this property to return true:
# https://github.com/Kotlin/kotlinx.coroutines/blob/8c98180f177bbe4b26f1ed9685a9280fea648b9c/kotlinx-coroutines-core/jvm/src/internal/MainDispatchers.kt#L19
#
# R8 is expected to optimize the default implementation to avoid a performance issue but a bug in R8
# as bundled with AGP v7.0.0 causes this optimization to fail so we use the fast service loader instead. See:
# https://github.com/mozilla-mobile/focus-android/issues/5102#issuecomment-897854121
#
# The fast service loader appears to be as performant as the R8 optimization so it's not worth the
# churn to later remove this workaround. If needed, the upstream fix is being handled in
# https://issuetracker.google.com/issues/196302685
####################################################################################################
-assumenosideeffects class kotlinx.coroutines.internal.MainDispatcherLoader {
    boolean FAST_SERVICE_LOADER_ENABLED return true;
}

####################################################################################################
# Remove debug logs from release builds
####################################################################################################
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int d(...);
}

####################################################################################################
# Mozilla Application Services
####################################################################################################

-keep class mozilla.appservices.** { *; }

####################################################################################################
# ViewModels
####################################################################################################

-keep class org.mozilla.fenix.**ViewModel { *; }

####################################################################################################
# Adjust
####################################################################################################

-keep public class com.adjust.sdk.** { *; }
-keep class com.google.android.gms.common.ConnectionResult {
    int SUCCESS;
}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient {
    com.google.android.gms.ads.identifier.AdvertisingIdClient$Info getAdvertisingIdInfo(android.content.Context);
}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info {
    java.lang.String getId();
    boolean isLimitAdTrackingEnabled();
}
-keep public class com.android.installreferrer.** { *; }
-keep class dalvik.system.VMRuntime {
    java.lang.String getRuntime();
}
-keep class android.os.Build {
    java.lang.String[] SUPPORTED_ABIS;
    java.lang.String CPU_ABI;
}
-keep class android.content.res.Configuration {
    android.os.LocaledList getLocales();
    java.util.Locale locale;
}
-keep class android.os.LocaleList {
    java.util.Locale get(int);
}

# Keep code generated from Glean Metrics
-keep class org.mozilla.fenix.GleanMetrics.** {  *; }

# Keep motionlayout internal methods
# https://github.com/mozilla-mobile/fenix/issues/2094
-keep class androidx.constraintlayout.** { *; }

# Keep adjust relevant classes
-keep class com.adjust.sdk.** { *; }
-keep class com.google.android.gms.common.ConnectionResult {
    int SUCCESS;
}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient {
    com.google.android.gms.ads.identifier.AdvertisingIdClient$Info getAdvertisingIdInfo(android.content.Context);
}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info {
    java.lang.String getId();
    boolean isLimitAdTrackingEnabled();
}
-keep public class com.android.installreferrer.** { *; }

# Keep Android Lifecycle methods
# https://bugzilla.mozilla.org/show_bug.cgi?id=1596302
-keep class androidx.lifecycle.** { *; }

# Kotlin
-dontwarn kotlin.**
-keep class kotlin.Metadata { *; }
-keepclassmembers class **$WhenMappings {
    <fields>;
}

# Enumes
-keepclassmembers enum * { *; }

# Retrofit2
-dontnote retrofit2.Platform
-dontwarn retrofit2.Platform$Java8
-keepattributes Signature
-dontwarn okio.**

-keepclassmembers class net.decentr.module_decentr.** {
    public synthetic <methods>;
    <fields>;
}

#-keepclassmembers class net.decentr.module_decentr_stacking.** {
#    public synthetic <methods>;
#    <fields>;
#}

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Gson
-keep class com.google.gson.** { *; }
-keepattributes Signature
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

#R8 issue with proto files
-keep class * extends com.google.protobuf.GeneratedMessageLite { *; }
-keep class * extends com.google.api.** { *;}
-keep class * extends cosmos.** { *;}
-keep class * extends ics23.** { *;}
-keep class * extends net.decentr.** { *;}
-keep class * extends tendermint.** { *;}