package com.example.softeng.recipick.Models;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import es.dmoral.toasty.Toasty;

public class Utility {

    public static final String USERS = "Users";
    public static final String DISPLAYNAME = "display_name";
    public static final String EMAIL = "email";
    public static final String INGREDIENTS = "ingredients";
    public static final String FAVOURITES = "favourites";
    public static final String TROLLEY = "trolley";
    public static final String RECIPE = "RECIPE";

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
                                StringBuilder favourites = new StringBuilder();
                                if (user.getFavourites()!=null) {
                                    for (String recipe : user.getFavourites()) {
                                        favourites.append(recipe).append(",");
                                    }
                                }
                                StringBuilder trolleyList = new StringBuilder();
                                if (user.getTrolley()!=null) {
                                    for (String items : user.getTrolley().keySet()) {
                                        trolleyList.append(items).append(",");
                                    }
                                }
                                prefs.edit().putString(DISPLAYNAME, user.getDisplay_name()).apply();
                                prefs.edit().putString(EMAIL, user.getEmail()).apply();
                                prefs.edit().putString(INGREDIENTS, list.toString()).apply();
                                prefs.edit().putString(FAVOURITES, favourites.toString()).apply();
                                prefs.edit().putString(TROLLEY, trolleyList.toString()).apply();
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


    public static boolean checkFavouriteRecipe(final Context context, String recipe_uid) {
        SharedPreferences prefs = context.getSharedPreferences("com.example.softeng.recipick", Context.MODE_PRIVATE);
        String list = prefs.getString(FAVOURITES, "");
        List<String> listOfFavourites = null;
        boolean exists = false;
        if (list != null) {
            listOfFavourites =  Arrays.asList(list.split(","));
            exists = listOfFavourites.contains(recipe_uid);
        }
        return exists;
    }
    public static void updateUserTrolley(final Context context, List<String> trolley) {
        SharedPreferences prefs = context.getSharedPreferences("com.example.softeng.recipick", Context.MODE_PRIVATE);
        StringBuilder list = new StringBuilder();
        if (trolley!=null) {
            for (String ingredient : trolley) {
                list.append(ingredient).append(",");
            }
        }
        prefs.edit().putString(TROLLEY, list.toString()).apply();
    }

}
