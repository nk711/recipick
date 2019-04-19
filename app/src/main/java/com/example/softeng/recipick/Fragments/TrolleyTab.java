package com.example.softeng.recipick.Fragments;

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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TrolleyTab.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TrolleyTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrolleyTab extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private FirebaseAuth mAuth;
    private DocumentReference userRef;
    private String uid;
    private static final String USERS = "Users";
    private static final String TROLLEY = "trolley";

    private List<String> trolley = new ArrayList<>();
    private IngredientsAdapter adapter;

    private EditText txtIngredient;
    private ListView listView;

    public TrolleyTab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TrolleyTab.
     */
    // TODO: Rename and change types and number of parameters
    public static TrolleyTab newInstance(String param1, String param2) {
        TrolleyTab fragment = new TrolleyTab();
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
        return inflater.inflate(R.layout.fragment_trolley_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnAddIngredient = view.findViewById(R.id.btnAddIngredient);
        txtIngredient = view.findViewById(R.id.txtIngredient);
        mAuth = FirebaseAuth.getInstance();
        uid =mAuth.getCurrentUser().getUid();

        userRef = FirebaseFirestore.getInstance().collection(USERS).document(uid);

        this.listView = (ListView) view.findViewById(R.id.ingredientsView);

        adapter = new IngredientsAdapter(TrolleyTab.this.getContext(), trolley);

        loadUsersIngredients();

        listView.setAdapter(adapter);

        btnAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String ingredientField = txtIngredient.getText().toString().toLowerCase();
                if (ingredientField.isEmpty()) {
                    Toasty.warning(requireContext(), "Invalid Ingredient!", Toast.LENGTH_SHORT, true).show();
                } else if (trolley.contains(ingredientField)){
                    Toasty.warning(requireContext(), "Ingredient already exists", Toast.LENGTH_SHORT, true).show();
                } else {
                    userRef.update(TROLLEY+"."+ingredientField, true)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        trolley.add(ingredientField);
                                        adapter.notifyDataSetChanged();
                                        txtIngredient.setText(null);
                                        Utility.updateUserTrolley(requireContext(), trolley);
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
                                Toasty.warning(requireContext(), "should work", Toast.LENGTH_SHORT, true).show();
                                User user = document.toObject(User.class);
                                trolley.addAll(user.getTrolley().keySet());
                                Toasty.warning(requireContext(), trolley.toString(), Toast.LENGTH_SHORT, true).show();
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            Toasty.info(requireContext(), "no work", Toast.LENGTH_SHORT, true).show();
                        }
                    }
                });
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
