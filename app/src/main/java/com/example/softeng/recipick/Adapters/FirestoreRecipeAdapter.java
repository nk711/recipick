package com.example.softeng.recipick.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.softeng.recipick.Models.Recipe;
import com.example.softeng.recipick.R;
import com.example.softeng.recipick.ViewHolders.RecipeViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


public class FirestoreRecipeAdapter extends FirestoreRecyclerAdapter<Recipe, RecipeViewHolder> {

    private Context context;

    public FirestoreRecipeAdapter(Context context, @NonNull FirestoreRecyclerOptions<Recipe> options) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull RecipeViewHolder holder, int position, @NonNull Recipe recipe) {
        holder.textViewName.setText(recipe.getName());
        holder.textViewDesc.setText(recipe.getDescription());
        Glide.with(this.context)
                .load(recipe.getImages().get(0))
                .centerCrop()
                .placeholder(R.drawable.ic_applogoo)
                .error(R.drawable.ic_applogo)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_recycler_view_layout, viewGroup,false);
        return new RecipeViewHolder(view);
    }
}
