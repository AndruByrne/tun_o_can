package com.anthropicandroid.patterngallery.presenters;

/*
 * Created by Andrew Brin on 7/13/2016.
 */

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.anthropicandroid.patterngallery.entities.interactions.PatternMetaData;
import com.anthropicandroid.patterngallery.routers.gallery.GalleryActivityComponent;
import com.anthropicandroid.patterngallery.entities.ui.GalleryItemViewModel;
import com.anthropicandroid.patterngallery.entities.ui.RawBitmapMeasurement;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

public class GalleryImageAdapter {

    public static final String TAG = GalleryImageAdapter.class.getSimpleName();

    @BindingAdapter("galleryItemViewModel")
    public static void setImageIndex(
            final GalleryActivityComponent galleryActivityComponent,
            final ImageView imageView,
            final GalleryItemViewModel galleryItemViewModel
    ) {
        int width = galleryItemViewModel.getWidth();

        // Set image parameters from assigned width
        final int imageBoundsHeight = width * 3 / 5;
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                width,
                imageBoundsHeight);
        imageView.setLayoutParams(layoutParams);

        imageView.setBackgroundColor(ContextCompat.getColor(
                imageView.getContext(),
                galleryItemViewModel.getColorResId()));
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
