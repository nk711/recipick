package com.example.softeng.recipick.ViewHolders;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.softeng.recipick.Models.Recipe;
import com.example.softeng.recipick.Models.Utility;
import com.example.softeng.recipick.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber, Recipe recipe) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putSerializable(Utility.RECIPE, recipe);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = null;

        // Changes the fragments according to the tabs tabs
        switch(getArguments().getInt(ARG_SECTION_NUMBER)) {
            case 1:
                // Main 'overview' fragment - has the ingredients list
                rootView = inflater.inflate(R.layout.fragment_recipe_details, container, false);
                break;
            case 2:
                // 'Preparation' fragment
                rootView = inflater.inflate(R.layout.fragment_recipe_preparation, container, false);
                break;
            case 3:
                //TODO: Add photo gallery view here
                rootView = inflater.inflate(R.layout.fragment_recipe_photos, container, false);
                break;
        }
        return rootView;
    }
}