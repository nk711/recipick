package com.example.softeng.recipick.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.softeng.recipick.Adapters.ImageListAdapter;
import com.example.softeng.recipick.R;
import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecipePhotosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecipePhotosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipePhotosFragment extends Fragment {

    private static final String TAG = "Recipe Photos";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    /** The request code in order for the
     *  user to select multiple images,
     *  capture an image using the camera,
     *  to have access to external storage*/
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int REQUEST_CAPTURE_IMAGE = 100;
    private final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private final int MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 2;


    /** Holds the list of file names     */
    private List<String> fileNameList;
    /** Holds the list of file directories as URI */
    private List<Uri> fileList;


    private Uri imageUri;

    private ImageListAdapter adapter;

    private Button btnOpenDialog;


    /** Recycler view will hold the list of selected images */
    private RecyclerView mImages;


    /** Allows us to create custom dialogs */
    private AlertDialog.Builder mBuilder;
    /** Allows us to create custom dialogs */
    private View mView;


    private OnFragmentInteractionListener mListener;

    public RecipePhotosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecipePhotosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipePhotosFragment newInstance(String param1, String param2) {
        RecipePhotosFragment fragment = new RecipePhotosFragment();
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
        View view =  inflater.inflate(R.layout.fragment_recipe_photos, container, false);

        if (view != null) {
            btnOpenDialog = view.findViewById(R.id.btnOpenDialog);
            Log.d(TAG, "view is not null");

            if (btnOpenDialog != null) {
                btnOpenDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toasty.info(requireContext(), "no work", Toast.LENGTH_SHORT, true).show();

                        createDialog();
                    }
                });
                Log.d(TAG, "mButton is not null");
            }
        }


        return view;
    }


    public void createDialog() {
        Toasty.info(requireContext(), "no work", Toast.LENGTH_SHORT, true).show();

        mBuilder = new AlertDialog.Builder(requireContext());
        mView = getLayoutInflater().inflate(R.layout.dialog_add_photos, null);

        /** The dialog is displayed and shown to the user*/
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
