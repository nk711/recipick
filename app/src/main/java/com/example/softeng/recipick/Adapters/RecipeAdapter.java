/**
 * RecipeAdapter.java
 */
package com.example.softeng.recipick.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.softeng.recipick.Activities.RecipeDetails;
import com.example.softeng.recipick.Models.Recipe;
import com.example.softeng.recipick.Models.Utility;
import com.example.softeng.recipick.R;
import com.example.softeng.recipick.ViewHolders.RecipeViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * RecyclerView.Adapter - binds data to the view
 * RecyclerView.Holder - holds the view
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeViewHolder> {

    /** Used to inflate the layout of the list of recipes (where Recycler and Card View are used) */
    private Context context;

    /** Holds the list of recipes - for now*/
    private List<Recipe> recipeList;
    private List<String> idList;

    public RecipeAdapter(Context context, Map<String, Recipe> recipeList) {
        this.context = context;
        this.recipeList = new ArrayList<>(recipeList.values());
        this.idList = new ArrayList<>(recipeList.keySet());

    }

    /**
     * Creates a view holder (the UI elements)
     * @param viewGroup
     * @param viewType
     * @return - an instance of type RecipeViewHolder
     */
    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Creating a view object with the use of LayoutInflater
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.card_recycler_view_layout, null);
        return new RecipeViewHolder(view);
    }

    /**
     * Bind data to view holder
     * @param holder
     * @param pos
     */
    @Override
    public void onBindViewHolder(final RecipeViewHolder holder, int pos) {
        final Recipe recipe = recipeList.get(holder.getAdapterPosition());
        holder.textViewName.setText(recipe.getName());
        holder.textViewDesc.setText(recipe.getDescription());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewItem = new Intent(context, RecipeDetails.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Utility.RECIPE, recipe);
                bundle.putString(Utility.ID, idList.get(holder.getAdapterPosition()));
                viewItem.putExtras(bundle);
                context.startActivity(viewItem);
            }
        });

        Glide.with(this.context)
                .load(recipe.getImages().get(0))
                .centerCrop()
                .placeholder(R.drawable.ic_applogo)
                .error(R.drawable.ic_applogo)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);
    }

    /**
     * Returns the size of the list
     * @return - size of the list
     */
    @Override
    public int getItemCount() {
        return recipeList.size();
    }

}
