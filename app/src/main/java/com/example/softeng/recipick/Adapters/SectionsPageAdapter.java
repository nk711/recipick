package com.example.softeng.recipick.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.softeng.recipick.Activities.RecipeDetails;
import com.example.softeng.recipick.Models.Recipe;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPageAdapter extends FragmentPagerAdapter {

    private Recipe recipe;

    public SectionsPageAdapter(FragmentManager fm, Recipe recipe) {
        super(fm);
        this.recipe = recipe;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return RecipeDetails.PlaceholderFragment.newInstance(position + 1, recipe);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }
}