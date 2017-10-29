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

#https://github.com/krschultz/android-proguard-snippets/tree/master/libraries
#https://stackoverflow.com/questions/5068251/android-what-are-the-recommended-configurations-for-proguard
#https://stuff.mit.edu/afs/sipb/project/android/sdk/android-sdk-linux/tools/proguard/docs/index.html#manual/troubleshooting.html
#https://stuff.mit.edu/afs/sipb/project/android/sdk/android-sdk-linux/tools/proguard/docs/index.html#manual/examples.html
#https://developer.android.com/studio/write/annotations.html, https://developer.android.com/studio/build/shrink-code.html
#https://gist.github.com/Jackgris/c4a71328b1ae346cba04
#https://stackoverflow.com/questions/30361621/proguard-stack-in-crashlytics
#https://www.simpligility.com/2010/12/hints-for-using-proguard-on-your-android-app/

-verbose
-dump class_files.txt
-printseeds seeds.txt
-printconfiguration config.txt
-printusage unused.txt
#-printmapping mapping.txt
#-printmapping build/outputs/mapping/release/mapping.txt


#https://docs.fabric.io/android/crashlytics/dex-and-proguard.html
#For Fabric to properly de-obfuscate your crash reports, you need to remove this line from your configuration file, or we won’t be able to automatically upload your mapping file:
#-keepresourcexmlelements manifest/application/meta-data@name=io.fabric.ApiKey
#-printmapping mapping.txt

# Preverification is irrelevant for the dex compiler and the Dalvik VM.
-dontpreverify

# Reduce the size of the output some more.
-repackageclasses ''
-allowaccessmodification

# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes InnerClasses


# Keep a fixed source file attribute and all line number tables to get line
# numbers in the stack traces.
# You can comment this out if you're not interested in stack traces.
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable

# Switch off some optimizations that trip older versions of the Dalvik VM.
-optimizations !code/simplification/arithmetic


#https://github.com/getsentry/sentry-java/issues/373
-dontwarn javax.naming.**
-dontwarn javax.servlet.**
-dontwarn org.slf4j.**
-dontwarn android.support.**
-dontwarn sun.misc.**
-dontwarn org.mockito.**
-dontwarn sun.reflect.**
-dontwarn android.test.**

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

-keep class android.support.** { *; }
-keep interface android.support.** { *; }


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

#keeping custom exceptions
-keep public class * extends java.lang.Exception

#https://stackoverflow.com/questions/33047806/proguard-duplicate-definition-of-library-class
-dontnote android.net.http.*
-dontnote org.apache.commons.codec.**
-dontnote org.apache.http.**

#https://stackoverflow.com/questions/36261672/proguard-problems-in-android-studio
# support-v4
#https://stackoverflow.com/questions/18978706/obfuscate-android-support-v7-widget-gridlayout-issue
#-dontwarn android.support.v4.**
#-keep class android.support.v4.app.** { *; }
#-keep interface android.support.v4.app.** { *; }
#-keep class android.support.v4.** { *; }


# support-v7
#-dontwarn android.support.v7.**
#-keep class android.support.v7.internal.** { *; }
#-keep interface android.support.v7.internal.** { *; }
#-keep class android.support.v7.** { *; }

# support design
#@link http://stackoverflow.com/a/31028536
#-dontwarn android.support.design.**
#-keep class android.support.design.** { *; }
#-keep interface android.support.design.** { *; }
#-keep public class android.support.design.R$* { *; }