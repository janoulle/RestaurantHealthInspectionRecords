# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\jane\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#https://stuff.mit.edu/afs/sipb/project/android/sdk/android-sdk-linux/tools/proguard/docs/index.html#manual/troubleshooting.html
#https://stuff.mit.edu/afs/sipb/project/android/sdk/android-sdk-linux/tools/proguard/docs/index.html#manual/examples.html
#https://developer.android.com/studio/write/annotations.html, https://developer.android.com/studio/build/shrink-code.html
#https://gist.github.com/Jackgris/c4a71328b1ae346cba04
#https://stackoverflow.com/questions/30361621/proguard-stack-in-crashlytics

# Save the obfuscation mapping to a file, so you can de-obfuscate any stack
# traces later on.
# uncommenting for crashlytics use
#-printmapping bin/classes-processed.map

# You can print out the seeds that are matching the keep options below.

-printseeds bin/classes-processed.seeds
-dump class_files.txt
-printseeds seeds.txt
-printusage unused.txt

# Preverification is irrelevant for the dex compiler and the Dalvik VM.

-dontpreverify

# Reduce the size of the output some more.

-repackageclasses ''
-allowaccessmodification

# Switch off some optimizations that trip older versions of the Dalvik VM.

-optimizations !code/simplification/arithmetic

# Keep a fixed source file attribute and all line number tables to get line
# numbers in the stack traces.
# You can comment this out if you're not interested in stack traces.

-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable

# RemoteViews might need annotations.

-keepattributes *Annotation*

# Preserve all fundamental application classes.

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider

# Preserve all View implementations, their special context constructors, and
# their setters.

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}


# Preserve all classes that have special context constructors, and the
# constructors themselves.

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

# Preserve all classes that have special context constructors, and the
# constructors themselves.

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# Preserve all possible onClick handlers.

-keepclassmembers class * extends android.content.Context {
   public void *(android.view.View);
   public void *(android.view.MenuItem);
}

-keepclassmembers class * implements android.os.Parcelable {
    static android.os.Parcelable$Creator CREATOR;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-dontwarn android.support.**




# Preserve annotated Javascript interface methods.

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# Preserve the required interface from the License Verification Library
# (but don't nag the developer if the library is not used at all).

-keep public interface com.android.vending.licensing.ILicensingService

-dontnote com.android.vending.licensing.ILicensingService

# Preserve all native method names and the names of their classes.

-keepclasseswithmembernames,includedescriptorclasses class * {
    native <methods>;
}

# Preserve the special static methods that are required in all enumeration
# classes.

-keepclassmembers,allowoptimization enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Explicitly preserve all serialization members. The Serializable interface
# is only a marker interface, so it wouldn't save them.
# You can comment this out if your application doesn't use serialization.
# If your code contains serializable classes that have to be backward
# compatible, please refer to the manual.

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# If you wish, you can let the optimization step remove Android logging calls.
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}


#Retrofit
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

#Okio
-dontwarn okio.**


#Butterknife
-keep class **$$ViewBinder { *; }

# Retain generated class which implement Unbinder.
-keep public class * implements butterknife.Unbinder { public <init>(**, android.view.View); }

# Prevent obfuscation of types which use ButterKnife annotations since the simple name
# is used to reflectively look up the generated ViewBinding.
-keep class butterknife.*
-keepclasseswithmembernames class * { @butterknife.* <methods>; }
-keepclasseswithmembernames class * { @butterknife.* <fields>; }

#Crashlytics
-keepattributes *Annotation*
-keep public class * extends java.lang.Exception
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

#FireBase
#https://github.com/firebase/quickstart-android/blob/master/database/app/proguard-rules.pro
#https://stackoverflow.com/questions/26273929/what-proguard-configuration-do-i-need-for-firebase-on-android
-keepattributes Signature
#-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes InnerClasses

# Basic ProGuard rules for Firebase Android SDK 2.0.0+
-keep class com.firebase.** { *; }
-keep class org.apache.** { *; }
-keepnames class com.fasterxml.jackson.** { *; }
-keepnames class javax.servlet.** { *; }
-keepnames class org.ietf.jgss.** { *; }
-dontwarn org.apache.**
-dontwarn org.w3c.dom.**
-dontwarn org.joda.time.**
-dontwarn org.shaded.apache.**
-dontwarn org.ietf.jgss.**

#https://stackoverflow.com/questions/26273929/what-proguard-configuration-do-i-need-for-firebase-on-android
#App Libs
-keep class com.janeullah.apps.healthinspectionviewer.** { *; }
# This rule will properly ProGuard all the model classes in
# the package com.yourcompany.models. Modify to fit the structure
# of your app.
-keepclassmembers class com.janeullah.apps.healthinspectionviewer.dtos.** {
*;
}
