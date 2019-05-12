/**
 * FirestoreRecipeAdapter.java
 */
package com.example.softeng.recipick.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

/**
 * Custom view adapter for recipes pulled from the DB
 */
public class FirestoreRecipeAdapter extends FirestoreRecyclerAdapter<Recipe, RecipeViewHolder> {
    private Context context;

    /**
     * @param context of the activity that is using the adapter
     * @param options will be applied to filter the results
     */
    public FirestoreRecipeAdapter(Context context, @NonNull FirestoreRecyclerOptions<Recipe> options) {
        super(options);
        this.context = context;
    }

    /**
     * Called by RecyclerView to display data at specific position
     * @param holder ViewHolder which should be updated to represent contents of item at the current position in the data set
     * @param position position of current item
     * @param recipe recipe to be displayed
     */
    @Override
    protected void onBindViewHolder(@NonNull RecipeViewHolder holder, int position, @NonNull final Recipe recipe) {
        holder.textViewName.setText(recipe.getName());
        holder.textViewDesc.setText(recipe.getDescription());
        final DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewItem = new Intent(context, RecipeDetails.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Utility.RECIPE, recipe);
                bundle.putString(Utility.ID, snapshot.getId());
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
     * @param viewGroup
     * @param i
     * @return the inflated view
     *
     */
    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_recycler_view_layout, viewGroup,false);
        return new RecipeViewHolder(view);
    }
}
