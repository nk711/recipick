package com.example.softeng.recipick.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.softeng.recipick.R;

public class SplashActivity extends AppCompatActivity {

    /** Sets a delay for the splash screen */
    private static int DELAY = 4000;
    /** Text view component */
    private ImageView app_logo;
    /** Animation component */
    private Animation fadeIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /** This hides the Android top status bar */
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        /** Initialising the text view */
        app_logo = (ImageView) findViewById(R.id.logo);


        /** Animation set on the text view*/
        fadeIn = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.fade_in);
        app_logo.startAnimation(fadeIn);

        /** After the the DELAY, start the main activity. */
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, DELAY);

    }
}
