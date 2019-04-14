package com.example.softeng.recipick.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.softeng.recipick.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class IngredientsAdapter extends ArrayAdapter<String> {

    /** Holds the list of ingredients */
    private List<String> ingredientsList;


    private DocumentReference userRef;
    private String uid;

    private static final String USERS = "Users";
    private static final String INGREDIENTS = "ingredients";


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
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.ingredients_row_layout, parent, false);

        uid = FirebaseAuth.getInstance().getUid();
        userRef = FirebaseFirestore.getInstance().collection(USERS).document(uid);

        TextView textView = (TextView) rowView.findViewById(R.id.txtIngredient);
        textView.setText(ingredientsList.get(position));


        ImageButton imageButton = (ImageButton) rowView.findViewById(R.id.deleteIngredient);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRef.update(INGREDIENTS + "." + ingredientsList.get(position), FieldValue.delete() )
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    ingredientsList.remove(position);
                                    notifyDataSetChanged();
                                } else {
                                    Toasty.error(getContext(), "An error has occurred, Please check your internet connection!", Toast.LENGTH_SHORT, true).show();
                                }
                            }
                        });

            }
        });

        return rowView;
    }
}
