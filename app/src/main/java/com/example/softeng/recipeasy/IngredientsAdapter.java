package com.example.softeng.recipeasy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class IngredientsAdapter extends ArrayAdapter<String> {

    /** Holds the list of ingredients */
    private List<String> ingredientsList;

    /**
     * Parameterised constructor
     * @param context
     * @param objects
     */
    public IngredientsAdapter(Context context, List<String> objects) {
        super(context, R.layout.ingredients_row_layout, objects);
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
        View rowView = inflater.inflate(R.layout.ingredients_row_layout, parent, false);

        TextView textView = (TextView) rowView.findViewById(R.id.txtIngredient);
        textView.setText(ingredientsList.get(position));


        ImageButton imageButton = (ImageButton) rowView.findViewById(R.id.deleteIngredient);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Method to delete ingredient", Toast.LENGTH_SHORT).show();
            }
        });

        return rowView;
    }
}
