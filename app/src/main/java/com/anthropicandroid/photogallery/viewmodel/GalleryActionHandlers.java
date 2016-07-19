package com.anthropicandroid.photogallery.viewmodel;

/*
 * Created by Andrew Brin on 7/12/2016.
 */

import android.app.Application;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.anthropicandroid.photogallery.databinding.LayoutActivityGalleryBinding;
import com.anthropicandroid.photogallery.databinding.LayoutGalleryImageBinding;
import com.anthropicandroid.photogallery.viewmodel.animation.GalleryToDetailAnimator;

final public class GalleryActionHandlers {

    public static final String TAG = GalleryActionHandlers.class.getSimpleName();
    private GalleryToDetailAnimator animator;
    private final GestureDetector singleTapUpDetector;
    private final GestureDetector downCatchDetector;

    public GalleryActionHandlers(
            GalleryToDetailAnimator animator,
            Application context) {
        this.animator = animator;
        singleTapUpDetector = new GestureDetector(context, new SingleTapUp());
        downCatchDetector = new GestureDetector(context, new DownCatch());
    }

    public boolean gridTouched(View view, MotionEvent motionEvent) {

        // only want tap ups
        if (!singleTapUpDetector.onTouchEvent(motionEvent)) return false;
        // don't want onDown
        if (downCatchDetector.onTouchEvent(motionEvent)) return true;

        Rect currentViewBounds = new Rect();
        LayoutGalleryImageBinding gridItemBinding = DataBindingUtil.findBinding(view);
        LayoutActivityGalleryBinding activityGalleryBinding = DataBindingUtil
                .findBinding((View) view.getParent().getParent());
        GalleryItem galleryItem = gridItemBinding.getItem();
        DetailImage detailImage = activityGalleryBinding.getAlphaDetailImage();

        // measure current view
        view.getGlobalVisibleRect(currentViewBounds);

        // pass along the measurements for the raw bitmap
        detailImage.setRawBitmapMeasurement(galleryItem.getRawBitmapMeasurement());
        // set detailIndex in data binding
        detailImage.setDetailIndex(galleryItem.getIndex());
        // set description in data binding
        detailImage.setDescription(galleryItem.getDescription());

        // animate the detail in
        RawBitmapMeasurement rawBitmapMeasurement = detailImage
                .getRawBitmapMeasurement();
        animator.zoomToReplace(
                currentViewBounds,
                activityGalleryBinding.alphaDetailMattingLayout,
                activityGalleryBinding.alphaDetailImageView,
                (float)rawBitmapMeasurement.getRawWidth() /
                        rawBitmapMeasurement.getRawHeight(),
                        motionEvent,
                activityGalleryBinding.galleryGrid);
        Log.d(TAG, "finished handler tasks");
        return true;
    }

    public void returnToGallery(View view){
        animator.returnToGallery();
    }

    private class SingleTapUp extends GestureDetector.SimpleOnGestureListener {
        // return true for event we want, and also onDown
        @Override
        public boolean onSingleTapUp(MotionEvent e) { return true; }

        @Override
        public boolean onDown(MotionEvent e) { return true; }
    }

    private class DownCatch extends GestureDetector.SimpleOnGestureListener {
        // return true for onDown so we can eliminate it
        @Override
        public boolean onDown(MotionEvent e) { return true; }
    }
}
