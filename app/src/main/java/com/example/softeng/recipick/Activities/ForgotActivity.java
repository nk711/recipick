/**
 * ForgotActivity.java
 */
package com.example.softeng.recipick.Activities;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.softeng.recipick.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import es.dmoral.toasty.Toasty;

/**
 * Allows users to send an email to reset their password
 */
public class ForgotActivity extends AppCompatActivity {
    /** Tags the Forgot activity class */
    private static final String TAG = "ForgotActivity";

    /** Components used in the forgot activity */
    private ImageButton back;
    private Button btnForgotPass;
    private EditText txtEmail;
    private FirebaseAuth mAuth;


    /** This is the regex for email format taken from this website: https://www.mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/*/
    private static final String EMAIL = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    /** email field */
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        back = (ImageButton) findViewById(R.id.backToMain);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        /**Firebase Authentication=*/
        btnForgotPass = findViewById(R.id.btnForgotPass);
        txtEmail = findViewById(R.id.txtEmail);
        mAuth = FirebaseAuth.getInstance();

        /** Setting up components + validating fields ... */
        btnForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });

    }

    /**
     * Performs validation on the email field
     * @returns True if passed
     *          False if email is blank or does not match the REGEX
     */
    public boolean validation() {
        email = txtEmail.getText().toString().trim();
        boolean validation = true;
        if (email.isEmpty() || !email.matches(EMAIL)) {
            validation = false;
        }
        return validation;
    }


    /**
     *  This attempts to send a password change request to the specified email by the user
     */
    public void sendRequest() {
        if (validation()) {
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toasty.success(ForgotActivity.this, "Email successfully sent. Please check your email.", Toast.LENGTH_SHORT, true).show();
                                Log.d(TAG, "Email sent.");
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toasty.error(ForgotActivity.this, "Email address entered is not registered or there is no internet connection.", Toast.LENGTH_LONG, true).show();

                }
            });
        } else {
            Toasty.error(ForgotActivity.this, "Invalid email address.", Toast.LENGTH_LONG, true).show();

        }
    }

}
