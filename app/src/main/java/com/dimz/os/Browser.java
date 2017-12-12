package com.dimz.os;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class Browser extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private View view; // sip udah ditambah
    private WebView webv;
    private EditText txturl;
    private Button btncari;
    private ProgressBar pg;
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        pg = (ProgressBar) findViewById(R.id.progressBar4);
        webv = (WebView) findViewById(R.id.webv);          // findViewById itu fungsinya view, jadi view diatas baru findView. cara aksesnya kaya gini kalau di fragment
        txturl = (EditText) findViewById(R.id.txturl);     // sama ky diatas
        btncari = (Button) findViewById(R.id.btncari);     // sama ky diatas
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        refreshLayout.setOnRefreshListener(this);

        String url = "https://google.co.id/";



        webv.getSettings().setJavaScriptEnabled(true);
        webv.getSettings().setDisplayZoomControls(true);
        webv.getSettings().setLoadWithOverviewMode(true);
        webv.getSettings().setUseWideViewPort(true);
        webv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webv.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webv.getSettings().setAllowFileAccessFromFileURLs(true);
        webv.getSettings().setAllowUniversalAccessFromFileURLs(true);

        pg = (ProgressBar) findViewById(R.id.progressBar4);
        webv.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress){
                pg.setVisibility(view.VISIBLE);
                pg.setProgress(newProgress);
                if (newProgress == 100){
                    pg.setVisibility(view.GONE);
                }
            }
        });

        webv.loadUrl(url);
        webv.setWebViewClient(new MyWebLaunch());

        btncari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = txturl.getText().toString();
                // aktifkan java script
                webv.getSettings().setJavaScriptEnabled(true);
                webv.getSettings().setDisplayZoomControls(true);
                pg = (ProgressBar) findViewById(R.id.progressBar4);
                webv.setWebChromeClient(new WebChromeClient(){
                    @Override
                    public void onProgressChanged(WebView view, int newProgress) {
                        pg.setVisibility(View.VISIBLE);
                        pg.setProgress(newProgress);
                        if (newProgress == 100){
                            pg.setVisibility(View.GONE);
                        }
                    }
                });
                webv.loadUrl(url);
                webv.setWebViewClient(new MyWebLaunch());
            }
        });
    }

    /**
     * Called when a swipe gesture triggers a refresh.
     */
    @Override
    public void onRefresh() {
        webv.reload();
        refreshLayout.setRefreshing(false);
    }

    private class MyWebLaunch extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url){
            view.loadUrl(url);
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN){
            switch (keyCode){
                case KeyEvent.KEYCODE_BACK :
                    if (webv.canGoBack()){
                        webv.goBack();
                    }else {
                        finish();
                    }
                    return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }
}
