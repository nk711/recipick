package com.example.softeng.recipick.Activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.example.softeng.recipick.Models.User;
import com.example.softeng.recipick.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import es.dmoral.toasty.Toasty;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";

    /** This is the regex for email format taken from this website: https://www.mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/*/
    private static final String EMAIL = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PASSWORD = "[a-zA-Z0-9]{6,}";
    private static final String USERS = "Users";
    //private static final String IMAGES = "Images";
    //private static final String RECIPES = "Recipes";

    private ImageButton back;


    private Button btnSignUp;
    private EditText txtEmail;
    private EditText txtConfirmEmail;
    private EditText txtPassword;
    private EditText txtConfirmPass;
    private TextView txtSignIn;

    private TextView txtDisplayName;

    private String email;
    private String confirmEmail;
    private String password;
    private String confirmPass;
    private String displayName;

    private CollectionReference userRef;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }


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
        db = FirebaseFirestore.getInstance();
        userRef = db.collection(USERS);

        /** initilises the components */
        btnSignUp =  findViewById(R.id.btnRegister);
        txtEmail =  findViewById(R.id.txtEmail);
        txtConfirmEmail =  findViewById(R.id.txtConfirmEmail);
        txtPassword = findViewById(R.id.txtPassword);
        txtConfirmPass = findViewById(R.id.txtConfirmPassword);
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

    public FirebaseUser currentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
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
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        /** Gets the currently logged in user */
                        String uid = currentUser().getUid();

                        /** Creates an instance of the user */
                        User user = new User(
                                displayName,
                                email
                        );

                        /**
                         * Creates a node called users, within the node, it'll create a node with the user's unique ID [FIREBASE]
                         *

                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(uid)
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                /** if the registration was successful, then switch to the login fragment
                                if (task.isSuccessful()) {
                                    Toasty.success(SignUpActivity.this, "Registered Successfully", Toast.LENGTH_SHORT, true).show();
                                    onBackPressed();
                                } else {
                                    Toasty.error(SignUpActivity.this, "Failed to register user! Please try again later!", Toast.LENGTH_LONG, true).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toasty.error(SignUpActivity.this, "Failed to register user! Check your internet connection!", Toast.LENGTH_LONG, true).show();
                            }
                        });
                            */

                        /**
                         * Creates a node called users, within the node, it'll create a node with the user's unique ID [FIRESTORE METHOD]
                         *
                         */

                        userRef.document(uid).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                /** if the registration was successful, then switch to the login fragment */
                                if (task.isSuccessful()) {
                                    Toasty.success(SignUpActivity.this, "Registered Successfully", Toast.LENGTH_SHORT, true).show();
                                    onBackPressed();
                                } else {
                                    Toasty.error(SignUpActivity.this, "Failed to register user! Please try again later!", Toast.LENGTH_LONG, true).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toasty.error(SignUpActivity.this, "Failed to register user! Check your internet connection!", Toast.LENGTH_LONG, true).show();
                            }
                        });


                        /** else if the registration was unsuccessful, then increment the attempt */
                    } else {
                        Toasty.error(SignUpActivity.this, "Failed to register user! Prehaps an account with the username already exists, or check your internet connection!", Toast.LENGTH_LONG, true).show();
                    }
                }
            });
        } else {
            Toasty.error(SignUpActivity.this, "Invalid details! Check the entered details and try again", Toast.LENGTH_LONG, true).show();
        }
    }

}
