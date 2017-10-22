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
-printmapping bin/classes-processed.map

# You can print out the seeds that are matching the keep options below.

-printseeds bin/classes-processed.seeds
-dump class_files.txt
-printseeds seeds.txt
-printusage unused.txt

-keep class !com.janeullah.apps.healthinspectionviewer.constants.HerokuConstants { *; }
