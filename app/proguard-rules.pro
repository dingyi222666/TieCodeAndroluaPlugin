# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class com.dingyi.tiecode.plugin.androlua.**

# -ignorewarnings                     # 忽略警告，避免打包时某些警告出现
-optimizationpasses 5               # 指定代码的压缩级别


# 混淆时不使用大小写混合，混淆后的类名为小写
-dontusemixedcaseclassnames

# 不跳过非公共的库的类
-dontskipnonpubliclibraryclasses

# 混淆后生成映射文件，map 类名->转化后类名的映射
-verbose

# Optimization is turned off by default. Dex does not like code run
# through the ProGuard optimize and preverify steps (and performs some
# of these optimizations on its own).
# 优化默认关闭，Dex不喜欢通过ProGuard的优化和预处理操作
-dontoptimize
-dontpreverify

# Note that if you want to enable optimization, you cannot just
# include optimization flags in your own project configuration file;
# instead you will need to point to the
# "proguard-android-optimize.txt" file instead of this one from your
# project.properties file.

# 保护代码中的Annotation不被混淆，这在JSON实体映射非常重要，如GSON
-keepattributes *Annotation*


# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
# 保留所有的地方native方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
# 不混淆View中的setXxx()和getXxx()方法，以保证熟悉动画能正常工作
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

# We want to keep methods in Activity that could be used in the XML attribute onClick
# 不混淆Activity中参数是View的方法，保证xml绑定的点击事件可以正常工作
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
# 不混淆枚举类中的value()和valueOf()方法
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 不混淆Parcelable实现类中的CREATOR字段，以保证Parcelable机制正常工作
-keepclassmembers class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator CREATOR;
}

# 不混淆R文件中的所有静态字段，以保证正确找到每个资源id
-keepclassmembers class **.R$* {
    public static <fields>;
}

# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
# 不对android.support包下的代码警告。(如果打包的版本低于support包下某些类的使用版本，会出现警告)
-dontwarn android.support.**

# Understand the @Keep support annotation.
# 不混淆Keep类
-keep class androidx.annotation.Keep

# 不混淆使用了注解的类和类成员
-keep @androidx.annotation.Keep class * {*;}

# 如果类中有使用了注解的方法，则不混淆类和类成员
-keepclasseswithmembers class * {
    @androidx.annotation.Keep <methods>;
}

# 如果类中有使用了注解的字段，则不混淆类和类成员
-keepclasseswithmembers class * {
    @androidx.annotation.Keep <fields>;
}

# 如果类中有使用了注解的构造函数，则不混淆类和类成员
-keepclasseswithmembers class * {
    @androidx.annotation.Keep <init>(...);
}