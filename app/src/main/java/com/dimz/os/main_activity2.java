package com.dimz.os;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.LruCache;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.dimz.os.drawer.Nav1;
import com.dimz.os.drawer.Nav2;

public class main_activity2 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;

    // siapkan variabel dulu
    String nama;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity2);

        // Ambil data yang dipassing dari MainActivity
        Intent intent = getIntent();
        nama = intent.getStringExtra("nama");
        url = intent.getStringExtra("url");

        mToolbar = (Toolbar) findViewById(R.id.nav_action);
        setSupportActionBar(mToolbar);

        Nav1 fragment = new Nav1();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, fragment);
        fragmentTransaction.commit();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // ini buat akses header diatas navigationview nya
        View navHeader = navigationView.getHeaderView(0);
        if (navHeader != null) {
            TextView textView9 = navHeader.findViewById(R.id.textView9);
            NetworkImageView networkImageView = navHeader.findViewById(R.id.imageView);
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            ImageLoader imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
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

            // masukin variabel yang udah dibuat di line 37-38 dan diisi di 47-48
            textView9.setText(nama);
            networkImageView.setImageUrl(url, imageLoader);

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_setting) {
            Nav1 fragment = new Nav1();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.framelayout, fragment);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_account) {
            Nav2 fragment = new Nav2();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.framelayout, fragment);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_logout) {
            finish();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }
}