package com.anthropicandroid.patterngallery.presenters;

/*
 * Created by Andrew Brin on 7/12/2016.
 */

import android.app.Application;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.anthropicandroid.patterngallery.databinding.LayoutActivityGalleryBinding;
import com.anthropicandroid.patterngallery.databinding.LayoutGalleryImageBinding;
import com.anthropicandroid.patterngallery.entities.ui.DetailImage;
import com.anthropicandroid.patterngallery.entities.ui.GalleryItem;
import com.anthropicandroid.patterngallery.entities.ui.RawBitmapMeasurement;
import com.anthropicandroid.patterngallery.view.animation.GalleryToDetailAnimator;

final public class GalleryActionHandlers {

    public static final String TAG = GalleryActionHandlers.class.getSimpleName();
    private GalleryToDetailAnimator animator;
    private final GestureDetector singleTapUpDetector;
    private final GestureDetector downCatchDetector;
    private LayoutActivityGalleryBinding activityGalleryBinding;

    public GalleryActionHandlers(
            GalleryToDetailAnimator animator,
            Application context
    ) {
        this.animator = animator;
        singleTapUpDetector = new GestureDetector(context, new SingleTapUp());
        downCatchDetector = new GestureDetector(context, new DownCatch());
    }

    public void selectImageForIndex(
            int index
    ) {
        RecyclerView galleryGrid = activityGalleryBinding.galleryGrid;
        RecyclerView.LayoutManager layoutManager = galleryGrid.getLayoutManager();
        layoutManager.scrollToPosition(index);
        View view = layoutManager.getChildAt(index);
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        bindDataAndAnimate(
                view,
                rect.centerX(),
                rect.centerY());
    }


    public boolean gridTouched(
            View view,
            MotionEvent motionEvent
    ) {

        // only want tap ups
        if (!singleTapUpDetector.onTouchEvent(motionEvent)) return false;
        // don't want onDown
        if (downCatchDetector.onTouchEvent(motionEvent)) return true;
        float rawX = motionEvent.getRawX();
        float rawY = motionEvent.getRawY();
        return bindDataAndAnimate(
                view,
                (int)rawX,
                (int)rawY);
    }

    private boolean bindDataAndAnimate(
            View view,
            int rawX,
            int rawY
    ) {
        Rect currentViewBounds = new Rect();
        LayoutGalleryImageBinding gridItemBinding = DataBindingUtil.findBinding(view);
        if (activityGalleryBinding == null) activityGalleryBinding = DataBindingUtil
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
        //  TODO: discuss the identity between the animator and the handlers; maybe a useful concept
        animator.zoomToReplace(
                currentViewBounds,
                activityGalleryBinding.alphaDetailMattingLayout,
                activityGalleryBinding.alphaDetailImageView,
                (float) rawBitmapMeasurement.getRawWidth() /
                        rawBitmapMeasurement.getRawHeight(),
                rawY,
                rawX,
                activityGalleryBinding.galleryGrid);
        return true;
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
