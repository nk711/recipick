package com.example.softeng.recipeasy;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";

    /** This is the regex for email format taken from this website: https://www.mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/*/
    private static final String EMAIL = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PASSWORD = "[a-zA-Z0-9]{6,}";

    private ImageButton back;

    private Button btnSignUp;
    private EditText txtEmail;
    private EditText txtConfirmEmail;
    private EditText txtPassword;
    private EditText txtConfirmPass;
    private TextView txtEmessage;
    private TextView txtSignIn;

    private TextView txtDisplayName;

    private String email;
    private String confirmEmail;
    private String password;
    private String confirmPass;
    private String displayName;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        back = (ImageButton) findViewById(R.id.backToMain);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        /** initilises the components */
        btnSignUp =  findViewById(R.id.btnRegister);
        txtEmail =  findViewById(R.id.txtEmail);
        txtConfirmEmail =  findViewById(R.id.txtConfirmEmail);
        txtPassword = findViewById(R.id.txtPassword);
        txtConfirmPass = findViewById(R.id.txtConfirmPassword);
        txtEmessage = findViewById(R.id.txtEmessage);
        txtDisplayName = findViewById(R.id.txtDisplayName);
        txtSignIn = findViewById(R.id.txtSignIn);

        /**
         * when the user presses the btnSignUp button
         * check the validation
         *
         * if the validation fails
         * increment attempt
         * or if registration fails
         * increment attempt
         */
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = txtEmail.getText().toString().trim();
                confirmEmail = txtConfirmEmail.getText().toString().trim();
                password = txtPassword.getText().toString().trim();
                confirmPass = txtConfirmPass.getText().toString().trim();
                displayName = txtDisplayName.getText().toString().trim();

                register();

            }
        });


        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }


    public boolean validation() {
        boolean result = true;
        if (displayName.isEmpty()) {
            result = false;
        }
        if (email.isEmpty()|| (!email.matches(EMAIL)) ||
                confirmEmail.isEmpty() ||  (!confirmEmail.matches(EMAIL))) {
            result = false;
        } else if (!email.equals(confirmEmail)) {
            result = false;
        }
        if (password.isEmpty() || confirmPass.isEmpty()) {
            result = false;
        } else if (!password.matches(PASSWORD) || !confirmPass.matches(PASSWORD)) {
            result = false;
        } else if (!password.equals(confirmPass)) {
            result = false;
        }
        return result;
    }

    /**
     * Registers a user into the system
     */
    public void register() {
        if (validation()) {
            final ProgressDialog progressDialog = ProgressDialog.show(SignUpActivity.this, "Please wait", "Registering New User!", true);
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Toast.makeText(SignUpActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                    if (task.isSuccessful()) {
                        /** if the registration was successful, then switch to the login fragment */
                        progressDialog.dismiss();
                        onBackPressed();
                        Toast.makeText(SignUpActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                        /** else if the registration was unsuccessful, then increment the attempt */
                    } else {
                        progressDialog.dismiss();
                        txtEmessage.setText("Registration was unsuccessful, please try again!");
                    }
                }
            });
        } else {
            txtEmessage.setText("Validation error");
        }
    }

}
