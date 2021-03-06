package com.dimz.os;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

    // Simpan dulu ke variabel biar ga ribet.
    String name;
    String email;
    String mobile;
    String url;
    String age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Profile");

        // ambil data dari sharedpreference yang disimpen
        name = SharedPrefs.readSharedSetting(this, SharedPrefs.PREF_NAME, "");
        mobile = SharedPrefs.readSharedSetting(this, SharedPrefs.PREF_MOBILE, "");
        email = SharedPrefs.readSharedSetting(this, SharedPrefs.PREF_EMAIL, "");
        url = SharedPrefs.readSharedSetting(this, SharedPrefs.PREF_URL, "");
        age = SharedPrefs.readSharedSetting(this, SharedPrefs.PREF_AGE,"");
        // ambil data dari intent yang dikirim oleh LoginActivity line 56 - 63 (ralat)
//        Intent intent = getIntent();
//        name = intent.getStringExtra("name");
//        email = intent.getStringExtra("email");
//        mobile = intent.getStringExtra("mobile");
//        url = intent.getStringExtra("url");


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
        tv_name.setText(name);
        tv_email.setText(email);
        tv_age.setText(age);
        tv_mobile.setText(mobile);
        networkImageView.setImageUrl(url,imageLoader);
//        tv_name.setText(getIntent().getStringExtra("name"));
//        tv_email.setText(getIntent().getStringExtra("email"));
//        tv_mobile.setText(getIntent().getStringExtra("mobile"));
//        tv_age.setText(getIntent().getStringExtra("age"));
//        networkImageView.setImageUrl(getIntent().getStringExtra("url"), imageLoader);

        CekSession();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // cek sesinya juga ditambah disini. biar ngecek ketika setelah ada yang nutup activity
        CekSession();
    }

    public void CekSession() {
        // cek jika shared preference aktif
        if (!SharedPrefs.readSharedSetting(this, SharedPrefs.KEY_LOGIN_NAME)) {
            Intent introIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(introIntent);
            finish();
        }

//        Boolean Check = Boolean.valueOf(SharedPrefs.readSharedSetting(MainActivity.this, "DimzCode", "true"));
//
//        Intent introIntent = new Intent(MainActivity.this, LoginActivity.class);
//        introIntent.putExtra("DimzCode", Check);
//
//        //The Value if you click on Login Activity and Set the value is FALSE and whe false the activity will be visible
//        if (Check) {
//            startActivity(introIntent);
//            finish();
//        } //If no the Main Activity not Do Anything
//    }
    }

    public void buttondimz1(View v) {
        startActivity(new Intent(this, Browser.class));
    }

    public void buttondimz2(View v) {
        // karena mau passing "name" sama "email", ga bisa langsung
//        startActivity(new Intent(this, main_activity2.class));
        Intent intent = new Intent(this, main_activity2.class);
        intent.putExtra("name", name);
        intent.putExtra("url", url);
        startActivity(intent);
    }

//    @Override
//    public void onBackPressed() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//        builder.setMessage("Do You Want To Log Out ?");
//        builder.setCancelable(true);
//        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int id) {
//                finish();
//            }
////
//        });
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int id) {
//                dialog.cancel();
//            }
//        });
//        AlertDialog alert = builder.create();
//        alert.show();
//    }
}