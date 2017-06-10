package com.anthropicandroid.patterngallery.presenters;

/*
 * Created by Andrew Brin on 7/13/2016.
 */

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.anthropicandroid.patterngallery.R;
import com.anthropicandroid.patterngallery.entities.ui.SVGItemViewModel;
import com.anthropicandroid.patterngallery.routers.gallery.GalleryActivityComponent;

public class GalleryImageAdapter {

    public static final String TAG = GalleryImageAdapter.class.getSimpleName();

    @BindingAdapter("svgItemViewModel")
    public static void setItemViewModel(
            final GalleryActivityComponent galleryActivityComponent,
            final ImageView imageView,
            final SVGItemViewModel svgItemViewModel
    ) {
        int maxChildWidth = svgItemViewModel.getMaxChildWidth();
        int width = svgItemViewModel.getLastKnownWidth();
        int height = svgItemViewModel.getLastKnownHeight();

        // Set image parameters from assigned width
//        final int imageBoundsHeight = width * 3 / 5;
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                maxChildWidth,
                maxChildWidth);
        imageView.setLayoutParams(layoutParams);

        imageView.setBackgroundColor(ContextCompat.getColor(
                imageView.getContext(),
                svgItemViewModel.getColorResId()));

        imageView.setImageDrawable(imageView.getContext().getResources().getDrawable(R.drawable.ic_empty_rect_24dp));
    }

    private static class GalleryImageHolder {
        Bitmap image;
        Pair<Integer, Integer> rawMeasurements;
        String description;

        public GalleryImageHolder(
                Bitmap image,
                Pair<Integer, Integer> rawMeasurements,
                String description
        ) {
            this.image = image;
            this.rawMeasurements = rawMeasurements;
            this.description = description;
        }
    }
}
