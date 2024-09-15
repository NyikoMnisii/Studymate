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
# General Android
-dontwarn android.**
-keep class android.** { *; }

# AndroidX
-dontwarn androidx.**
-keep class androidx.** { *; }

# PDF Viewer
-dontwarn com.github.barteksc.pdfviewer.**
-keep class com.github.barteksc.pdfviewer.** { *; }

# PRDownloader
-dontwarn com.mindorks.android.prdownloader.**
-keep class com.mindorks.android.prdownloader.** { *; }

# Picasso
-dontwarn com.squareup.picasso.**
-keep class com.squareup.picasso.** { *; }

# Volley
-dontwarn com.android.volley.**
-keep class com.android.volley.** { *; }

# Simple Ads SDK
-dontwarn com.github.solodroidx.simple_ads_sdk.**
-keep class com.github.solodroidx.simple_ads_sdk.** { *; }

# Solodroidx Push SDK
-dontwarn com.github.solodroidx.solodroidx_push_sdk.**
-keep class com.github.solodroidx.solodroidx_push_sdk.** { *; }

# Multidex
-dontwarn androidx.multidex.**
-keep class androidx.multidex.** { *; }

# WorkManager
-dontwarn androidx.work.**
-keep class androidx.work.** { *; }

# Lifecycle
-dontwarn androidx.lifecycle.**
-keep class androidx.lifecycle.** { *; }

# Play Core
-dontwarn com.google.android.play.**
-keep class com.google.android.play.** { *; }

# Facebook Ads
-keep class com.facebook.ads.** { *; }
-dontwarn com.facebook.ads.**

-keep public class com.app.studymate.models** { *; }
-keep class com.shockwave.**

-dontwarn org.conscrypt.Conscrypt
-dontwarn org.conscrypt.OpenSSLProvider