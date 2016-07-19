package com.anthropicandroid.photogallery.viewmodel;

/*
 * Created by Andrew Brin on 7/17/2016.
 */

import android.animation.Animator;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

public class GalleryToDetailAnimator implements BackPressedRepo.BackPressedHandler {

    public static final String TAG = GalleryToDetailAnimator.class.getSimpleName();
    private BackPressedRepo backPressedRepo;
    private int statusBarHeight;

    public GalleryToDetailAnimator(BackPressedRepo backPressedRepo, int statusBarHeight) {
        this.backPressedRepo = backPressedRepo;
        this.statusBarHeight = statusBarHeight;
    }

    @Override
    public boolean backPressedConsumed() {
        return false;
    }


    public void zoomToReplace(
            Rect currentRect,
            View mattingLayout,
            ImageView newImage,
            float trueImageRatio,
            MotionEvent motionEvent,
            RecyclerView galleryGrid) {
        // get rect to draw into
        Rect targetRect = new Rect();
        Point targetOffset = new Point();
        Log.d(TAG, "current rect reporting at "
                + " top: " + currentRect.top
                + " bottom: " + currentRect.bottom
                + " height: " + currentRect.height()
                + " left: " + currentRect.left
                + " right: " + currentRect.right
                + " width: " + currentRect.width()
        );
        if (!galleryGrid.getGlobalVisibleRect(targetRect, targetOffset)) return;
        targetRect.offset(-targetOffset.x, statusBarHeight - targetOffset.y);
        int rawY = (int) motionEvent.getRawY();
        int rawX = (int) motionEvent.getRawX();
        // get ratio of image height to width
        detailImageImageAnim(currentRect, newImage, trueImageRatio, targetRect);

        detailImageMattingAnim(mattingLayout, targetRect, rawY, rawX);

//        animator.start();

        // get imageview animation


    }

    private void detailImageImageAnim(
            Rect currentRect,
            ImageView newImage,
            float trueImageRatio,
            Rect targetRect) {
        // Ratio of image starting width to screen width
        float widthRatio = (float) currentRect.width() / targetRect.width();
        Log.d(TAG, " we have a image rati, and it is: "+trueImageRatio);
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
        newImage.setElevation(20);
        newImage.animate()
                .y((targetRect.height()-(targetRect.width()/trueImageRatio))/2)
                .x(targetRect.left)
                .scaleX(1)
                .scaleY(1)
                .z(25)
                .setListener(getLoggingListener(newImage, "new image"))
                .setInterpolator(new LinearInterpolator())
                .setDuration(1000);
    }

    private void detailImageMattingAnim(
            View mattingLayout,
            Rect targetRect,
            int rawY,
            int rawX) {// matting anim init
        String animationName = "matting";
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
        mattingLayout.setZ(0);
        mattingLayout.setPivotX(rawX);
        mattingLayout.setPivotY(rawY);
        mattingLayout.setAlpha(1);
        mattingLayout.requestFocus();
        mattingLayout.setVisibility(View.VISIBLE);

        Log.d(TAG, " pivot X: "+mattingLayout.getPivotX()+" pivot Y: "+mattingLayout.getPivotY());

//         perform background animation
        mattingLayout.animate()
                .x(targetRect.left)
                .y(targetRect.top)
                .z(15)
                .scaleX(1f)
                .scaleY(1f)
                .setInterpolator(new AccelerateDecelerateInterpolator())
//                .setListener(getLoggingListener(mattingLayout, animationName))
                .setDuration(250);
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
