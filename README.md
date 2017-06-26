# WebView使用总结

> 1. WebView 实际上继承AbsoluteLayout,但是完全不支持ViewGroup的各种操作；无法查找里面的控件，也不能addView()
> 2. WebView内部的内容不是控件，而是屏幕绘制出来的内容，底层是一个浏览器引擎：webKit画出来的
> 3. webKit:一套开源的，支持多平台的浏览器引擎，几乎所有的android手机官方，默认浏览器，都使用了webkit ，能够将网页用图像的形式绘制出来，同时支持JavaScript和Html5的各种规范。

## 1. WebView 加载方式

### 1.1 webView.loadUrl(String url)

```java
// 加载网络链接
webView.loadUrl("https://www.baidu.com/")
// 加载本地资源(test.html放在assets文件夹下)
webView.loadUrl("file:///android_asset/test.html")
```

### 1.2 webView.loadData(String data, String mimeType, String encoding)

用来加载html代码段，**可能会出现页面编码问题，设置编码格式`text/html;charset=UTF-8`**

```java
String summary = "<html><body>You scored <b>192</b> points.</body></html>";
 webview.loadData(summary, "text/html", null);
 // 标签中包含中文会出现编码问题，设置编码格式
  webView.loadData(summary, "text/html;charset=UTF-8", null);
```

### 1.3 webView.loadDataWithBaseURL(String baseUrl, String data,String mimeType, String encoding, String historyUrl)

和`loadData()`方法相似都是加载html代码段，但是多了`baseUrl,和historyUrl`，**因为html中`css`和图片等资源大都使用相对路径，定义了`baseUrl`可以成功加载这些资源。**

```java
   webView.loadDataWithBaseURL(testUrl, IOUtil.readFile(path), "html/text", null, testUrl);
```

   


## 2.WebSettings

```java
 WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);//设置webview支持js脚本
        webSettings.setLoadsImagesAutomatically(true);//支持自动加载图片

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
```




## 3. Java和JS的交互

> 设置WebView支持JS功能 `settings.setJavaScriptEnabled(true);`
在Html中定义需要调用的方法

```javascript
script type="text/javascript">
<!-- JS 调用 Java-->
    function showToastMessage() {
        window.android.showToastMessage(text)
    }
    <!--Java 调用 JS-->
    function alertMessage(message){
        alert(message)
    }
var text="js 调用 Java";
</script>
```
### 3.1 Java调用JS代码


#### 3.1.1. 通过WebView的loadUrl()方式

```java
 // 必须另开线程进行JS方法调用(否则无法调用)
                mWebView.post(new Runnable() {
                    @Override
                    public void run() {

                        // 注意调用的JS方法名要对应上
                        // 调用javascript的callJS()方法
                          webView.loadUrl("javascript:alertMessage(\"Java 调用 JS \")");
                    }
                });

```

#### 3.1.2. 通过WebView的evaluateJavascript()方式

> 1. 该方法的执行不会使页面刷新，而第一种方法（loadUrl ）的执行则会
> 2. Android 4.4 后才可使用
```
mWebView.evaluateJavascript（"javascript:callJS(\"Java 调用 JS \")", new ValueCallback<String>() {
        @Override
        public void onReceiveValue(String value) {
            //此处为 js 返回的结果
        }
    });
```

### 3.2 JS调用Java代码

定义JS和Java的接口类

```java
//js和java的接口定义
    public class JsInteration {
        @JavascriptInterface
        public void showToastMessage(final String message) {
            jsHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
```
添加JS交互接口

```java
 webView.addJavascriptInterface(new JsInteration(), "android");
```
JS的调用方法

```javascript
 function showToastMessage() {
        window.android.showToastMessage(text)
    }
```


## 4. 常见问题

### 4.1 清除历史记录

> `webview.clearHistory`的使用方法

```
webView.setWebViewClient(new WebViewClient() {
    @Override
    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        super.doUpdateVisitedHistory(view, url, isReload);
        if (needClearHistory) {
            needClearHistory = false;
            view.clearHistory();//清除历史记录
        }
    }
});                       
```
### WebView的资源回收

```
if (webView != null) {
    webView.stopLoading();
    webView.setWebViewListener(null);
    webView.clearHistory();
    webView.clearCache(true);
    webView.loadUrl("about:blank");
    webView.pauseTimers();
    webView = null;
}
```

