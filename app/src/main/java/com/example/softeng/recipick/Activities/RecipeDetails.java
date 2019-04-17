package com.example.softeng.recipick.Activities;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.softeng.recipick.Adapters.ImageViewAdapter;
import com.example.softeng.recipick.Adapters.SectionsPageAdapter;
import com.example.softeng.recipick.Models.Recipe;
import com.example.softeng.recipick.Models.Utility;
import com.example.softeng.recipick.R;

import es.dmoral.toasty.Toasty;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

public class RecipeDetails extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPageAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    /* Recipe image component */
    private CollapsingToolbarLayout collapsingToolBar;

    /** Image view holder, will hold an array of images */
    private ViewPager mImageHolder;
    /** The selected recipe*/
    private Recipe recipe;
    /** Adapter which will keep a list of images to be displayed */
    private ImageViewAdapter adapter;

    private CheckBox btnFavourite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.recipe_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mImageHolder = findViewById(R.id.recipe_view_pager);
        adapter = new ImageViewAdapter(RecipeDetails.this);
        collapsingToolBar = findViewById(R.id.collapsing_toolbar);
       // btnFavourite = findViewById(R.id.btnFavourite);

        /** gets the bundle from the previous activity */
        Bundle extras = getIntent().getExtras();
        /** checks if the bundle is null */
        if (extras!=null) {
            /** if not set the item */
            recipe = (Recipe)extras.getSerializable(Utility.RECIPE);
        }


        /** Create the adapter that will return a fragment for each of the three
         primary sections of the activity. */
        mSectionsPagerAdapter = new SectionsPageAdapter(getSupportFragmentManager(), recipe);
        /** Set up the ViewPager with the sections adapter. */
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


        Toasty.error(RecipeDetails.this, recipe.getImages().get(0) , Toast.LENGTH_LONG, true).show();


        /**
         * gets the rotation of the device
         */
        int rotation = this.getResources().getConfiguration().orientation;

        /**
         * if its portrait resize the image appropriately
         */
        if (rotation == ORIENTATION_PORTRAIT) {
            DisplayMetrics metrics = new DisplayMetrics();
            this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int width = metrics.widthPixels;
            ViewGroup.LayoutParams params = mImageHolder.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = width;
            mImageHolder.setLayoutParams(params);
        }

        if (recipe!=null) {
            for (String url: recipe.getImages()) {
                adapter.addImage(url);
            }
            mImageHolder.setAdapter(adapter);

            SpannableString s = new SpannableString(recipe.getName());
            s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 0, recipe.getName().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            collapsingToolBar.setTitle(s);

        } else {
            /** If recipe is null, go back to main menu */

            //TODO: Add error message
            finish();

        }

        /**
        btnFavourite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    
                } else {

                }
            }
        }); */

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
