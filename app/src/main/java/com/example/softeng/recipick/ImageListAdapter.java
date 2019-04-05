/**
 * ImageListAdapter.java
 */
package com.example.softeng.recipick;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

/**
 *  @author Nithesh Koneswaran
 */
public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ViewHolder> {
    /** the list of file names   */
    public List<String> fileNameList;
    /** the list of file directories */
    public List<Uri> fileList;
    /** the context of the passed activity*/
    public Context context;

    /**
     * The constructor of the imagelistAdapter
     * @param fileNameList
     *          the list of file names
     * @param fileList
     *          the list of file directories
     */
    public ImageListAdapter(List<String> fileNameList, List<Uri> fileList) {
            this.fileNameList = fileNameList;
            this.fileList = fileList;
    }


    /**
     * Inflates the row of the recycler view
     * @param parent
     * @param viewType
     * @returns the viewholder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_recipe_image_recycler_view_layout, parent, false);
        this.context = parent.getContext();
        return new ViewHolder(view);
    }

    /**
     * Binds data to the viewholder,
     * goes through each position and will add the filename and image to the row
     * @param holder
     * @param position
     *              the position of the row
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String fileName = fileNameList.get(position);
        holder.fileNameView.setText(fileName);
        Uri image = fileList.get(position);
        Toast.makeText(this.context, image.toString(), Toast.LENGTH_LONG).show();
        Glide.with(this.context)
                .load(image)
                .centerCrop()
                .placeholder(R.drawable.ic_applogoo)
                .error(R.drawable.ic_applogo)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.fileView);


        holder.fileView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }


    /**
     *
     * @returns the size of the list
     */
    @Override
    public int getItemCount() {
        return fileNameList.size();
    }

    /**0
     * @param position
     *          Deletes the specified position of the item in the recycler view
     */
    public void delete(int position) {
        try {
            fileNameList.remove(position);
            fileList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, fileNameList.size());
            notifyItemRangeChanged(position, fileList.size());
        } catch (ArrayIndexOutOfBoundsException e) {

        }
    }

    /**
     * Defines the attributes and behaviour of a view holder
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * Holds the view
         */
        View mView;

        /** The components of the view         */
        public TextView fileNameView;
        public ImageView fileView;
        public ImageView btnRemove;
        public LinearLayout background;

        public ViewHolder(View itemView) {
            super(itemView);
            mView= itemView;
            /** Initialising the components of the view */
            fileNameView = (TextView) mView.findViewById(R.id.txt_file);
            fileView = (ImageView) mView.findViewById(R.id.img);
            btnRemove = (ImageView) mView.findViewById(R.id.btn_delete);
            background = (LinearLayout) mView.findViewById(R.id.background);

            /**
             * Pressing the remove button on a specific row will delete that specific row
             */
            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    delete(getAdapterPosition());
                }
            });



        }
    }
}
