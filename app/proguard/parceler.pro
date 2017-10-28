# Parceler rules
# Source: https://github.com/johncarl81/parceler#configuring-proguard
-keep interface org.parceler.Parcel
-keep @org.parceler.Parcel class * { *; }
-keep class **$$Parcelable { *; }
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

-keep class org.parceler.Parceler$$Parcels