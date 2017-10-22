#FireBase
#https://github.com/firebase/quickstart-android/blob/master/database/app/proguard-rules.pro
#https://stackoverflow.com/questions/26273929/what-proguard-configuration-do-i-need-for-firebase-on-android
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