
#Guice
#https://github.com/google/guava/issues/2926
#https://stackoverflow.com/questions/13208784/proguard-returned-with-error-code-1-see-console
#https://stackoverflow.com/questions/9120338/proguard-configuration-for-guava-with-obfuscation-and-optimization
#https://code.google.com/p/guava-libraries/wiki/UsingProGuardWithGuava
#https://github.com/krschultz/android-proguard-snippets/blob/master/libraries/proguard-guava.pro
# http://stackoverflow.com/questions/9120338/proguard-configuration-for-guava-with-obfuscation-and-optimization
-dontwarn javax.annotation.**
-dontwarn javax.inject.**
-dontwarn sun.misc.Unsafe
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

-dontwarn org.mockito.**
-dontwarn sun.reflect.**
-dontwarn android.test.**
-dontwarn com.google.common.base.**
-dontwarn com.google.inject.internal.asm.util.$TraceClassVisitor
-dontwarn com.google.common.collect.MinMaxPriorityQueue
-dontwarn com.google.common.collect.**
-dontwarn java.lang.ClassValue
-dontwarn com.google.j2objc.annotations.Weak
-dontwarn com.google.j2objc.annotations.**


# deal with Google's voodoo
-keep class com.google.common.base.** {*;}
-keep @interface com.google.inject.Inject
-keepclasseswithmembers class * {
    @com.google.inject.Inject <init>(...);
}
#-keep public class com.google.inject.internal.asm.util.$*
#-keep public class com.google.inject.internal.cglib.core.$*
-keep class com.google.common.collect.** { *; }
-keep class com.google.common.collect.MapMakerInternalMap$ReferenceEntry
-keep class com.google.common.cache.LocalCache$ReferenceEntry

-keep class org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement { *; }
-keep class com.google.j2objc.annotations.** { *; }
-keep class java.lang.ClassValue { *; }

-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**