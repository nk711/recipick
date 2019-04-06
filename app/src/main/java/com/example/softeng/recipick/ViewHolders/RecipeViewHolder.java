package com.example.softeng.recipick.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.softeng.recipick.R;

/**
 * Class for UI elements
 */
public class RecipeViewHolder extends RecyclerView.ViewHolder {

    /** Holds the image for recipes*/
    public ImageView imageView;
    /** Holds the name and description for recipes */
    public TextView textViewName, textViewDesc;

    public RecipeViewHolder(View itemView) {
        super(itemView);

        /** Getting views from the view from the instance of this class */
        imageView = itemView.findViewById(R.id.imageView);
        textViewName = itemView.findViewById(R.id.recipeName);
        textViewDesc = itemView.findViewById(R.id.recipeDesc);
    }
}