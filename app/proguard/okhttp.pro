# OkHttp
# Ignore warnings: https://github.com/square/okhttp/wiki/FAQs
# Ignore warnings: https://github.com/square/okio/issues/60
# https://github.com/square/okhttp/issues/2230
# Ignore warnings: https://github.com/square/retrofit/issues/435
-dontwarn okio.**
-dontwarn okhttp3.**

-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }