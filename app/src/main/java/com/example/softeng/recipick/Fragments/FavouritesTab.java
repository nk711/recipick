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

import com.example.softeng.recipick.Adapters.FirestoreRecipeAdapter;
import com.example.softeng.recipick.Adapters.RecipeAdapter;
import com.example.softeng.recipick.Models.Recipe;
import com.example.softeng.recipick.Models.Utility;
import com.example.softeng.recipick.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * List of users favourite recipes
 */
public class FavouritesTab extends Fragment {
    private CollectionReference recipeRef;
    private DocumentReference userRef;
    private RecipeAdapter recipeAdapter;

    private String uid;
    private static final String USERS = "Users";
    private static final String INGREDIENTSQUERY = "ingredientsQuery";
    private static final String INGREDIENTS = "ingredients";
    private static final String RECIPES = "Recipes";

    public FavouritesTab() {
        // Required empty public constructor
    }

    RecyclerView recyclerView;
    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.favouritestab_layout, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.listOfFavourites);
        uid = Utility.getUid();
        recipeRef = FirebaseFirestore.getInstance().collection(RECIPES);
        userRef = FirebaseFirestore.getInstance().collection(USERS).document(uid);

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
