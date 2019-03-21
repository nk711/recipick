package com.example.softeng.recipeasy;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotActivity extends AppCompatActivity {
    /** Tags the Forgot activity class */
    private static final String TAG = "ForgotActivity";

    /** Components used in the forgot activity */
    private ImageButton back;
    private Button btnForgotPass;
    private EditText txtEmail;
    private TextView txtEmessage;
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

        btnForgotPass = findViewById(R.id.btnForgotPass);
        txtEmail = findViewById(R.id.txtEmail);
        txtEmessage = findViewById(R.id.txtEmessage);

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
            Toast.makeText(getApplication(), "Enter the email address you signed in with", Toast.LENGTH_SHORT).show();
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
                                Log.d(TAG, "Email sent.");
                                txtEmessage.setText("Email has been sent! Check your email");
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Email failed to send, please try again later", Toast.LENGTH_SHORT).show();
                    txtEmessage.setText("Email failed to send, please try again later");
                }
            });
        }
    }

}
