package com.example.softeng.recipick.ViewHolders;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.softeng.recipick.Models.Recipe;
import com.example.softeng.recipick.R;

/**
 * Class for UI elements
 */
public class RecipeViewHolder extends RecyclerView.ViewHolder {

    public View mView;
    /** Holds the image for recipes*/
    public ImageView imageView;
    /** Holds the name and description for recipes */
    public TextView textViewName, textViewDesc;

    public CardView row;

    public RecipeViewHolder(View itemView) {
        super(itemView);

        /** Getting views from the view from the instance of this class */
        imageView = itemView.findViewById(R.id.imageView);
        textViewName = itemView.findViewById(R.id.recipeName);
        textViewDesc = itemView.findViewById(R.id.recipeDesc);
        mView = itemView;

        /** Listener set on a row */
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());
            }
        });
    }

    private RecipeViewHolder.ClickListener mClickListener;

    //Interface to send callbacks...
    public interface ClickListener{
        public void onItemClick(View view, int position);
        public void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(RecipeViewHolder.ClickListener clickListener){
        mClickListener = clickListener;
    }


}