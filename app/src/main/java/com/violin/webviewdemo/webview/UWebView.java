package com.violin.webviewdemo.webview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.violin.webviewdemo.R;

/**
 * Created by whl on 2016/11/3.
 */

public class UWebView extends FrameLayout {
    private WebViewClient client = new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            super.onPageFinished(view, url);
            progressView.setVisibility(View.GONE);
        }


        /**
         * 防止加载网页时调取系统的浏览器
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String s) {
//            Uri uri=Uri.parse(s);
//            Intent intent=new Intent(Intent.ACTION_VIEW,uri);
//            getContext().startActivity(intent);
            Log.d("whl", s);

            webView.loadUrl(s);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
//加载错误页
            view.loadUrl("file:///android_asset/error/error.html");
        }
    };
    private WebChromeClient chromeClient = new WebChromeClient() {
//        TODO 设置WebChromeClient


        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            progressTV.setText(newProgress + "%");
        }

    };

    public UWebView(Context context) {
        this(context, null);
    }

    public WebView getWebView() {
        return webView;
    }

    public UWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.webview_layout, this);
        initView();
    }

    private WebView webView;
    //    private ProgressBar progressBar;
    private FrameLayout progressView;
    private TextView progressTV;

    private void initView() {
        webView = (WebView) findViewById(R.id.web_view);
        initWebSetting(webView);
//        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressView = (FrameLayout) findViewById(R.id.progress_view);
        progressTV = (TextView) findViewById(R.id.progress_text);

    }

    /**
     * WebSettings的设置
     */
    private void initWebSetting(WebView webView) {
        webView.setWebViewClient(client);
        webView.setWebChromeClient(chromeClient);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);//设置webview支持js脚本
        webSettings.setLoadsImagesAutomatically(true);//支持自动加载图片
        webSettings.setUseWideViewPort(true);//设置界面自适应屏幕
        webSettings.setLoadWithOverviewMode(true);//设置使用屏幕
        webSettings.setSupportZoom(true);//设置支持缩放
    }
}
