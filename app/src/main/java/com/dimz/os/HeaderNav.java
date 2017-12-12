package com.dimz.os;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

/**
 * Created by DIMZ on 12/12/2017.
 */

public class HeaderNav extends Activity {

    TextView tv_name;
    NetworkImageView networkImageView;
    ImageLoader imageLoader;
    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_header);

        tv_name = (TextView) findViewById(R.id.textView9);

        networkImageView = (NetworkImageView) findViewById(R.id.imageView);
        requestQueue = Volley.newRequestQueue(this);
        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            int cacheSize = 4 * 1024 * 1024; // 4 MegaBytes
            LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(cacheSize);
            @Override
            public Bitmap getBitmap(String url) {
                return lruCache.get(url);
            }
            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                lruCache.put(url, bitmap);
            }
        });

        tv_name.setText(getIntent().getStringExtra("name"));

        networkImageView.setImageUrl(getIntent().getStringExtra("url"), imageLoader);
    }
}
