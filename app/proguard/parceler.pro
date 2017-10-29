# Parceler rules
# Source: https://github.com/johncarl81/parceler#configuring-proguard
# Parceler library
-keep interface org.parceler.Parcel
-keep @org.parceler.Parcel class * { *; }
-keep class **$$Parcelable { *; }