package com.violin.webviewdemo;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.violin.webviewdemo.webview.UWebView;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private Handler jsHandler = new Handler();

    private UWebView uWebView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);
        initView();
        }



    private void initView() {
        uWebView = (UWebView) findViewById(R.id.webview);
        webView = uWebView.getWebView();
        webView.loadUrl("file:///android_asset/test.html");
        Button btn1= (Button) findViewById(R.id.button1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebCacheActivity.launch(MainActivity.this);
            }
        });


        Button btn2 = (Button) findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.post(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadUrl("javascript:alertMessage(\"Java 调用 JS \")");

                    }
                });
            }
        });
        webView.addJavascriptInterface(new JsInteration(), "android");

    }

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
}
