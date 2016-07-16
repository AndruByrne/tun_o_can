package com.anthropicandroid.photogallery.viewmodel;

/*
 * Created by Andrew Brin on 7/13/2016.
 */

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anthropicandroid.photogallery.R;
import com.anthropicandroid.photogallery.databinding.LayoutGridItemBinding;
import com.anthropicandroid.photogallery.injectionmodules.GalleryActivityComponent;

import java.util.List;

public class GalleryListAdapter extends RecyclerView.Adapter {

    public static final String TAG = GalleryListAdapter.class.getSimpleName();
    private int childWidth;
    private List<Integer> imageIndicies;

    public GalleryListAdapter(int childWidth, List<Integer> imageIndicies) {
        this.childWidth = childWidth;
        // class constructed by an annotated static function
        this.imageIndicies = imageIndicies;
    }

    @BindingAdapter(value = {"numSpans", "gridEntries"})
    public static void setEntries(
            GalleryActivityComponent galleryActivityComponent,
            RecyclerView view,
            Integer numSpans,
            List<Integer> imageIndicies) {
        // create and populate list adapter and give it to the view
        view.setAdapter(new GalleryListAdapter(
                galleryActivityComponent.getScreenWidthInDp() / numSpans,
                imageIndicies));
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {

        private LayoutGridItemBinding dataBinding;

        public BindingHolder(View rowView) {
            // get tie-in to this view's databinding
            super(rowView);
            dataBinding = DataBindingUtil.bind(rowView);
            dataBinding.setItem(new GalleryItem());
        }

        public LayoutGridItemBinding getDataBinding() {
            return dataBinding;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_grid_item,
                parent,
                false);
        return new BindingHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // Sets viewmodel index
        if (holder instanceof BindingHolder) {
            LayoutGridItemBinding itemBinding = ((BindingHolder) holder).getDataBinding();
            Integer imageIndex = imageIndicies.get(position);

            itemBinding.getItem().setWidth(childWidth);
            itemBinding.getItem().setIndex(imageIndex);
            itemBinding.getItem().setDescription("Photo "+position);
        }
    }

    @Override
    public int getItemCount() {
        return imageIndicies.size();
    }
}
