package com.anthropicandroid.photogallery.viewmodel;

/*
 * Created by Andrew Brin on 7/17/2016.
 */

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class GalleryToDetailAnimator implements BackPressedRepo.BackPressedHandler {

    private BackPressedRepo backPressedRepo;

    public GalleryToDetailAnimator(BackPressedRepo backPressedRepo) {
        this.backPressedRepo = backPressedRepo;
    }

    @Override
    public boolean backPressedConsumed() {
        return false;
    }


    public void zoomToReplace(
            Rect currentViewBounds,
            RelativeLayout mattingLayout,
            ImageView newImage,
            MotionEvent motionEvent,
            RecyclerView galleryGrid) {
        // get ratio of image height to width

    }
}
