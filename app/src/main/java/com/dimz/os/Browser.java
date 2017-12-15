package com.dimz.os;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Browser extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private View view; // sip udah ditambah
    private WebView webv;
    private EditText txturl;
    private Button btncari;
    private ProgressBar pg;
    private SwipeRefreshLayout refreshLayout;

    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> uploadMessage;
    public static final int REQUEST_SELECT_FILE = 100;
    private final static int FILECHOOSER_RESULTCODE = 1;


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


        webv.setWebChromeClient(new WebChromeClient() {
            // For 3.0+ Devices (Start)
            // onActivityResult attached before constructor
            protected void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
            }


            // For Lollipop 5.0+ Devices
            @Override
            public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                    uploadMessage = null;
                }

                uploadMessage = filePathCallback;

                Intent intent = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    intent = fileChooserParams.createIntent();
                }
                try {
                    startActivityForResult(intent, REQUEST_SELECT_FILE);
                } catch (ActivityNotFoundException e) {
                    uploadMessage = null;
                    Toast.makeText(Browser.this, "Cannot Open File Chooser", Toast.LENGTH_LONG).show();
                    return false;
                }
                return true;
            }

            //For Android 4.1 only
            protected void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "File Browser"), FILECHOOSER_RESULTCODE);
            }

            protected void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                pg.setVisibility(view.VISIBLE);
                pg.setProgress(newProgress);
                if (newProgress == 100) {
                    pg.setVisibility(view.GONE);
                }
            }
        });
        webv.getSettings().setJavaScriptEnabled(true);
        webv.getSettings().setDisplayZoomControls(true);
        webv.getSettings().setLoadWithOverviewMode(true);
        webv.getSettings().setUseWideViewPort(true);
        webv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webv.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webv.getSettings().setAllowFileAccessFromFileURLs(true);
        webv.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webv.getSettings().setAllowFileAccess(true);
        webv.getSettings().setSupportZoom(true);
        webv.getSettings().setAllowContentAccess(true);

        pg = (ProgressBar) findViewById(R.id.progressBar4);


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

                // Kodingan ini ga perlu karena udah ada di line 113, ini yang nyebabin dia ga mau keluar file-choosenya
//                webv.setWebChromeClient(new WebChromeClient() {
//                    @Override
//                    public void onProgressChanged(WebView view, int newProgress) {
//                        pg.setVisibility(View.VISIBLE);
//                        pg.setProgress(newProgress);
//                        if (newProgress == 100) {
//                            pg.setVisibility(View.GONE);
//                        }
//                    }
//                });

                webv.loadUrl(url);
                webv.setWebViewClient(new MyWebLaunch());
            }
        });


        webv.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                DownloadManager.Request request = new DownloadManager.Request(
                        Uri.parse(url));

                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Name of your downloadable file goes here");
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
                Toast.makeText(getApplicationContext(), "Downloading Files",
                        Toast.LENGTH_LONG).show();

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
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
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
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webv.canGoBack()) {
                        webv.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode == REQUEST_SELECT_FILE) {
                if (uploadMessage == null)
                    return;
                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                uploadMessage = null;
            }
        } else if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            // Use MainActivity.RESULT_OK if you're implementing WebView inside Fragment
            // Use RESULT_OK only if you're implementing WebView inside an Activity
            Uri result = intent == null || resultCode != MainActivity.RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        } else
            Toast.makeText(this.getApplicationContext(), "Failed to Upload Image", Toast.LENGTH_LONG).show();
    }
}
