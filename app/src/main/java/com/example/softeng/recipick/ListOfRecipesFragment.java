package com.example.softeng.recipick;

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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


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


    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private String uid;
    private static final String USERS = "Users";
    private static final String CONTACTS = "Contacts";
    private static final String IMAGES = "Images";
    private static final String RECIPES = "Recipes";
    private User mUser;

    RecyclerView recyclerView;
    RecipeAdapter recipeAdapter;

    List<Recipe> recipeList;

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
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        uid = user.getUid();
        // Write a message to the database
        mDatabase= FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference().child(RECIPES);

        recipeList = new ArrayList<>();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(ListOfRecipesFragment.this.getActivity()));


        recipeAdapter = new RecipeAdapter(ListOfRecipesFragment.this.getActivity(), recipeList);
        recyclerView.setAdapter(recipeAdapter);


        myRef.addChildEventListener(new ChildEventListener() {

            public Recipe create(DataSnapshot dataSnapshot) {
                List<String> images = new ArrayList<>();
                Recipe recipe = dataSnapshot.getValue(Recipe.class);
                for (DataSnapshot img : dataSnapshot.child(IMAGES).getChildren()) {
                    images.add(img.getValue().toString());
                }
                recipe.setImages(images);
                return recipe;
            }
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                recipeList.add(create(dataSnapshot));
                recipeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Toast.makeText(getContext(), "2" , Toast.LENGTH_LONG).show();

                populateList(dataSnapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Toast.makeText(getContext(), "3" , Toast.LENGTH_LONG).show();

                if (recipeList.remove(create(dataSnapshot))) {
                    Toast.makeText(getContext(), "Removed", Toast.LENGTH_SHORT).show();
                }
                recipeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Toast.makeText(getContext(), "4" , Toast.LENGTH_LONG).show();

                populateList(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "5" , Toast.LENGTH_LONG).show();


            }
        });

/**
        recipeList.add(
                new Recipe("Pasta",
                        "Some pasta and sauce lol",
                        R.drawable.pasta
                ));
        recipeList.add(
                new Recipe("Chicken Dish",
                        "Very nice chicken, would recommend",
                        R.drawable.chicken
                ));
        recipeList.add(
                new Recipe("Pizza",
                        "Hella good pizza",
                        R.drawable.pizza
                ));

        recipeList.add(
                new Recipe("Pasta",
                        "Some pasta and sauce lol",
                        R.drawable.pasta
                ));
        recipeList.add(
                new Recipe("Chicken Dish",
                        "Very nice chicken, would recommend",
                        R.drawable.chicken
                ));
        recipeList.add(
                new Recipe("Pizza",
                        "Hella good pizza",
                        R.drawable.pizza
                ));

 **/


    }



    public void populateList(DataSnapshot dataSnapshot) {
        recipeList.clear();
        List<String> images = new ArrayList<>();

        Toast.makeText(getContext(), dataSnapshot.getKey() , Toast.LENGTH_LONG).show();


        for (DataSnapshot posts : dataSnapshot.child(RECIPES).getChildren()) {
                    Recipe recipe = posts.getValue(Recipe.class);
                    for (DataSnapshot img : posts.child(IMAGES).getChildren()) {
                        images.add(img.getValue().toString());
                    }
                    recipe.setImages(images);
                    recipeList.add(recipe);
                    images = new ArrayList<>();
        }
        recipeAdapter.notifyDataSetChanged();
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
