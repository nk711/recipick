package com.example.softeng.recipick.Models;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import es.dmoral.toasty.Toasty;

public class Utility {

    private static final String USERS = "Users";

    private static final String DISPLAYNAME = "display_name";
    private static final String EMAIL = "email";
    private static final String INGREDIENTS = "ingredients";

    private static User user;



    private DocumentReference userRef;

    public static boolean loggedIn() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser()==null) {
            return false;
        } else{
            return true;
        }
    }

    public static String getUid() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser()==null) {
            return null;
        }
        return mAuth.getCurrentUser().getUid();
    }

    public static void saveUserDetails(final Context context) {
        if (!Utility.loggedIn()) {
            return;
        }

        DocumentReference userRef = FirebaseFirestore.getInstance().collection(USERS).document(getUid());
       // CountDownLatch done = new CountDownLatch(1);
        userRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                User user = document.toObject(User.class);
                                /** Updates shared preferences */
                                Utility.user = user;
                                SharedPreferences prefs = context.getSharedPreferences("com.example.softeng.recipick", Context.MODE_PRIVATE);
                                StringBuilder list = new StringBuilder();
                                if (user.getIngredients()!=null) {
                                    for (String ingredient : user.getIngredients().keySet()) {
                                        list.append(ingredient).append(",");
                                    }
                                }
                                prefs.edit().putString(DISPLAYNAME, user.getDisplay_name()).apply();
                                prefs.edit().putString(EMAIL, user.getEmail()).apply();
                                prefs.edit().putString(INGREDIENTS, list.toString()).apply();
                            }
                        }
                    }
                });
    }

    public static void updateUserIngredients(final Context context, List<String> ingredients) {
        SharedPreferences prefs = context.getSharedPreferences("com.example.softeng.recipick", Context.MODE_PRIVATE);
        StringBuilder list = new StringBuilder();
        if (ingredients!=null) {
            for (String ingredient : ingredients) {
                list.append(ingredient).append(",");
            }
        }
        prefs.edit().putString(INGREDIENTS, list.toString()).apply();
    }


    public static String[] retrieveUserIngredients(final Context context) {
        SharedPreferences prefs = context.getSharedPreferences("com.example.softeng.recipick", Context.MODE_PRIVATE);
        String list = prefs.getString(INGREDIENTS, "");
        String[] listOfIngredients = null;
        if (list != null) {
            listOfIngredients =  list.split(",");
        }
        return listOfIngredients;
    }
}
