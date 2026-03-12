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

-keep class com.tech.pokedex.data.remote.model.** { *; }
-keepclassmembers class com.tech.pokedex.data.remote.model.** { *; }

-keepattributes Annotation, Signature
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

-keep class com.tech.pokedex.data.remote.api.** { *; }

-dontwarn retrofit2.**
-keep class retrofit2.** { *; }

-keep class com.tech.pokedex.data.local.entity.** { *; }
-keepclassmembers class com.tech.pokedex.data.local.entity.** { *; }

-keep class com.tech.pokedex.data.local.dao.** { *; }
-keep class * extends androidx.room.RoomDatabase { *; }

-keep class org.koin.** { *; }

-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}