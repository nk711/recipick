package com.example.softeng.recipick.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.MonthDisplayHelper;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.softeng.recipick.Models.User;
import com.example.softeng.recipick.Models.Utility;
import com.example.softeng.recipick.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import es.dmoral.toasty.Toasty;

public class SplashActivity extends AppCompatActivity {


    private DocumentReference userRef;

    private static final String USERS = "Users";



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


        Utility.saveUserDetails(this);

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

    public void loadUsersIngredients() {

    }


}
