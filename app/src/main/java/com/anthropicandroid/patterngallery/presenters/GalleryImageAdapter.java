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
        int   maxChildWidth = svgItemViewModel.getMaxChildWidth();
        int   width         = svgItemViewModel.getLastKnownWidth();
        int   height        = svgItemViewModel.getLastKnownHeight();
        float heightToWidthRatio;
        if (height == 0 || width == 0)
            heightToWidthRatio = 1;
        else heightToWidthRatio = height / width;

        // Set image parameters from max possible width
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                maxChildWidth,  // The horizontal dimension will always be the maximum allowed
                (int) (maxChildWidth * heightToWidthRatio));  // The vertical dimension will be variable
        imageView.setLayoutParams(layoutParams);

        imageView.setBackgroundColor(ContextCompat.getColor(
                imageView.getContext(),
                svgItemViewModel.getColorResId()));

        imageView.setImageDrawable(svgItemViewModel.getDrawable());
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
