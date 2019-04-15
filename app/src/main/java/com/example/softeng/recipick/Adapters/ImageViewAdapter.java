/**
 *  ImageAdapter.java
 */
package com.example.softeng.recipick.Adapters;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.softeng.recipick.R;

import java.util.ArrayList;
import java.util.List;

/**
 *  @author Nithesh Koneswaran
 */
public class ImageViewAdapter extends PagerAdapter {
    /** The context of the activity */
    private Context context;
    /** The list of images as URL from firebase */
    private List<String> images;
    /** The layout inflator to set the view */
    private LayoutInflater layoutInflater;

    /**
     * Constructor to set the context and initialise the arraylist
     * @param context
     */
    public ImageViewAdapter(Context context) {
        this.context = context;
        images = new ArrayList<>();
    }

    public void setImages(List<String> images) {
        if (this.images!=null) images = new ArrayList<>();
        images.addAll(images);}
    /**
     * @param image
     *      adds the image to the addapter
     */
    public void addImage(String image) {
        images.add(image);
    }

    /**
     * @returns the number of images to display
     */
    @Override
    public int getCount() {
        return images.size();
    }

    /**
     * Checks if the view == object
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    /**
     * Goes through each position of the arraylist and will ultimately create the image slideshow
     * @returns the view
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.image_holder, container, false);
        ImageView image = (ImageView) view.findViewById(R.id.gallery);

        Glide.with(this.context)
                .load(images.get(position))
                .centerCrop()
                .placeholder(R.drawable.ic_applogoo)
                .error(R.drawable.ic_applogoo)
                .into(image);

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;
    }


    /**
     * Destroys the image at the sepecified position
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }
}

