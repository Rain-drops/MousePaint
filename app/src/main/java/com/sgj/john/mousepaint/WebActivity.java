package com.sgj.john.mousepaint;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sgj.john.mousepaint.utils.StatusBarCompat;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WebActivity extends BaseActivity {

    private static final String TAG = "WebActivity";

    public static final String EXTRA_URL = "extra_url";
    public static final String EXTRA_TITLE = "title";

    @Bind(R.id.webView)WebView mWebView;

    private Context mContext;
    String mUrl, mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        mContext = this;

        StatusBarCompat.compat(this, getResources().getColor(R.color.black_tran));

        ButterKnife.bind(this);

        mUrl = getIntent().getStringExtra(EXTRA_URL);
        mTitle = getIntent().getStringExtra(EXTRA_TITLE);

        Log.i(TAG, "url---->" + mUrl + "----title---->" + mTitle);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);//告诉WebView启用javascript执行。
        settings.setLoadWithOverviewMode(true);//Returns true if this WebView loads page with overview mode.
        settings.setAppCacheEnabled(true);//告诉WebView启用应用程序缓存API。
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//设置基本布局算法。
        settings.setSupportZoom(true);//设置是否WebView支持缩放。

        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new MyWebClient());

        mWebView.loadUrl(mUrl);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mWebView !=null){
            mWebView.destroy();
        }
        ButterKnife.unbind(this);
    }

    @Override
    protected void onPause() {
        if(mWebView != null){
            mWebView.onPause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mWebView != null) mWebView.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getAction() == KeyEvent.ACTION_DOWN){
            switch (keyCode){
                case KeyEvent.KEYCODE_BACK:
                    if(mWebView.canGoBack()){
                        mWebView.goBack();
                    }else {
                        finish();
                    }
                    return true;
            }
        }


        return super.onKeyDown(keyCode, event);
    }

    /**
     *
     */
    private class ChromeClient extends WebChromeClient{
        public ChromeClient() {
            super();
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            setTitle(title);
        }
    }

    /**
     * WebViewClient主要帮助WebView处理各种通知、请求事件的
     */
    private class MyWebClient extends WebViewClient{

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(url != null){
                view.loadUrl(url);
            }
            return true;
        }
    }


}
