/**
 *  MyRecipeActivity.java
 */
package com.example.softeng.recipick.Activities;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.softeng.recipick.Adapters.FirestoreRecipeAdapter;

import com.example.softeng.recipick.Models.Recipe;
import com.example.softeng.recipick.Models.Utility;
import com.example.softeng.recipick.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

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
    /** gets the currently logged in user */
    private FirebaseAuth mAuth;
    /** The navigation drawer */
    private Drawer result;
    /** Toolbar of the activity*/
    private Toolbar toolbar;
    /** Sets up the navigation drawer */
    public void navbar_setup() {
        //if you want to update the items at a later time it is recommended to keep it in a variable
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("Home");
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withName("Shopping List");
        PrimaryDrawerItem item5 = new PrimaryDrawerItem().withIdentifier(5).withName("My Recipes");
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(3).withName("Add a recipe");
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(4).withName("Log out");

        AccountHeader accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withHeaderBackground(R.drawable.ic_applogo)
                .build();

        //create the drawer and remember the `Drawer` result object
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(accountHeader)
                .addDrawerItems(
                        item1,
                        item2,
                        item5,
                        item3,
                        new DividerDrawerItem(),
                        //   item5,
                        item4
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D

                        switch ((int) drawerItem.getIdentifier()) {
                            case 1:
                                result.closeDrawer();
                                Intent homePageIntent = new Intent(MyRecipeActivity.this, HomeActivity.class);
                                startActivity(homePageIntent);
                                break;
                            case 2:
                                result.closeDrawer();
                                Intent shoppingListIntent = new Intent(MyRecipeActivity.this, ShoppingList.class);
                                startActivity(shoppingListIntent);
                                break;
                            case 3:
                                result.closeDrawer();
                                Intent addRecipeIntent= new Intent(MyRecipeActivity.this, AddRecipeActivity.class);
                                startActivity(addRecipeIntent);
                                break;
                            case 4:
                                result.closeDrawer();
                                mAuth.signOut();
                                finish();
                                startActivity(new Intent(MyRecipeActivity.this, LoginActivity.class));
                                break;
                            case 5:
                                result.closeDrawer();
                                break;
                        }

                        return true;
                    }
                })
                .build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipe);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //holds the list of recipes
        recyclerView = findViewById(R.id.list_recipes);
        //Gets the uid of the currently logged in user
        uid = Utility.getUid();
        //setting the document reference path
        recipeRef = FirebaseFirestore.getInstance().collection(Utility.RECIPES);
        mAuth = FirebaseAuth.getInstance();
        navbar_setup();
    }

    /**
     *
     *  This function will load the list of recipes the user has added
     *
     */
    public void setAdapter() {
        Query query = recipeRef.orderBy("name");
        query = query.whereEqualTo("uid", uid);

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
