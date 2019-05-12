package com.example.softeng.recipick.Fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.softeng.recipick.Adapters.IngredientsAdapter;
import com.example.softeng.recipick.Models.User;
import com.example.softeng.recipick.Models.Utility;
import com.example.softeng.recipick.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListOfIngredientsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListOfIngredientsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListOfIngredientsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "ListOfIngredients";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private static final String REGEX = "[A-z ]+";


    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private FirebaseAuth mAuth;
    private DocumentReference userRef;
    private String uid;
    private static final String USERS = "Users";
    private static final String INGREDIENTS = "ingredients";

    private List<String> ingredients = new ArrayList<>();
    private IngredientsAdapter adapter;

    private EditText txtIngredient;
    private ListView listView;

    public ListOfIngredientsFragment() {
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
    public static ListOfIngredientsFragment newInstance(String param1, String param2) {
        ListOfIngredientsFragment fragment = new ListOfIngredientsFragment();
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
        return inflater.inflate(R.layout.fragment_list_of_ingredients, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnAddIngredient = view.findViewById(R.id.btnAddIngredient);
        txtIngredient = view.findViewById(R.id.txtIngredient);
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();

        userRef = FirebaseFirestore.getInstance().collection(USERS).document(uid);

        this.listView = (ListView) view.findViewById(R.id.ingredientsView);

        adapter = new IngredientsAdapter(ListOfIngredientsFragment.this.getContext(), ingredients, INGREDIENTS);

        loadUsersIngredients();

        listView.setAdapter(adapter);

        btnAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String ingredientField = txtIngredient.getText().toString().toLowerCase().trim();
                if (connectedToInternet() != true) {
                    Toasty.warning(requireContext(), "Please check your internet connection", Toasty.LENGTH_SHORT, true).show();
                } else if (ingredientField.isEmpty() || !ingredientField.matches(REGEX)) {
                    Toasty.warning(requireContext(), "Invalid Ingredient!", Toast.LENGTH_SHORT, true).show();
                } else if (ingredients.contains(ingredientField)) {
                    Toasty.warning(requireContext(), "Ingredient already exists", Toast.LENGTH_SHORT, true).show();
                }  else {
                    userRef.update(INGREDIENTS + "." + ingredientField, true)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        ingredients.add(ingredientField);
                                        adapter.notifyDataSetChanged();
                                        txtIngredient.setText(null);
                                        Utility.updateUserIngredients(requireContext(), ingredients);
                                    } else {
                                        Toasty.error(requireContext(), "An error has occurred, Please check your internet connection!", Toast.LENGTH_SHORT, true).show();
                                    }
                                }
                            });
                }
            }
        });
    }



    public void loadUsersIngredients() {
        userRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                //Toasty.warning(requireContext(), "should work", Toast.LENGTH_SHORT, true).show();
                                User user = document.toObject(User.class);
                                ingredients.addAll(user.getIngredients().keySet());
                                //Toasty.warning(requireContext(), ingredients.toString(), Toast.LENGTH_SHORT, true).show();
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            //Toasty.info(requireContext(), "no work", Toast.LENGTH_SHORT, true).show();
                        }
                    }
                });
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
    }


    @Override
    public void onStop() {
        super.onStop();
    }


}
