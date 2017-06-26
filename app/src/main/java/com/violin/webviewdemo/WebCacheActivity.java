package com.violin.webviewdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.violin.webviewdemo.util.IOUtil;
import com.violin.webviewdemo.webview.UWebView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.R.attr.path;

public class WebCacheActivity extends AppCompatActivity {
    private WebView webView;
    private String path;
    private UWebView uWebView;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            webView.loadDataWithBaseURL(testUrl, IOUtil.readFile(path), "html/text", "utf-8", testUrl);

        }
    };
    private String testUrl = "http://www.jianshu.com/u/bed9b01686ab";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_cache);
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/history";
        initView();
        requestWebData();
    }

    private void initView() {
        uWebView = (UWebView) findViewById(R.id.webview);
        webView = uWebView.getWebView();
    }

    private void requestWebData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(testUrl);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    if (200 == urlConnection.getResponseCode()) {
                        InputStream inputStream = urlConnection.getInputStream();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte b[] = new byte[1024];
                        int len = 0;
                        while (-1 != (len = inputStream.read(b))) {
                            baos.write(b, 0, len);
                            baos.flush();
                        }
                        IOUtil.writeFile(path, baos.toString("utf-8"));
                        inputStream.close();
                        baos.close();
                        handler.sendEmptyMessage(1);


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }


    public static void launch(Context content) {
        Intent intent = new Intent(content, WebCacheActivity.class);
        content.startActivity(intent);
    }

}
