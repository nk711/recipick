package com.example.softeng.recipick.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.softeng.recipick.Adapters.FirestoreRecipeAdapter;
import com.example.softeng.recipick.Models.Recipe;
import com.example.softeng.recipick.Models.User;
import com.example.softeng.recipick.Models.Utility;
import com.example.softeng.recipick.R;


import com.example.softeng.recipick.ViewHolders.RecipeViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.MoreObjects;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    private CollectionReference recipeRef;
    private DocumentReference userRef;
    private FirestoreRecyclerAdapter recipeAdapter;

    private String uid;
    private static final String USERS = "Users";
    private static final String INGREDIENTSQUERY = "ingredientsQuery";
    private static final String INGREDIENTS = "ingredients";
    private static final String RECIPES = "Recipes";


    private Button btnSearch;
    private EditText txtSearch;
    RecyclerView recyclerView;

    private OnFragmentInteractionListener mListener;

    public ListOfRecipesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListOfIngredientsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListOfRecipesFragment newInstance(String param1, String param2) {
        ListOfRecipesFragment fragment = new ListOfRecipesFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_of_recipes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        btnSearch = (Button) view.findViewById(R.id.btnSearch);
        txtSearch = (EditText) view.findViewById(R.id.txtSearch);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        uid = Utility.getUid();
        recipeRef = FirebaseFirestore.getInstance().collection(RECIPES);
        userRef = FirebaseFirestore.getInstance().collection(USERS).document(uid);


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
                 */
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
        });
        setAdapter(null);
    }


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
