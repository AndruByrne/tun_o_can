package com.anthropicandroid.photogallery.viewmodel;

/*
 * Created by Andrew Brin on 7/12/2016.
 */

import android.app.Application;
import android.content.ContextWrapper;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.anthropicandroid.photogallery.databinding.LayoutActivityGalleryBinding;
import com.anthropicandroid.photogallery.databinding.LayoutGalleryImageBinding;
import com.anthropicandroid.photogallery.databinding.LayoutImageDetailBinding;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

final public class GalleryActionHandlers {

    public static final String TAG = GalleryActionHandlers.class.getSimpleName();
    private GalleryToDetailAnimator animator;
    private DetailActionHandlers detailActionHandlers;
    private final GestureDetector singleTapUpDetector;
    private final GestureDetector downCatchDetector;

    public GalleryActionHandlers(
            GalleryToDetailAnimator animator,
            Application context,
            DetailActionHandlers detailActionHandlers) {
        this.animator = animator;
        this.detailActionHandlers = detailActionHandlers;
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
        CoordinatorLayout rootActivityLayout = activityGalleryBinding.rootActivityLayout;
        AppCompatActivity activity = (AppCompatActivity) ((ContextWrapper) view.getContext())
                .getBaseContext();

        // measure current view
        view.getGlobalVisibleRect(currentViewBounds);

        // Bind data objects to layout
        LayoutImageDetailBinding imageDetailBinding = LayoutImageDetailBinding.inflate(
                (LayoutInflater) view.getContext().getSystemService(LAYOUT_INFLATER_SERVICE));
        // set action handlers in data binding
        imageDetailBinding.setDetailActionHandlers(detailActionHandlers);
        // pass along the measurements fo the raw bitmap, for performance
        imageDetailBinding.setRawBitmapMeasurement(gridItemBinding.getRawBitmapMeasurement());
        // set index in data binding
        imageDetailBinding.setDetailIndex(gridItemBinding.getItem().getIndex());

        // Views in the detail layout init as GONE, so this won't affect the layout
        RelativeLayout mattingLayout = imageDetailBinding.detailMattingLayout;
        // add the view!
        rootActivityLayout.addView(mattingLayout);

        // animate the detail in
        animator.zoomToReplace(
                currentViewBounds,
                mattingLayout,
                imageDetailBinding.detailImageView,
                motionEvent,
                activityGalleryBinding.galleryGrid);
        Log.d(TAG, "finished handler tasks");
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
