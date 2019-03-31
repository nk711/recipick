package com.example.softeng.recipick;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class RecipeIngredientsAdapter extends ArrayAdapter<RecipeIngredient> {

    /** Holds the list of ingredients */
    private List<RecipeIngredient> ingredientsList;

    /**
     * Parameterised constructor
     * @param context
     * @param objects
     */
    public RecipeIngredientsAdapter(Context context, List<RecipeIngredient> objects) {
        super(context, R.layout.add_recipe_ingredients_listview_layout, objects);
        this.ingredientsList = objects;
    }

    /**
     *
     * @param position - what ingredient was clicked in the list
     * @param convertView - reference to the object that was clicked
     * @param parent - reference to the parent list view
     * @return - row that was updated
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.add_recipe_ingredients_listview_layout, parent, false);
        TextView ingredient_name = (TextView) rowView.findViewById(R.id.txtIngredient);
        TextView ingredient_desc = (TextView) rowView.findViewById(R.id.txtAmount);
        ImageButton btnDelete = (ImageButton) rowView.findViewById(R.id.deleteIngredient);
        RecipeIngredient current_ingredient = ingredientsList.get(position);
        String howMuchOfAnIngredient = current_ingredient.getQuantity() + " " + current_ingredient.getMeasurement();

        ingredient_name.setText(current_ingredient.getTitle());
        ingredient_desc.setText(howMuchOfAnIngredient);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Method to delete ingredient", Toast.LENGTH_SHORT).show();
            }
        });

        return rowView;
    }
}
