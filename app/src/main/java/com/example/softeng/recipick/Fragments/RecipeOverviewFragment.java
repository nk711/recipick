package com.example.softeng.recipick.Fragments;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import android.widget.TextView;
import android.widget.Toast;


import com.example.softeng.recipick.Adapters.IngredientsAndMeasurementsAdapter;
import com.example.softeng.recipick.Models.Ingredient;
import com.example.softeng.recipick.Models.Recipe;
import com.example.softeng.recipick.Models.Utility;
import com.example.softeng.recipick.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecipeOverviewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecipeOverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeOverviewFragment extends Fragment {
    private static final String TAG = "RecipeOverviewFragment";


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TROLLEY = "trolley";
    private static final String FAVOURITES = "favourites";
    private static final String USERS = "Users";
    private static final String USERFAVOURITES = "User_Favourites";

    /** the list of ingredient's name   */
    public List<String> rIngredients;
    /** the list of ingredient's measurements */
    public List<String> rMeasurements;
    /** the list of ingredient's quantity */
    public List<String> rQuantity;
    /** Adapter used to populate and view the user added ingredients */
    private IngredientsAndMeasurementsAdapter ingredientsAdapter;
    /** holds the list of ingredients for the user*/
    private RecyclerView ingredients_measurements_list;

    private FirebaseAuth mAuth;
    private DocumentReference userRef;

    private DocumentReference userFavouriteRef;
    private String uid;

    private String recipe_id;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /** Holds the selected recipe */
    private Recipe recipe;
    /** Button for adding recipe to favourites */
    private Button btnFavourites;
    /** Button for adding recipe to shopping list */
    private Button btnTrolley;

    private TextView recipeName, desc;

    private OnFragmentInteractionListener mListener;

    public RecipeOverviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecipeOverviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeOverviewFragment newInstance(String param1, String param2) {
        RecipeOverviewFragment fragment = new RecipeOverviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_recipe_overview, container, false);
        if (savedInstanceState != null) {
            recipe = (Recipe)savedInstanceState.getSerializable("recipe");
        }

        mAuth = FirebaseAuth.getInstance();
        uid =mAuth.getCurrentUser().getUid();
        userRef = FirebaseFirestore.getInstance().collection(USERS).document(uid);
        userFavouriteRef = FirebaseFirestore.getInstance().collection(USERFAVOURITES).document(uid);

        ingredients_measurements_list = view.findViewById(R.id.list_ingredients);


        btnFavourites = view.findViewById(R.id.btnFavourite);
        btnTrolley = view.findViewById(R.id.btnShop);

        recipeName = view.findViewById(R.id.recipeName);
        desc = view.findViewById(R.id.txtDescription);



        //initialising ingredient lists
        rIngredients = new ArrayList<>();
        rMeasurements = new ArrayList<>();
        rQuantity = new ArrayList<>();

        /** gets the bundle from the previous activity */
        Bundle extras = requireActivity().getIntent().getExtras();
        /** checks if the bundle is null */
        if (extras!=null) {
            /** if not set the item */
            recipe = (Recipe)extras.getSerializable(Utility.RECIPE);
            recipe_id = (String)extras.getString(Utility.ID);

            /** Set name and description of recipe */
            recipeName.setText(recipe.getName());
            desc.setText(recipe.getDescription());

            for(Ingredient item : recipe.getIngredients()) {
                rIngredients.add(item.getName());
                rMeasurements.add(item.getMeasurement());
                rQuantity.add(item.getQuantity());
            }

        }

        /** Button text changes depending whether the user had favourited the currently viewed recipe */
        if (Utility.checkFavouriteRecipe(requireContext(), recipe_id)) {
            btnFavourites.setText("Remove from favourites");

        } else {
            btnFavourites.setText("Add to favourite");
        }

        //Setting up ingredients adapter
        ingredientsAdapter = new IngredientsAndMeasurementsAdapter(rIngredients, rMeasurements, rQuantity, TAG);
        ingredients_measurements_list.setLayoutManager(new LinearLayoutManager(requireContext()));
        ingredients_measurements_list.setAdapter(ingredientsAdapter);

        /** Adds the recipe to the user's favourite list */
        btnFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** Checks if the recipe is null, if not go back to main page, otherwise
                 *  Check if the recipe exists in the user's favourite list, if not add to favourites*/
                if (recipe!=null||recipe_id!=null) {
                    if (Utility.checkFavouriteRecipe(requireContext(), recipe_id)) {
                        removeFromFavourites();
                    } else {
                        if(connectedToInternet()) {
                            addToFavourites();
                        } else {
                            Toasty.warning(getContext(), "Please check your internet connection.", Toasty.LENGTH_SHORT, true).show();
                        }
                    }
                } else {
                    requireActivity().onBackPressed();
                }
            }
        });

        /** Adds the ingredients from the recipe to the user's trolley */
        btnTrolley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** Checks if the recipe is null, if not go back to main page, otherwise
                 *  add ingredients to the shopping list */
                if (recipe!=null) {
                    if(connectedToInternet()) {
                        addToTrolley();
                    } else {
                        Toasty.warning(getContext(), "Please check your internet connection.", Toasty.LENGTH_SHORT, true).show();
                    }
                } else {
                    requireActivity().onBackPressed();
                }
            }
        });

        return view;
    }

    /** Removes the recipe from the user's recipe list */
    public void removeFromFavourites() {
        userRef.update(FAVOURITES+"."+recipe_id, FieldValue.delete())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Utility.saveUserDetails(requireContext());
                            Toasty.success(requireContext(), "Recipe removed from Favourites", Toasty.LENGTH_LONG, true).show();
                            btnFavourites.setText("Add to favourite");
                        } else {
                            Toasty.error(requireContext(), "An error has occurred, Please check your internet connection!", Toast.LENGTH_SHORT, true).show();
                        }
                    }
                });
    }

    /** Adds the recipe to the user's favourite list */
    public void addToFavourites() {
        userRef.update(FAVOURITES+"."+recipe_id, recipe)
              .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Utility.saveUserDetails(requireContext());
                            Toasty.success(requireContext(), "Recipe added to Favourites", Toasty.LENGTH_LONG, true).show();
                            btnFavourites.setText("Remove from favourites");

                        } else {
                            Toasty.error(requireContext(), "An error has occurred, Please check your internet connection!", Toast.LENGTH_SHORT, true).show();
                        }
                    }
                });

    }

    /** Adds the recipe's ingredients to the user's trolley */
    public void addToTrolley() {
        List<String> itemsInTrolly = Arrays.asList(Utility.retrieveUserTrolley(requireContext()));
        Map<String, Object> trolley = new HashMap<>();
        /** Checks if the user has any ingredients to compare to, if so then begin to filter out all the recipe ingredients that are in the users trolley */
        if (itemsInTrolly.size()!=0) {
            for (String ingredient : recipe.getIngredientsQuery().keySet()) {
                if (!itemsInTrolly.contains(ingredient)) {
                    trolley.put(TROLLEY + "." + ingredient, true);
                }
            }
            /** Since the user has no ingredients, simply add the items into the trolley */
        } else {
            for (String ingredient : recipe.getIngredientsQuery().keySet()) {
                    trolley.put(TROLLEY + "." + ingredient, true);
            }

        }
        if (trolley.size()==0)  {
            Toasty.info(requireContext(), "Ingredients are already in your trolley!", Toast.LENGTH_SHORT, true).show();
        } else {
            /** Updates the users trolley */
            userRef.update(trolley)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Utility.saveUserDetails(requireContext());
                                Toasty.success(requireContext(), "Recipe ingredients added to your trolley", Toasty.LENGTH_LONG, true).show();
                            } else {
                                Toasty.error(requireContext(), "An error has occurred, Please check your internet connection!", Toast.LENGTH_SHORT, true).show();
                            }
                        }
                    });
        }
    }


    /**
     * A method that checks if there is an internet connection.
     *
     * @return whether or not there is an internet connection.
     */
    public boolean connectedToInternet() {
        // Creates a ConnectivityManager object that checks the connectivity service
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        // If there is a connectivity service then get its information
        if (connectivityManager != null) {
            NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
            // If network info is not null, check if the state of the network info is connected
            if (networkInfos != null) {
                for (int i = 0; i < networkInfos.length; i++) {
                    if (networkInfos[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's state here
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
}
