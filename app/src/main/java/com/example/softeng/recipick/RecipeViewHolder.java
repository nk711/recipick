package com.example.softeng.recipick;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Class for UI elements
 */
class RecipeViewHolder extends RecyclerView.ViewHolder {

    /** Holds the image for recipes*/
    ImageView imageView;
    /** Holds the name and description for recipes */
    TextView textViewName, textViewDesc;

    public RecipeViewHolder(View itemView) {
        super(itemView);

        /** Getting views from the view from the instance of this class */
        imageView = itemView.findViewById(R.id.imageView);
        textViewName = itemView.findViewById(R.id.recipeName);
        textViewDesc = itemView.findViewById(R.id.recipeDesc);
    }
}