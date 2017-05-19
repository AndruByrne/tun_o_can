package com.anthropicandroid.patterngallery.presenters;

/*
 * Created by Andrew Brin on 7/13/2016.
 */

import android.content.res.TypedArray;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anthropicandroid.patterngallery.R;
import com.anthropicandroid.patterngallery.databinding.LayoutGalleryImageBinding;
import com.anthropicandroid.patterngallery.entities.ui.GalleryItemViewModel;
import com.anthropicandroid.patterngallery.routers.gallery.GalleryActivityComponent;
import com.anthropicandroid.patterngallery.entities.ui.RawBitmapMeasurement;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class GalleryListAdapter
        extends RecyclerView.Adapter {

    public static final String TAG = GalleryListAdapter.class.getSimpleName();
    private GalleryActionHandlers galleryActionHandlers;
    private int childWidth;
    private TypedArray bgColors;
    private List<Integer> imageIndicies;

    public GalleryListAdapter(
            GalleryActionHandlers galleryActionHandlers,
            int childWidth,
            TypedArray bgColors,
            List<Integer> imageIndicies
    ) {
        this.galleryActionHandlers = galleryActionHandlers;
        // class constructed by an annotated static function
        this.childWidth = childWidth;
        this.bgColors = bgColors;
        this.imageIndicies = imageIndicies;
    }

    @BindingAdapter(value = {
            "numSpans",
            "gridEntries"})
    public static void setEntries(
            GalleryActivityComponent galleryActivityComponent,
            RecyclerView view,
            Integer numSpans,
            List<Integer> imageIndicies
    ) {
        // create and populate list adapter and give it to the view
        view.setAdapter(new GalleryListAdapter(
                galleryActivityComponent.getGalleryActionHandlers(),
                galleryActivityComponent.getNarrowestScreenDimenInPx() / numSpans,
                view.getContext().getResources().obtainTypedArray(R.array.bg_colors),
                imageIndicies));
    }

    public static class BindingHolder
            extends RecyclerView.ViewHolder {

        private LayoutGalleryImageBinding dataBinding;

        public BindingHolder(
                View rowView,
                GalleryActionHandlers galleryActionHandlers
        ) {
            // get tie-in to this view's databinding
            super(rowView);
            dataBinding = DataBindingUtil.bind(rowView);
            // add the binding objects
            dataBinding.setViewModel(new GalleryItemViewModel());
            dataBinding.setGalleryActionHandlers(galleryActionHandlers);
        }

        public LayoutGalleryImageBinding getDataBinding() {
            return dataBinding;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(
            ViewGroup parent,
            int viewType
    ) {
        // inflate view
        LayoutGalleryImageBinding galleryImageBinding = LayoutGalleryImageBinding.inflate(
                (LayoutInflater) parent.getContext().getSystemService(LAYOUT_INFLATER_SERVICE),
                parent,
                false);
        return new BindingHolder(galleryImageBinding.getRoot(), galleryActionHandlers);
    }

    @Override
    public void onBindViewHolder(
            RecyclerView.ViewHolder holder,
            int position
    ) {
        // Sets item-specific data
        if (holder instanceof BindingHolder) {
            LayoutGalleryImageBinding galleryImageBinding = ((BindingHolder) holder)
                    .getDataBinding();
            Integer imageIndex = imageIndicies.get(position);

            GalleryItemViewModel galleryItemViewModel = galleryImageBinding.getViewModel();
            galleryItemViewModel.setRawBitmapMeasurement(new RawBitmapMeasurement());
            galleryItemViewModel.setWidth(childWidth);
            galleryItemViewModel.setIndex(imageIndex);
            galleryItemViewModel.setDescription("Photo " + position);
            galleryItemViewModel.setColorResId(bgColors
                    .getResourceId(position % 8, R.color.colorOrange));
        }
    }

    @Override
    public int getItemCount() {
        return imageIndicies.size();
    }
}
