
# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\tools\adt-bundle-windows-x86_64-20131030\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
  public *;
}
-dontskipnonpubliclibraryclasses # 不忽略非公共的库类
-optimizationpasses 5            # 指定代码的压缩级别
-dontusemixedcaseclassnames      # 是否使用大小写混合
-dontpreverify                   # 混淆时是否做预校验
-verbose                         # 混淆时是否记录日志
-keepattributes *Annotation*     # 保持注解
-ignorewarning                   # 忽略警告
-dontoptimize                    # 优化不优化输入的类文件

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*  # 混淆时所采用的算法

#保持哪些类不被混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

#生成日志数据，gradle build时在本项目根目录输出
-dump class_files.txt            #apk包内所有class的内部结构
-printseeds seeds.txt            #未混淆的类和成员
-printusage unused.txt           #打印未被使用的代码
-printmapping mapping.txt        #混淆前后的映射

-keep public class * extends android.support.** #如果有引用v4或者v7包，需添加
-keep class com.chinaums.**
-keepattributes Signature        #不混淆泛型
-keepnames class * implements java.io.Serializable #不混淆Serializable

-keepclassmembers class **.R$* { #不混淆资源类
public static <fields>;
}
-keepclasseswithmembernames class * {  # 保持 native 方法不被混淆
    native <methods>;
}
-keepclasseswithmembers class * {      # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {      # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity { # 保持自定义控件类不被混淆
    public void *(android.view.View);
}
-keepclassmembers enum * {             # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable {         # 保持 Parcelable 不被混淆
    public static final android.os.Parcelable$Creator *;
}





#友盟推送
-dontwarn com.taobao.**
-dontwarn anet.channel.**
-dontwarn anetwork.channel.**
-dontwarn org.android.**
-dontwarn org.apache.thrift.**
-dontwarn com.xiaomi.**
-dontwarn com.huawei.**

-keepattributes *Annotation*

-keep class com.taobao.** {*;}
-keep class org.android.** {*;}
-keep class anet.channel.** {*;}
-keep class com.umeng.** {*;}
-keep class com.xiaomi.** {*;}
-keep class com.huawei.** {*;}
-keep class org.apache.thrift.** {*;}

-keep class com.alibaba.sdk.android.**{*;}
-keep class com.ut.**{*;}
-keep class com.ta.**{*;}

-keep public class **.R$*{
   public static final int *;
}

#友盟统计
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keep public class [com.huoniao.oc].R$*{
public static final int *;
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#（可选）避免Log打印输出
-assumenosideeffects class android.util.Log {
   public static *** v(...);
   public static *** d(...);
   public static *** i(...);
   public static *** w(...);
 }








#pos
-keep public class com.google.zxing.client.**{*;}
-dontwarn    com.google.zxing.client.**
-keep public class com.ums.umsicc.driver.**{*;}
-dontwarn  com.ums.umsicc.driver.**
-keep public class org.apache.log4j.**{*;}
-dontwarn  org.apache.log4j.**
-keep public class com.chinaums.xgdswipe.**{*;}
-dontwarn com.chinaums.xgdswipe.*

-keep public class com.chinaums.xgdswipe.apiimpl.**{*;}
-dontwarn com.chinaums.xgdswipe.apiimpl.**

-keep public class com.chinaums.commondhjt.**{*;}
-dontwarn  com.chinaums.commondhjt.**

-keep public class com.landicorp.android.**{*;}
-dontwarn  com.landicorp.android.**
-keep public class okhttp3.internal.huc.**{*;}
-dontwarn okhttp3.internal.huc.**
-keep public class okio.DeflaterSink
-dontwarn  okio.DeflaterSink
-keep public class okio.Okio
-dontwarn   okio.Okio

-keep public class com.chinaums.xgdswipe.util.p
-dontwarn com.chinaums.xgdswipe.util.p
-keep public class com.chinaums**{*;}
-keep public class com.ums**{*;}

# 反射
-keepattributes EnclosingMethod

#混淆后 记录对应的映射
-printmapping build/outputs/mapping/debug/mapping.txt

#butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewInjector { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

#百度混淆
-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-dontwarn com.baidu.**

#鲁班
-keep public interface rx.functions.**{*;}
-dontwarn rx.functions.**
-keep public class rx.Observable.**{*;}
-dontwarn rx.Observable.**

-keep public class rx.Observable{*;}
-dontwarn rx.Observable

-keep public class rx.schedulers.**{*;}
-dontwarn rx.schedulers.**
-keep public class rx.android.schedulers.**{*;}
-dontwarn rx.android.schedulers.**
-keep public class com.alipay.android.phone.**{*;}
-dontwarn com.alipay.android.phone.**

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# EventBus
-keep public class de.greenrobot.event.** { *; }
-dontwarn  de.greenrobot.event.**
#保护指定类成员
-keepclassmembers class ** {
    public void onEvent*(**);
    void onEvent*(**);
}

# Serializable
-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
static final long serialVersionUID;
private static final java.io.ObjectStreamField[] serialPersistentFields;
!static !transient <fields>;
private void writeObject(java.io.ObjectOutputStream);
private void readObject(java.io.ObjectInputStream);
java.lang.Object writeReplace();
java.lang.Object readResolve();
}

#实体类不混淆
-keepnames public class com.huoniao.oc.bean.**{*;}
 -dontwarn com.huoniao.oc.bean.**

 #photoview
 -keep class uk.co.senab.photoview** { *; }
 -keep interface uk.co.senab.photoview** { *; }

 #MPAndroidChart
 -keep class com.github.mikephil.charting.** { *; }

# design包下都不混淆     这里我只使用design包下的 TabLayout所以标注一下
-keep class android.support.design.widget.**{*;}