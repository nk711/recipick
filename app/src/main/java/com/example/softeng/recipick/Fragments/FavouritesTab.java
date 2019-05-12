/**
 * FavouritesTab.java
 */
package com.example.softeng.recipick.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.softeng.recipick.Adapters.RecipeAdapter;
import com.example.softeng.recipick.Models.Utility;
import com.example.softeng.recipick.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
/**
 * List of users favourite recipes
 */
public class FavouritesTab extends Fragment {
    /** path to the recipe collection*/
    private CollectionReference recipeRef;
    /** path to a user document in the user collection */
    private DocumentReference userRef;
    /** Recipe adapter holds the list of recipes that is favourited by the user */
    private RecipeAdapter recipeAdapter;
    /** currently logged in user's id */
    private String uid;


    public FavouritesTab() {
        // Required empty public constructor
    }

    RecyclerView recyclerView;
    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.favouritestab_layout, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.listOfFavourites);
        uid = Utility.getUid();
        recipeRef = FirebaseFirestore.getInstance().collection(Utility.RECIPES);
        userRef = FirebaseFirestore.getInstance().collection(Utility.USERS).document(uid);

        /** Checks if the user static field is null before displaying user's favourites */
        if (Utility.user != null) {
            recipeAdapter = new RecipeAdapter(requireContext(), Utility.user.getFavourites());
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
            recyclerView.setAdapter(recipeAdapter);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Utility.user != null) {
            recipeAdapter = new RecipeAdapter(requireContext(), Utility.user.getFavourites());
            recyclerView.setAdapter(recipeAdapter);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (Utility.user != null) {
            recipeAdapter = new RecipeAdapter(requireContext(), Utility.user.getFavourites());
            recyclerView.setAdapter(recipeAdapter);
        }
    }
}
