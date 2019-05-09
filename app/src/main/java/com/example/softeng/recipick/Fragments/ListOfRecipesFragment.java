/**
 *  ListOfRecipesFragment.java
 */
package com.example.softeng.recipick.Fragments;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.softeng.recipick.Adapters.FirestoreRecipeAdapter;
import com.example.softeng.recipick.Models.Recipe;
import com.example.softeng.recipick.Models.Utility;
import com.example.softeng.recipick.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import es.dmoral.toasty.Toasty;
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListOfRecipesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListOfRecipesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListOfRecipesFragment extends Fragment {
    private CollectionReference recipeRef;
    /** Used to */
    private FirestoreRecyclerAdapter recipeAdapter;
    /** The uid of the currently logged in user*/
    private String uid;
    /** button component used to apply changes to the list of recipes */
    //private Button btnSearch;
    /** edit text component used to filter recipes */
    //private EditText txtSearch;
    /** holds the list of recipes */
    RecyclerView recyclerView;
    /** listens to fragment interactions*/
    private OnFragmentInteractionListener mListener;
    public ListOfRecipesFragment() {

    }
    // TODO: Rename and change types and number of parameters
    public static ListOfRecipesFragment newInstance() {
        ListOfRecipesFragment fragment = new ListOfRecipesFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_of_recipes, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //btnSearch = (Button) view.findViewById(R.id.btnSearch);
        //txtSearch = (EditText) view.findViewById(R.id.txtSearch);
        //holds the list of recipes
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        //Gets the uid of the currently logged in user
        uid = Utility.getUid();
        //setting the document reference path
        recipeRef = FirebaseFirestore.getInstance().collection(Utility.RECIPES);


        /**
         Code use: allows the user to search for recipes

        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /**
                 * If the user erases text such that txtField has length 0 then
                 *  display list as usual else, supply the additional query parameters
                 *  to the adapter.
                 *
                if (s.toString().length()==0) {
                    setAdapter(null);
                } else {
                    setAdapter(s.toString().trim());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setAdapter(txtSearch.toString().trim());
            }
        });*/

        /** Set adapter will load the list of recipes from the database **/
        setAdapter(null);
    }


    /**
     *
     *  This function will load the list of ingredients the user has and will update the list of recipes
     *  based on the user's ingredients. If the user has no ingredients, no filter is applied, and
     *  all the list of ingredients are added.
     *
     * @param additional
     *        used to allow the user to filter recipes by a name.
     */
    public void setAdapter (String additional) {
        String[] listOfIngredients = Utility.retrieveUserIngredients(this.requireContext());
        Query query = recipeRef.orderBy("name");
        for (String item : listOfIngredients) {
            if (!item.isEmpty())
                query = query.whereEqualTo("ingredientsQuery." + item, true);
        }
        if (additional!= null) {
            query = query.startAt(additional).endAt(additional+"\uf8ff");
            Toasty.error(requireContext(), additional, Toasty.LENGTH_LONG).show();
        }

        if (recipeAdapter!=null)
            recipeAdapter.stopListening();

        //Required to load the list of recipes
        FirestoreRecyclerOptions<Recipe> options = new FirestoreRecyclerOptions.Builder<Recipe>()
                .setQuery(query, Recipe.class)
                .build();

        recipeAdapter = new FirestoreRecipeAdapter(requireContext(), options);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ListOfRecipesFragment.this.getActivity()));
        recyclerView.setAdapter(recipeAdapter);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



    @Override
    public void onStart() {
        super.onStart();
        setAdapter(null);
        if (recipeAdapter!=null)
            recipeAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (recipeAdapter!=null)
            recipeAdapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        setAdapter(null);
        if (recipeAdapter!=null)
            recipeAdapter.startListening();
    }

}
