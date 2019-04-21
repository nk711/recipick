package com.example.softeng.recipick.Adapters;

import android.content.Context;
import android.media.Image;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.softeng.recipick.R;

import java.util.ArrayList;
import java.util.List;

public class SharedImageAdapter extends BaseAdapter  {
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
    public SharedImageAdapter(Context context, List<String> images) {
        this.context = context;
        this.images = images;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return images.get(position);
    }

    @Override
    public int getCount() {
            if (images==null) {
                return 0;
            } else {
                return images.size();
            }
    }

    /**
     *
     * @param position - what ingredient was clicked in the list
     * @param convertView - reference to the object that was clicked
     * @param parent - reference to the parent list view
     * @return - row that was updated
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView==null) {
            convertView = layoutInflater.inflate(R.layout.grid_view_item, parent, false);
            ImageView img = convertView.findViewById(R.id.image_holder);

            Glide.with(this.context)
                    .load(images.get(position))
                    .centerCrop()
                    .placeholder(R.drawable.ic_applogo)
                    .error(R.drawable.ic_applogo)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(img);
        }
        return convertView;
    }

}
