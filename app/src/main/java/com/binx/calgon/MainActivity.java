package com.binx.calgon;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    WebView webView;

    final Handler scriptCaller = new Handler();
    final int scriptDelay = 100;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String script = "for (const el of document.querySelectorAll('*')){\n" +
                "  if (el.textContent.toLowerCase().includes('kcal'.toLowerCase()) && el.childElementCount == 0){\n" +
                "    el.remove();\n" +
                "  }\n" +
                "}\n";

        webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void doUpdateVisitedHistory (WebView view, String url, boolean isReload){
                super.doUpdateVisitedHistory(view, url, isReload);
                view.setVisibility(View.INVISIBLE);
                (new Handler()).postDelayed(new Runnable(){
                    public void run(){
                        view.setVisibility(View.VISIBLE);
                    }
                }, 1250);
            }
        });

        webView.loadUrl("https://www.ubereats.com/");

        scriptCaller.postDelayed(new Runnable(){
            public void run(){
                webView.loadUrl("javascript:(function(){" + script + "})()");
                scriptCaller.postDelayed(this, scriptDelay);
            }
        }, scriptDelay);

    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}