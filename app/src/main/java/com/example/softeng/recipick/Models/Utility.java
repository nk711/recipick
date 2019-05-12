/**
 *  Utility.java
 */
package com.example.softeng.recipick.Models;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Arrays;
import java.util.List;

/**
 * Commonly used methods throughout the app and user attributes regularly accessed
 */
public class Utility {

    /** constants */
    public static final String USERS = "Users";
    public static final String RECIPES = "Recipes";
    public static final String DISPLAYNAME = "display_name";
    public static final String EMAIL = "email";
    public static final String INGREDIENTS = "ingredients";
    public static final String FAVOURITES = "favourites";
    public static final String TROLLEY = "trolley";
    public static final String RECIPE = "RECIPE";
    public static final String ID = "ID";
    /** Currently logged in user */
    public static User user;
    /** reference to the currently logged in user's document*/
    private DocumentReference userRef;


    /**
     *
     * @param str
     * @return - string with each first letter of each word capitalised
     */
    public static String uppercase(String str) {
        //Split where there is a white space
        String[] split = str.split(" ");
        StringBuilder buffer = new StringBuilder();
        // Go through each word and capitalise the first letter
        for (String word: split) {
            buffer.append(word.substring(0,1).toUpperCase());
            buffer.append(word.substring(1));
            buffer.append(" ");
        }
        //return string
        return buffer.toString().trim();
    }


    /** Checks if a user logged in or not */
    public static boolean loggedIn() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser()==null) {
            return false;
        } else{
            return true;
        }
    }

    /** Gets the currently logged in user id */
    public static String getUid() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser()==null) {
            return null;
        }
        return mAuth.getCurrentUser().getUid();
    }

    /** Saves the users details to shared preferences */
    public static void saveUserDetails(final Context context) {
        if (!Utility.loggedIn()) {
            return;
        }
        DocumentReference userRef = FirebaseFirestore.getInstance().collection(USERS).document(getUid());
       /** Could turn this into a listener */
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
                                    for (String id : user.getFavourites().keySet()) {
                                        favourites.append(id).append(",");
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

    /**
     * @param context
     * @param ingredients
     *  Updates the user's ingredients locally in shared preference
     */
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


    /**
     * @param context
     * @return user's ingredients from shared preference
     */
    public static String[] retrieveUserIngredients(final Context context) {
        SharedPreferences prefs = context.getSharedPreferences("com.example.softeng.recipick", Context.MODE_PRIVATE);
        String list = prefs.getString(INGREDIENTS, "");
        String[] listOfIngredients = null;
        if (list != null) {
            listOfIngredients =  list.split(",");
        }
        return listOfIngredients;
    }

    /**
     * @param context
     * @return user's trolley from shared preference
     */
    public static String[] retrieveUserTrolley(final Context context) {
        SharedPreferences prefs = context.getSharedPreferences("com.example.softeng.recipick", Context.MODE_PRIVATE);
        String list = prefs.getString(TROLLEY, "");
        String[] listOfIngredients = null;
        if (list != null) {
            listOfIngredients =  list.split(",");
        }
        return listOfIngredients;
    }


    /**
     * @param context
     * @param recipe_uid
     * @return true if the recipe id passed in is a user favourite otherwise false
     */
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


    /**
     * @param context
     * @param trolley updates the trolley to shared preference
     */
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
