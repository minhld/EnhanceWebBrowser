package com.minhld.enhancewebbrowser;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.goBtn)
    Button goBtn;

    @BindView(R.id.urlText)
    TextView urlText;

    @BindView(R.id.webview)
    WebView webView;

    @BindView(R.id.logText)
    TextView logText;

    long startTime = 0;
    final SimpleDateFormat SDF = new SimpleDateFormat("MM-dd HH:mm:ss.SSS");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        logText.setMovementMethod(new ScrollingMovementMethod());

        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadUrl();
            }
        });

        setupWebview();

        preload();
    }

    private void setupWebview() {
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (startTime == 0) {
                    startTime = System.currentTimeMillis();
                }
                writeLog("starts loading page: " + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                long durr = System.currentTimeMillis() - startTime;
                writeLog("[" + durr + "ms] completed loading page: " + url + "");
                startTime = 0;
            }
        });
    }

    private void preload() {
        urlText.setText("http://vietnamnet.vn");
    }

    private void loadUrl() {
        String url = urlText.getText().toString();
        webView.loadUrl(url);
    }

    private void writeLog(final String log) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                logText.append(SDF.format(new Date()) + ": " + log + "\r\n");
            }
        });
    }
}
