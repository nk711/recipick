/**
 * ImageListAdapter.java
 */
package com.example.softeng.recipick;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 *  @author Nithesh Koneswaran
 */
public class IngredientsAndMeasurementsAdapter extends RecyclerView.Adapter<IngredientsAndMeasurementsAdapter.ViewHolder> {
    /** the list of ingredient's name   */
    public List<String> ingredients;
    /** the list of ingredient's measurements */
    public List<String> measurements;
    /** the list of ingredient's quantity */
    public List<String> quantity;

    /** the context of the passed activity*/
    public Context context;

    /**
     * The constructor of the IngredientsAndMeasurementsAdapter
     * @param ingredients
     *          the list of ingredients names
     * @param measurements
     *          the list of ingredients paired with a measurement type and quantity
     */
    public IngredientsAndMeasurementsAdapter(List<String> ingredients, List<String> measurements, List<String> quantity) {
            this.ingredients = ingredients;
            this.measurements = measurements;
            this.quantity = quantity;
    }


    /**
     * Inflates the row of the recycler view
     * @param parent
     * @param viewType
     * @returns the viewholder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_recipe_ingredients_recycler_view_layout, parent, false);
        this.context = parent.getContext();
        return new ViewHolder(view);
    }

    /**
     * Binds data to the viewholder,
     * goes through each position and will add the filename and image to the row
     * @param holder
     * @param position
     *              the position of the row
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String description = quantity.get(holder.getAdapterPosition()) + " " +measurements.get(holder.getAdapterPosition());
        holder.ingredient_name.setText(ingredients.get(holder.getAdapterPosition()));
        holder.ingredient_desc.setText(description);
    }


    /**
     *
     * @returns the size of the list
     */
    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    /**0
     * @param position
     *          Deletes the specified position of the item in the recycler view
     */
    public void delete(int position) {
        try {
            ingredients.remove(position);
            measurements.remove(position);
            quantity.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, ingredients.size());
            notifyItemRangeChanged(position, measurements.size());
            notifyItemRangeChanged(position, quantity.size());
        } catch (ArrayIndexOutOfBoundsException e) {

        }
    }

    /**
     * Defines the attributes and behaviour of a view holder
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * Holds the view
         */
        View mView;




        /** The components of the view         */
        public TextView ingredient_name;
        public TextView ingredient_desc;
        public ImageView btnDelete;
        public LinearLayout background;

        public ViewHolder(View itemView) {
            super(itemView);
            mView= itemView;
            /** Initialising the components of the view */
             ingredient_name = (TextView) mView.findViewById(R.id.txtIngredient);
             ingredient_desc = (TextView) mView.findViewById(R.id.txtAmount);
             btnDelete = (ImageView) mView.findViewById(R.id.btn_delete);
            background = (LinearLayout) mView.findViewById(R.id.background);

            /**
             * Pressing the remove button on a specific row will delete that specific row
             */
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    delete(getAdapterPosition());
                }
            });



        }
    }
}
