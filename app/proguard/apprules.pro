#App Libs
-keep class com.janeullah.apps.healthinspectionviewer.activities.* { *; }
-keep class com.janeullah.apps.healthinspectionviewer.adapters.* { *; }
-keep class com.janeullah.apps.healthinspectionviewer.async.* { *; }
-keep class com.janeullah.apps.healthinspectionviewer.callbacks.* { *; }
-keep class com.janeullah.apps.healthinspectionviewer.fragments.* { *; }
-keep class com.janeullah.apps.healthinspectionviewer.interfaces.* { *; }
-keep class com.janeullah.apps.healthinspectionviewer.listeners.* { *; }
-keep class com.janeullah.apps.healthinspectionviewer.services.** { *; }
-keep class com.janeullah.apps.healthinspectionviewer.utils.** { *; }
-keep class com.janeullah.apps.healthinspectionviewer.viewholder.* { *; }
-keep class com.janeullah.apps.healthinspectionviewer.constants.AppConstants
#-keep class com.janeullah.apps.healthinspectionviewer.constants.AwsElasticSearchConstants
#-keep class com.janeullah.apps.healthinspectionviewer.constants.HerokuConstants
-keep class com.janeullah.apps.healthinspectionviewer.constants.GeocodeConstants
-keep class com.janeullah.apps.healthinspectionviewer.constants.PermissionCodes
-keep class com.janeullah.apps.healthinspectionviewer.constants.YelpConstants
-keep class com.janeullah.apps.healthinspectionviewer.constants.YelpQueryParams

#https://www.guardsquare.com/en/proguard/manual/usage
#https://firebase.google.com/docs/database/android/start/
#https://stackoverflow.com/questions/26273929/what-proguard-configuration-do-i-need-for-firebase-on-android
#https://stackoverflow.com/questions/40487399/how-to-exclude-this-particuar-class-from-proguard-obfuscation
# This rule will properly ProGuard all the model classes in
# the package com.yourcompany.models. Modify to fit the structure
# of your app.
-keepclassmembers class com.janeullah.apps.healthinspectionviewer.models.** { *; }
-keepclassmembers class com.janeullah.apps.healthinspectionviewer.dtos.** {
*;
}
