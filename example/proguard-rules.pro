# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/akinaru/Android/Sdk/tools/proguard/proguard-android.txt
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
-useuniqueclassmembernames
-keepattributes SourceFile,LineNumberTable
-allowaccessmodification

-dontwarn android.**
-dontwarn com.**
-dontwarn org.**
-dontwarn javax.**

-keep class com.google.gdata.** { *; }

-keepnames class * implements java.io.Serializable

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keepclassmembers class * extends com.google.gdata.model.Element {
    public static com.google.gdata.model.ElementKey KEY;
    public static void registerMetadata(com.google.gdata.model.MetadataRegistry);
}
-keep class fr.bmartel.youtubetv.JavascriptInterface { *; }

-keepclassmembers,allowobfuscation class fr.bmartel.youtubetv.JavascriptInterface.** {
    <methods>;
}