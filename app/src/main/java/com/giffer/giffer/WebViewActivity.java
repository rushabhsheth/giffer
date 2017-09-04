package com.giffer.giffer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

import com.giffer.giffer.NewsCard.NewsCard;

/**
 * Created by archanaarunkumar on 8/20/17.
 */

public class WebViewActivity extends AppCompatActivity {

    private static final String TAG = "WebViewActivity";

    String mLinkURL;
    String mLinkText;

    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbarWebview);
        setSupportActionBar(myToolbar);

        Intent intent = getIntent();
        mLinkText = (String) intent.getSerializableExtra("LinkText");
        mLinkURL = (String) intent.getSerializableExtra("LinkURL");

        getSupportActionBar().setTitle(mLinkText);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(mLinkURL);

    }
}
