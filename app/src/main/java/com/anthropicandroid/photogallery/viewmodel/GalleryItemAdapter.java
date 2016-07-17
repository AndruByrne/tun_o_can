package com.anthropicandroid.photogallery.viewmodel;

/*
 * Created by Andrew Brin on 7/16/2016.
 */

import android.databinding.BindingAdapter;
import android.util.Log;
import android.widget.FrameLayout.LayoutParams;
import android.widget.RelativeLayout;

public class GalleryItemAdapter {

    @BindingAdapter("setDimen")
    public static void setGalleryItem(
            RelativeLayout layout,
            GalleryItem galleryItem) {
        // set dimensions for the container layout for gallery Image
        Log.d(GalleryItemAdapter.class.getSimpleName(), "setting dimens");
        int width = galleryItem.getWidth();
        LayoutParams layoutParams = new LayoutParams(width, width/2);
        layout.setLayoutParams(layoutParams);
        layout.setBackgroundColor(galleryItem.getColorResId());
    }
}
