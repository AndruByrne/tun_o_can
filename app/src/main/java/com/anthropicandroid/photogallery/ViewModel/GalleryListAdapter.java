package com.anthropicandroid.photogallery.ViewModel;

/*
 * Created by Andrew Brin on 7/13/2016.
 */

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.v17.leanback.widget.VerticalGridView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anthropicandroid.photogallery.InjectionModules.GalleryActivityComponent;
import com.anthropicandroid.photogallery.R;
import com.anthropicandroid.photogallery.databinding.LayoutGridItemBinding;

import java.util.List;

public class GalleryListAdapter extends RecyclerView.Adapter {

    private List<Integer> imageIndicies;

    public GalleryListAdapter(List<Integer> imageIndicies) {
        // class constructed by an annotated static function
        this.imageIndicies = imageIndicies;
    }

    @BindingAdapter("entries")
    public static void setEntries(
            GalleryActivityComponent galleryActivityComponent,
            VerticalGridView view,
            List<Integer> imageIndicies) {
        // create and populate list adapter and give it to the view
        view.setAdapter(new GalleryListAdapter(imageIndicies));
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {

        private LayoutGridItemBinding dataBinding;

        public BindingHolder(View rowView) {
            // get tie-in to this view's databinding
            super(rowView);
            dataBinding = DataBindingUtil.bind(rowView);
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

            itemBinding.setIndex(imageIndex);
            itemBinding.setDescription(""+position);
        }
    }

    @Override
    public int getItemCount() {
        return imageIndicies.size();
    }
}
