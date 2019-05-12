package com.example.softeng.recipick.Fragments;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.softeng.recipick.R;

import es.dmoral.toasty.Toasty;


/**
 * Able to switch between the recipe tab and the ingredient tab
 */
public class RecipeTab extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.recipeTab:
                    ListOfRecipesFragment recipeTab = new ListOfRecipesFragment();
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame, recipeTab, "Recipe");
                    fragmentTransaction.commit();
                    return true;
                case R.id.ingredientsTab:
                    if(connectedToInternet()) {
                        ListOfIngredientsFragment listOfIngredientsFragment = new ListOfIngredientsFragment();
                        FragmentTransaction fragmentTransaction2 = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction2.replace(R.id.frame, listOfIngredientsFragment, "List Of Ingredients");
                        fragmentTransaction2.commit();
                        return true;
                    } else {
                        Toasty.warning(requireContext(), "Please check your internet connection",Toasty.LENGTH_LONG, true).show();
                    }
            }
            return false;
        }
    };


    public RecipeTab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecipeTab.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeTab newInstance(String param1, String param2) {
        RecipeTab fragment = new RecipeTab();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListOfRecipesFragment recipeTab = new ListOfRecipesFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, recipeTab, "Recipe");
        fragmentTransaction.commit();

        BottomNavigationView navigation = (BottomNavigationView) view.findViewById(R.id.bottom_nav);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    public boolean connectedToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
            if(networkInfos != null) {
                for(int i = 0; i < networkInfos.length; i++) {
                    if(networkInfos[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.recipetab_layout, container, false);
    }

    @Override
    public void onPause() {
        super.onPause();

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
    public void onDestroy() {
        super.onDestroy();
    }
}

