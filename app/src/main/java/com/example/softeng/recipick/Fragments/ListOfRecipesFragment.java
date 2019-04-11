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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.softeng.recipick.Models.Recipe;
import com.example.softeng.recipick.Models.User;
import com.example.softeng.recipick.R;


import com.example.softeng.recipick.ViewHolders.RecipeViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


import java.util.ArrayList;
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


  //  private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
  //  private DatabaseReference recipeRef;

 //   private FirebaseRecyclerOptions<Recipe> options;
  //  private FirebaseRecyclerAdapter<Recipe, RecipeViewHolder> recipeAdapter;

    private CollectionReference recipeRef;
    private DocumentReference userRef;
    private FirestoreRecyclerAdapter recipeAdapter;

    private String uid;
    private static final String USERS = "Users";
    private static final String INGREDIENTS = "ingredientsQuery";
    private static final String RECIPES = "Recipes";
    private User mUser;

    List<String> userIngredients;

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

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();

        recipeRef = FirebaseFirestore.getInstance().collection(RECIPES);
        userRef = FirebaseFirestore.getInstance().collection(USERS).document(uid);


        loadUsersIngredients();
        Query query = recipeRef.whereArrayContains(INGREDIENTS, userIngredients);

        FirestoreRecyclerOptions<Recipe> options = new FirestoreRecyclerOptions.Builder<Recipe>()
                .setQuery(query, Recipe.class)
                .build();

        Toasty.error(requireContext(), userIngredients.toString(), Toast.LENGTH_LONG, true).show();


        recipeAdapter = new FirestoreRecyclerAdapter<Recipe, RecipeViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RecipeViewHolder holder, int position, @NonNull Recipe recipe) {
                holder.textViewName.setText(recipe.getName());
                holder.textViewDesc.setText(recipe.getDescription());
                Glide.with(requireContext())
                        .load(recipe.getImages().get(0))
                        .centerCrop()
                        .placeholder(R.drawable.ic_applogoo)
                        .error(R.drawable.ic_applogo)
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.imageView);
            }

            @NonNull
            @Override
            public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                // Creating a view object with the use of LayoutInflater
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                View view = layoutInflater.inflate(R.layout.card_recycler_view_layout, null);
                return new RecipeViewHolder(view);
            }
        };

        //  recipeList = new ArrayList<>();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ListOfRecipesFragment.this.getActivity()));


        /** SELECT * FROM Recipes WHERE ingredients = user_ingredients
        Query query = recipeRef.child("ingredients").orderByChild("name").equalTo("chicken");

        options = new FirebaseRecyclerOptions.Builder<Recipe>()
                .setQuery(query, Recipe.class).build();

        recipeAdapter = new FirebaseRecyclerAdapter<Recipe, RecipeViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RecipeViewHolder holder, int position, @NonNull Recipe recipe) {
                holder.textViewName.setText(recipe.getName());
                holder.textViewDesc.setText(recipe.getDescription());
                try {
                    Glide.with(getContext())
                            .load(recipe.getImages().get(0))
                            .centerCrop()
                            .placeholder(R.drawable.ic_applogoo)
                            .error(R.drawable.ic_applogo)
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(holder.imageView);

                    /** Catches an error caused if the recipe has no images
                } catch (IndexOutOfBoundsException e) {
                }
            }


            @NonNull
            @Override
            public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                // Creating a view object with the use of LayoutInflater
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                View view = layoutInflater.inflate(R.layout.card_recycler_view_layout, null);
                return new RecipeViewHolder(view);
            }
        };

        */

        recyclerView.setAdapter(recipeAdapter);
      //  recipeAdapter = new RecipeAdapter(ListOfRecipesFragment.this.getActivity(), recipeList);
       // recipeAdapter.startListening();

       // recyclerView.setAdapter(recipeAdapter);



    }

    public void loadUsersIngredients() {
        userIngredients = new ArrayList<>();
        userRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            User user = documentSnapshot.toObject(User.class);
                            userIngredients.addAll(user.getIngredients());
                            Toasty.warning(requireContext(), userIngredients.toString(), Toast.LENGTH_SHORT, true).show();
                        }
                    }
                });
    }

    public void checkIfIngredientsIsBlank() {
        /**
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(USERS).child(uid).child(INGREDIENTS);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                   // Toasty.info(thi, "You currently have no ingredients in your collection, keep track of your ingredients and add them to your list in order to view recipe's based on your current ingredients!", Toast.LENGTH_LONG, true).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
         */
    }



    public void loadRecipes() {
       // Query query = recipeRef.whereArrayContains("/",  );
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
        if (recipeAdapter!=null)
            loadUsersIngredients();
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
        if (recipeAdapter!=null)
            loadUsersIngredients();
            recipeAdapter.startListening();
    }

}
