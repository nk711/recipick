package com.example.softeng.recipick;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * RecyclerView.Adapter - binds data to the view
 * RecyclerView.Holder - holds the view
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeViewHolder> {

    /** Used to inflate the layout of the list of recipes (where Recycler and Card View are used) */
    private Context context;

    /** Holds the list of recipes - for now*/
    private List<Recipe> recipeList;

    public RecipeAdapter(Context context, List<Recipe> recipeList) {
        this.context = context;
        this.recipeList = recipeList;

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
     * @param recipeViewHolder
     * @param pos
     */
    @Override
    public void onBindViewHolder(RecipeViewHolder recipeViewHolder, int pos) {
        // Get the specified recipe using pos
        Recipe recipe = recipeList.get(pos);

        recipeViewHolder.textViewName.setText(recipe.getName());
        recipeViewHolder.textViewDesc.setText(recipe.getDescription());

        try {
            Glide.with(this.context)
                    .load(recipe.getImages().get(0))
                    .centerCrop()
                    .placeholder(R.drawable.ic_applogoo)
                    .error(R.drawable.ic_applogo)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(recipeViewHolder.imageView);

            /** Catches an error caused if the recipe has no images */
        } catch (IndexOutOfBoundsException e) {

        }
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
