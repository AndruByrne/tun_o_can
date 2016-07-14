package com.anthropicandroid.photogallery.ViewModel;

/*
 * Created by Andrew Brin on 7/13/2016.
 */

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anthropicandroid.photogallery.databinding.ActivityGalleryBinding;

import java.util.List;

public class GalleryGridAdapter extends RecyclerView.Adapter {

    private List<Integer> imageIndicies;

    public GalleryGridAdapter(List<Integer> imageIndicies) {
        // class constructed by an annotated static function
        this.imageIndicies = imageIndicies;
    }

    @BindingAdapter("entries")
    public static void setEntries(
            ActivityGalleryBinding activityGalleryBinding,
            RecyclerView view,
            List<Integer> imageIndicies) {
        GalleryGridAdapter adapter = new GalleryGridAdapter(imageIndicies);
        view.setAdapter(adapter);
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {

        private ViewDataBinding dataBinding;

        public BindingHolder(View rowView) {
            super(rowView);
            // get tie-in to this view's databinding
            dataBinding = DataBindingUtil.bind(rowView);
        }

        public ViewDataBinding getDataBinding() {
            return dataBinding;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                android.R.layout.simple_list_item_1,
                parent,
                false);
        return new BindingHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BindingHolder) {
            BindingHolder bindingHolder = (BindingHolder) holder;
            Integer imageIndex = imageIndicies.get(position);
        }
    }

    @Override
    public int getItemCount() {
        return imageIndicies.size();
    }
}
