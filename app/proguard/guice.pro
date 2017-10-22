
#Guice
#https://github.com/google/guava/issues/2926
#https://stackoverflow.com/questions/13208784/proguard-returned-with-error-code-1-see-console
#https://stackoverflow.com/questions/9120338/proguard-configuration-for-guava-with-obfuscation-and-optimization
-dontwarn org.mockito.**
-dontwarn sun.reflect.**
-dontwarn android.test.**

-dontwarn com.google.common.base.**
-keep class com.google.common.base.** {*;}
-dontwarn com.google.errorprone.annotations.**
-keep class com.google.errorprone.annotations.** {*;}
-dontwarn com.google.j2objc.annotations.**
-keep class com.google.j2objc.annotations.** { *; }
-dontwarn java.lang.ClassValue
-keep class java.lang.ClassValue { *; }
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-keep class org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement { *; }

-keep @interface com.google.inject.Inject
-keepclasseswithmembers class * {
    @com.google.inject.Inject <init>(...);
}
-dontwarn com.google.inject.internal.asm.util.$TraceClassVisitor
-keep @interface com.google.inject.Inject

# deal with Google's voodoo
-dontwarn com.google.inject.internal.asm.util.$TraceClassVisitor
-dontwarn com.google.common.collect.MinMaxPriorityQueue
-keep public class com.google.inject.internal.asm.util.$*
-keep public class com.google.inject.internal.cglib.core.$*
