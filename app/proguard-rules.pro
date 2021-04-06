# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-dontwarn sun.misc.**
#-keep class com.google.gson.stream.** { *; }

##---------------End: proguard configuration for Gson  ----------

# Keep models from obfuscation
-keepclassmembers enum * { *; }

-dontnote rx.internal.util.PlatformDependent

-dontwarn com.google.errorprone.annotations.**

-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt # core serialization annotations

# kotlinx-serialization-json specific. Add this if you have java.lang.NoClassDefFoundError kotlinx.serialization.json.JsonObjectSerializer
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

-keep,includedescriptorclasses class com.github.vase4kin.shared.coindesk.service.models.**$$serializer { *; }
-keep,includedescriptorclasses class com.github.vase4kin.shared.database.models.**$$serializer { *; }
-keepclassmembers class com.github.vase4kin.shared.coindesk.service.models.** {
    *** Companion;
}
-keepclassmembers class com.github.vase4kin.shared.database.models.** {
    *** Companion;
}
-keepclasseswithmembers class com.github.vase4kin.shared.coindesk.service.models.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keepclasseswithmembers class com.github.vase4kin.shared.database.models.** {
    kotlinx.serialization.KSerializer serializer(...);
}

-dontwarn javax.mail.Authenticator
-dontwarn org.codehaus.groovy.runtime.GeneratedClosure
-dontwarn groovy.lang.**
-dontwarn javax.servlet.**
-dontwarn javax.servlet.**