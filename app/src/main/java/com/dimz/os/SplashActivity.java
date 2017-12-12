package com.dimz.os;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by DIMZ on 02/12/2017.
 */

public class SplashActivity extends Activity {

    ImageView loading;

    @Override
    public void onCreate(Bundle splashactivity){
        super.onCreate(splashactivity);
        setContentView(R.layout.splash_activity);

        new Timer().schedule(new TimerTask(){

            @Override
            public void run() {

                Intent x = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(x);
                finish();

            }
        },3000);

        loading = (ImageView) findViewById(R.id.loading);
        loading.setBackgroundResource(R.drawable.loading);
        AnimationDrawable animation = (AnimationDrawable) loading.getBackground();
        animation.start();
    }
}
