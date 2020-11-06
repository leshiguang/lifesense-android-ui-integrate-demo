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
#webview
-keep  class * extends com.lifesense.weidong.lswebview.webview.base.JsEntity { *; }
-keep class com.lifesense.weidong.lswebview.webview.LSWebViewManager { *; }
#device
-keep class com.lifesense.component.devicemanager.application.**{ *; }
-keep class * implements android.os.Parcelable {*;}
-keep class * implements com.lifesense.component.devicemanager.infrastructure.bean.LSJSONSerializable {*;}
-keep class com.lifesense.component.devicemanager.component.receiver.BluetoothStatusChangeTrigger{*;}
-keep class com.lifesense.component.devicemanager.component.receiver.BluetoothStatusChangeTrigger$*{*;}
#net
-keep class * extends com.lifesense.weidong.lzsimplenetlibs.net.invoker.JsonResponse{*;}
#greendao
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties { *; }

# If you DO use SQLCipher:
-keep class org.greenrobot.greendao.database.SqlCipherEncryptedHelper { *; }

# If you do NOT use SQLCipher:
-dontwarn net.sqlcipher.database.**
# If you do NOT use RxJava:
-dontwarn rx.**
# universal-image-loader 混淆
-dontwarn com.nostra13.universalimageloader.**
-keep class com.nostra13.universalimageloader.** { *; }
#weibo分享
-dontwarn com.weibo.sdk.Android.WeiboDialog
-dontwarn android.NET.http.SslError
-dontwarn android.webkit.WebViewClient
-keep public class android.Net.http.SslError{
*;
}
-keep public class android.webkit.WebViewClient{
*;
}
-keep public class android.webkit.WebChromeClient{
*;
}
-keep public interface android.webkit.WebChromeClient$CustomViewCallback {
*;
}
-keep public interface android.webkit.ValueCallback {
*;
}
-keep class * implements android.webkit.WebChromeClient {
*;
}

-keep class com.sina.weibo.sdk.api.** {
*;
}

-keep class com.sina.weibo.sdk.** {
*;
}
#微信分享
-keep class com.tencent.mm.opensdk.** {
    *;
}

-keep class com.tencent.wxop.** {
    *;
}

-keep class com.tencent.mm.sdk.** {
    *;
}
