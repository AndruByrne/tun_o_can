package com.anthropicandroid.patterngallery.view.animation;

/*
 * Created by Andrew Brin on 7/17/2016.
 */

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.anthropicandroid.patterngallery.R;
import com.anthropicandroid.patterngallery.databinding.LayoutActivityGalleryBinding;
import com.anthropicandroid.patterngallery.routers.gallery.BackPressedRepo;

public class GalleryToDetailAnimator implements BackPressedRepo.BackPressedHandler {

    public static final String TAG = GalleryToDetailAnimator.class.getSimpleName();
    private BackPressedRepo backPressedRepo;
    private DetailToGalleryAnimator detailToGalleryAnimator;
    private int statusBarHeight;
    private Resources resources;
    private LayoutActivityGalleryBinding binding;

    public GalleryToDetailAnimator(
            BackPressedRepo backPressedRepo,
            DetailToGalleryAnimator detailToGalleryAnimator,
            int statusBarHeight,
            Resources resources
    ) {
        this.backPressedRepo = backPressedRepo;
        this.detailToGalleryAnimator = detailToGalleryAnimator;
        this.statusBarHeight = statusBarHeight;
        this.resources = resources;
    }

    @Override
    public boolean backPressedConsumed() {
        return binding != null && detailToGalleryAnimator.returnToGallery(binding);
    }


    public void zoomToReplace(
            Rect currentRect,
            View mattingLayout,
            ImageView newImage,
            float trueImageRatio,
            int rawY,
            int rawX,
            RecyclerView galleryGrid
    ) {
        // hold a reference to the data binding to request unanimation
        binding = DataBindingUtil.findBinding(galleryGrid);
        // get rect to draw into
        Rect targetRect = new Rect();
        Point targetOffset = new Point();
        if (!galleryGrid.getGlobalVisibleRect(targetRect, targetOffset)) return;
        targetRect.offset(-targetOffset.x, statusBarHeight - targetOffset.y);
        // get imageview animation
        detailImageAnim(currentRect, newImage, trueImageRatio, targetRect);

        // get animation for fast matte
        detailMattingAnim(
                mattingLayout,
                targetRect,
                rawY,
                rawX);
        detailToGalleryAnimator.storeRecentRect(currentRect);
        backPressedRepo.addHandler(this);
    }

    private void detailImageAnim(
            Rect currentRect,
            ImageView newImage,
            float trueImageRatio,
            Rect targetRect
    ) {
        // Ratio of image starting width to screen width
        final float widthRatio = (float) currentRect.width() / targetRect.width();
        // image anim init
        newImage.setScaleX(widthRatio);
        newImage.setScaleY(widthRatio);
        // TODO: use TRANSLATION from the CENTER of the shape!!!
        // half the shrinkage of the image is on the topside; that it is still there is what
        // causes distortion; the final height is determined by the true ratio and the screen width
        int startingTop = currentRect.top -
                ((int) (((float) targetRect.width() / trueImageRatio) / 2));
        // the X axis is similar; 0 centers a former match_parent shunk to a pint in the middle
        // of the screen, 1/2 screen to the left to start on left side
        int startingLeft = (2 * currentRect.left - targetRect.width() + currentRect.width()) / 2;
        newImage.setY(startingTop);
        newImage.setX(startingLeft);
        newImage.setZ(resources.getInteger(R.integer.z_position_detail_image_start));
        newImage.setVisibility(View.VISIBLE);

        newImage.animate()
                .translationY((targetRect.height() - (targetRect.width() / trueImageRatio)) / 2)
                .translationX(targetRect.left)
                .translationZ(resources.getInteger(R.integer.z_position_detail_image_emd))
                .scaleX(1)
                .scaleY(1)
                .setListener(getTacticalListener(newImage))
                .setStartDelay(resources.getInteger(R.integer.duration_gallery_to_detail)
                        / 4)
                .setDuration(resources.getInteger(R.integer.duration_gallery_to_detail)
                        * 3 / 4)
                .setInterpolator(new LinearInterpolator());
    }

    private void detailMattingAnim(
            final View mattingLayout,
            Rect targetRect,
            int rawY,
            int rawX) {
        // matting anim init
        int mattingWidthScaleShift = mattingLayout.getWidth() / 2;
        int mattingHeightScaleShift = mattingLayout.getHeight() / 2;
        int offsetY = rawY - mattingHeightScaleShift;
        int offsetX = rawX - mattingWidthScaleShift;

        mattingLayout.setScaleX(0);
        mattingLayout.setScaleY(0);
        mattingLayout.setY(offsetY);
        mattingLayout.setX(offsetX);
        mattingLayout.setZ(resources.getInteger(R.integer.z_position_matting_start));
        mattingLayout.setAlpha(1);
        mattingLayout.setPivotX(rawX);
        mattingLayout.setPivotY(rawY);
        mattingLayout.requestFocus();
        mattingLayout.setVisibility(View.VISIBLE);

        // I do not like this way of animating at all; regular animation objects are better

        // Perform background animation from touch point
        mattingLayout.animate()
                .x(targetRect.left)
                .y(targetRect.top)
                .z(resources.getInteger(R.integer.z_position_matting_end))
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(resources.getInteger(R.integer.duration_gallery_to_detail))
                .setListener(getTacticalListener(mattingLayout))
                .setInterpolator(new DecelerateInterpolator(2f));
    }

    @NonNull
    private static AnimatorListenerAdapter getTacticalListener(
            final View view
    ) {
        return new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setFocusable(true);
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                view.setFocusable(false);
                super.onAnimationStart(animation);
            }
        };
    }
}
