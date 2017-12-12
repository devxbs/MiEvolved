package com.dimz.os;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {
    TextView tv_name, tv_mobile, tv_email, tv_age;
    NetworkImageView networkImageView;
    ImageLoader imageLoader;
    RequestQueue requestQueue;//To Download the use profile picture
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Profile");
        //Initializing Widgets
        tv_name = (TextView) findViewById(R.id.textView9);
        tv_mobile = (TextView) findViewById(R.id.textView11);
        tv_email = (TextView) findViewById(R.id.textView13);
        tv_age = (TextView) findViewById(R.id.textView15);
        networkImageView = (NetworkImageView) findViewById(R.id.imageView);
        requestQueue = Volley.newRequestQueue(MainActivity.this);
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
        //Displaying the values passed from the LoginActivity
        tv_name.setText(getIntent().getStringExtra("name"));
        tv_email.setText(getIntent().getStringExtra("email"));
        tv_mobile.setText(getIntent().getStringExtra("mobile"));
        tv_age.setText(getIntent().getStringExtra("age"));
        networkImageView.setImageUrl(getIntent().getStringExtra("url"), imageLoader);
    }
    public void buttondimz1 (View v){
        startActivity(new Intent(this, Browser.class));
    }
      public void buttondimz2 (View v){
        startActivity(new Intent(this, main_activity2.class));
  }

  @Override
    public void onBackPressed(){
      AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
      builder.setMessage("Do You Want To Log Out ?");
      builder.setCancelable(true);
      builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
          @Override
          public void onClick(DialogInterface dialog, int id){
              finish();
          }

      });
      builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
          @Override
          public void onClick(DialogInterface dialog, int id){
              dialog.cancel();
          }
      });
      AlertDialog alert = builder.create();
      alert.show();
  }
}