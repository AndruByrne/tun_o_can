package com.anthropicandroid.patterngallery.view.animation;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.SwipeDismissBehavior;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.anthropicandroid.patterngallery.databinding.LayoutActivityGalleryBinding;
import com.anthropicandroid.patterngallery.routers.gallery.GalleryActivityComponent;
import com.anthropicandroid.patterngallery.presenters.GalleryActionHandlers;

import java.util.List;

/*
 * Created by Andrew Brin on 7/19/2016.
 */
public class ImageDismissingBehavior extends SwipeDismissBehavior<FrameLayout> {

    public static final String TAG = ImageDismissingBehavior.class.getSimpleName();
    // I have some doubt as to whether this will work; it doesn't have a native XML constructor
    private Rect imageMeasurer = new Rect();
    private int rentMeasureRight = 0;
    private int rentMeasureLeft;

    public ImageDismissingBehavior(Context context, AttributeSet attrs) {
        super();
    }


    public ImageDismissingBehavior() {
        super();
    }

    @Override
    public boolean layoutDependsOn(
            CoordinatorLayout parent,
            FrameLayout child,
            View dependency
    ) {
        return dependency instanceof ImageView;
    }

    @Override
    public void setSwipeDirection(
            int direction
    ) {
        super.setSwipeDirection(SwipeDismissBehavior.SWIPE_DIRECTION_ANY);
    }

    //  TODO: rip out the swipy-swipes; just give a button
    @Override
    public boolean onDependentViewChanged(
            CoordinatorLayout parent,
            FrameLayout child,
            View dependency
    ) {
        // Focus is serving as an excellent flag; I was considering using a data binding var
        if (!dependency.isFocusable() ||
                // It could happen
                dependency.getVisibility() != View.VISIBLE
                ) return true;

        if (0 == rentMeasureRight) {
            Rect rentMeasurer = new Rect();
            parent.getGlobalVisibleRect(rentMeasurer);
            rentMeasureRight = rentMeasurer.right;
            rentMeasureLeft = rentMeasurer.left;
        }
        dependency.getGlobalVisibleRect(imageMeasurer);
        if (imageMeasurer.right - rentMeasureLeft > 100
                && rentMeasureRight - imageMeasurer.left > 100
                ) return true;
        rentMeasureRight = 0;
        child.setFocusable(false);
        dependency.setFocusable(false);
        child.setVisibility(View.GONE);
        dependency.setVisibility(View.GONE);
        return true;
    }

    @Override
    public void onDependentViewRemoved(
            CoordinatorLayout parent,
            FrameLayout child,
            View dependency
    ) {
        Log.d(TAG, "dependent view removed");
        GalleryActivityComponent activityComponent = (GalleryActivityComponent) DataBindingUtil
                .getDefaultComponent();
        GalleryActionHandlers actionHandlers = activityComponent
                .getGalleryActionHandlers();
        LayoutActivityGalleryBinding binding = DataBindingUtil.findBinding(child);
        // get the next picture to show
        Integer currentIndex = binding.getAlphaDetailImage().getDetailIndex();
        List<Integer> entries = binding.getEntries();
        int nextIndex = (currentIndex + 1) % entries.size();
        // clean up our mess
        dependency.setAlpha(1);
        child.setAlpha(1);
        dependency.setScaleX(1);
        dependency.setScaleY(1);
        child.setScaleX(1);
        child.setScaleY(1);
        dependency.setTop(0);
        dependency.setLeft(0);
        child.setTop(0);
        child.setLeft(0);
        // tell the onBackPressedRepo not to worry about this one
        activityComponent.getBackPressedRepo().releaseHandler(activityComponent
                .getGalleryToDetailAnimator());
        actionHandlers.selectImageForIndex(nextIndex);
    }

    @Override
    public void setDragDismissDistance(
            float distance
    ) {
        super.setDragDismissDistance(.2f);
    }
}
