package com.example.softeng.recipeasy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import es.dmoral.toasty.Toasty;


public class LoginActivity extends AppCompatActivity {
    /** Tags the login activity class */
    private static final String TAG = "LoginActivity";

    /** Components used in the login activity */
    private Button btnLogin;
    private Button btnRegister;

    /** This is the regex for email format taken from this website: https://www.mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/*/
    private static final String EMAIL = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    /**Regex for password */
    private static final String PASSWORD = "[a-zA-Z0-9]{6,}";

    /** setting up input fields + output fields */
    private EditText txtEmail;
    private EditText txtPassword;
    private TextView txtChangePass;
    private TextView txtRegister;

    /** email and password fields */
    private String email;
    private String password;

    /** Gets the currently logged in User */
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /** Gets the currently logged in user */
        mAuth = FirebaseAuth.getInstance();
        /** If the user is already logged in then proceed to the main activity */
        if (mAuth.getCurrentUser()!=null) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        txtChangePass = findViewById(R.id.txtChangePass);
        txtRegister = findViewById(R.id.txtRegister);


        /**
         * When the user selects the text view, it will open the sign_up activity
         */
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegister();
            }
        });

        /** When the user selects the text view, it will open the forgot password activity */
        txtChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the Forgot activity
                Intent intent = new Intent(LoginActivity.this, ForgotActivity.class);;
                startActivity(intent);
            }
        });


        /** Setting up components + validating fields ... */
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               login();
            }
        });

        /**
         * When the user hits the register button, it will open the sign_up activity
         */
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the Signup activity
                openRegister();
            }
        });


        /** VALIDATES THE FIELD AS THE USER TYPES ON THE TEXT FIELD
         *  ABIT BUGGY
         */
        /**  validates the email field
        txtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validate();
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validate();
            }
            @Override
            public void afterTextChanged(Editable editable) {
                validate();
            }
            public void validate() {
                email = txtEmail.getText().toString().trim();
                if (email.isEmpty()) {
                    txtEmail.setError("Not a valid email address!");
                } else if (!email.matches(EMAIL)) {
                    txtEmail.setError("Not a valid email address!");
                }
            }
        });

        /**  validates the password field whilst the user types on the field
        txtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validate();
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validate();
            }
            @Override
            public void afterTextChanged(Editable editable) {
                validate();
            }
            public void validate() {
                password = txtPassword.getText().toString().trim();
                if (password.isEmpty()) {
                    txtPassword.setError("Password must be greater than 6 characters!");
                } else if (!password.matches(PASSWORD)) {
                    txtPassword.setError("Password must be greater than 6 characters!");
                }
            }
        }); */
    }

    /** Starts up the register activity */
    public void openRegister() {
        // Start the Signup activity
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);;
        startActivity(intent);
    }
    /**
     * Checks the validation of the fields
     * @return true if the validation passed
     *          false if the validation failed
     */
    public boolean validation() {
        email = txtEmail.getText().toString().trim();
        password = txtPassword.getText().toString().trim();
        boolean validation = true;
        if (email.isEmpty() || !email.matches(EMAIL)) {
            validation = false;
        }
        if (password.isEmpty() || !password.matches(PASSWORD)) {
            validation = false;
        }
        return validation;
    }

    /**
     * This will try log the user into the system
     */
    public void login() {
        email = txtEmail.getText().toString().trim();
        password = txtPassword.getText().toString().trim();

        if (validation()) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        /** if the login was successfull then go through to the next activity */
                        Toasty.success(LoginActivity.this, "Successfully Logged in", Toast.LENGTH_SHORT, true).show();
                        Intent intent = new Intent(LoginActivity.this, AddRecipeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toasty.error(LoginActivity.this, "Invalid details! Check the entered details and try again", Toast.LENGTH_LONG, true).show();
                    }
                }
            });
        } else {

            Toasty.error(LoginActivity.this, "Invalid details! Check the entered details and try again", Toast.LENGTH_LONG, true).show();
        }

    }




}
