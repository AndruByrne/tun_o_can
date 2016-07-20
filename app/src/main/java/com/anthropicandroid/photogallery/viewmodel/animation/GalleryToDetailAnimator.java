package com.anthropicandroid.photogallery.viewmodel.animation;

/*
 * Created by Andrew Brin on 7/17/2016.
 */

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.databinding.DataBindingUtil;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.anthropicandroid.photogallery.databinding.LayoutActivityGalleryBinding;
import com.anthropicandroid.photogallery.viewmodel.BackPressedRepo;

public class GalleryToDetailAnimator implements BackPressedRepo.BackPressedHandler {

    public static final String TAG = GalleryToDetailAnimator.class.getSimpleName();
    public static final int DURATION = 700;
    public static final int HALF_DURATION = DURATION/2;
    private BackPressedRepo backPressedRepo;
    private DetailToGalleryAnimator detailToGalleryAnimator;
    private int statusBarHeight;
    private LayoutActivityGalleryBinding binding;

    public GalleryToDetailAnimator(
            BackPressedRepo backPressedRepo,
            DetailToGalleryAnimator detailToGalleryAnimator,
            int statusBarHeight) {
        this.backPressedRepo = backPressedRepo;
        this.detailToGalleryAnimator = detailToGalleryAnimator;
        this.statusBarHeight = statusBarHeight;
    }

    @Override
    public boolean backPressedConsumed() {
        return detailToGalleryAnimator.returnToGallery(binding);
    }


    public void zoomToReplace(
            Rect currentRect,
            View mattingLayout,
            ImageView newImage,
            float trueImageRatio,
            int rawY,
            int rawX,
            RecyclerView galleryGrid) {
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
        detailMattingAnim(mattingLayout, targetRect, rawY, rawX);

        detailToGalleryAnimator.recentRect(currentRect);
        backPressedRepo.addHandler(this);


    }

    private void detailImageAnim(
            Rect currentRect,
            ImageView newImage,
            float trueImageRatio,
            Rect targetRect) {
        // Ratio of image starting width to screen width
        float widthRatio = (float) currentRect.width() / targetRect.width();
        // image anim init
        newImage.setScaleX(widthRatio);
        newImage.setScaleY(widthRatio);
        // half the shrinkage of the image is on the topside; that it is still there is what
        // causes distortion; the final height is determined by the true ratio and the screen width
        int startingTop = currentRect.top -
                ((int) (((float) targetRect.width() / trueImageRatio) / 2));
        // the X axis is similar; 0 centers a former match_parent shunk to a pint in the middle
        // of the screen, 1/2 screen to the left to start on left side
        int startingLeft = (2 * currentRect.left - targetRect.width() + currentRect.width()) / 2;
        newImage.setTop(startingTop);
        newImage.setBottom(startingTop + currentRect.height());
        newImage.setLeft(startingLeft);
        newImage.setRight(startingLeft + currentRect.width());
        newImage.setVisibility(View.VISIBLE);
        newImage.setElevation(8);
        newImage.animate()
                .y((targetRect.height()-(targetRect.width()/trueImageRatio))/2)
                .x(targetRect.left)
                .scaleX(1)
                .scaleY(1)
                .z(12)
                .setListener(getTacticalListener(newImage))
//                .setListener(getLoggingListener(newImage, "new image"))
                .setDuration(DURATION)
                .setInterpolator(new LinearInterpolator());
    }

    private void detailMattingAnim(
            final View mattingLayout,
            Rect targetRect,
            int rawY,
            int rawX) {// matting anim init
        int mattingWidthScaleShift = mattingLayout.getWidth() / 2;
        int mattingHeightScaleShift = mattingLayout.getHeight() / 2;
        int offsetY = rawY - mattingHeightScaleShift;
        int offsetX = rawX - mattingWidthScaleShift;
        mattingLayout.setScaleX(0);
        mattingLayout.setScaleY(0);
        mattingLayout.setTop(offsetY);
        mattingLayout.setBottom(offsetY);
        mattingLayout.setLeft(offsetX);
        mattingLayout.setRight(offsetX);
        mattingLayout.setZ(6);
        mattingLayout.setAlpha(1);
        mattingLayout.requestFocus();
        mattingLayout.setVisibility(View.VISIBLE);

        // I do not like this way of animating at all; regular animation objects are better

        // Perform background animation from touch point
        mattingLayout.animate()
                .x(targetRect.left)
                .y(targetRect.top)
                .z(8)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(HALF_DURATION)
                .setListener(getTacticalListener(mattingLayout))
                .setInterpolator(new DecelerateInterpolator(2f));
//                .setListener(getLoggingListener(mattingLayout, animationName))
    }

    @NonNull
    private static AnimatorListenerAdapter getTacticalListener(final View view) {
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

    @NonNull
    private Animator.AnimatorListener getLoggingListener(
            final View view,
            final String animationName) {
        return new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                Log.d(
                        TAG,
                        "@ start of " + animationName + " anim, z: " + view.getZ()
                                + " visibility: " + view.getVisibility()
                                + " alpha: " + view.getAlpha()
                                + " scaleX " + view.getScaleX()
                                + " scaleY " + view.getScaleY()
                                + " top: " + view.getTop()
                                + " bottom: " + view.getBottom()
                                + " left: " + view.getLeft()
                                + "right: " + view.getRight());
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                view.requestFocus();
                view.findFocus();
                Log.d(
                        TAG,
                        "@ end of " + animationName + "  anim, z: " + view.getZ()
                                + " visibility: " + view.getVisibility()
                                + " alpha: " + view.getAlpha()
                                + " scaleX " + view.getScaleX()
                                + " scaleY " + view.getScaleY()
                                + " top: " + view.getTop()
                                + " bottom: " + view.getBottom()
                                + " left: " + view.getLeft()
                                + "right: " + view.getRight());
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        };
    }

}
