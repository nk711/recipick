/**
 *  MyRecipeActivity.java
 */
package com.example.softeng.recipick.Activities;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.softeng.recipick.Adapters.FirestoreRecipeAdapter;

import com.example.softeng.recipick.Models.Recipe;
import com.example.softeng.recipick.Models.Utility;
import com.example.softeng.recipick.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * Activity will list the recipes created by a user
 */
public class MyRecipeActivity extends AppCompatActivity {
    /** reference to the recipe collection */
    private CollectionReference recipeRef;
    /** Used to populate the recycler view*/
    private FirestoreRecyclerAdapter recipeAdapter;
    /** The uid of the currently logged in user*/
    private String uid;
    /** holds the list of recipes */
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipe);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //holds the list of recipes
        recyclerView = findViewById(R.id.recyclerView);
        //Gets the uid of the currently logged in user
        uid = Utility.getUid();
        //setting the document reference path
        recipeRef = FirebaseFirestore.getInstance().collection(Utility.RECIPES);
    }

    /**
     *
     *  This function will load the list of recipes the user has added
     *
     */
    public void setAdapter() {
        Query query = recipeRef.orderBy("name");
        query = query.whereEqualTo("author", uid);

        if (recipeAdapter!=null)
            recipeAdapter.stopListening();

        //Required to load the list of recipes
        FirestoreRecyclerOptions<Recipe> options = new FirestoreRecyclerOptions.Builder<Recipe>()
                .setQuery(query, Recipe.class)
                .build();

        recipeAdapter = new FirestoreRecipeAdapter(this, options);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MyRecipeActivity.this));
        recyclerView.setAdapter(recipeAdapter);
    }


    @Override
    public void onStart() {
        super.onStart();
        setAdapter();
        if (recipeAdapter!=null)
            recipeAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (recipeAdapter!=null)
            recipeAdapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        setAdapter();
        if (recipeAdapter!=null)
            recipeAdapter.startListening();
    }

}
