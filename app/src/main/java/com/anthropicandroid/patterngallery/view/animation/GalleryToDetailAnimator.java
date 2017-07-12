package com.anthropicandroid.patterngallery.view.animation;

/*
 * Created by Andrew Brin on 7/17/2016.
 */

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.anthropicandroid.patterngallery.R;
import com.anthropicandroid.patterngallery.databinding.LayoutActivityGalleryBinding;
import com.anthropicandroid.patterngallery.entities.ui.SVGItemViewModel;
import com.anthropicandroid.patterngallery.routers.gallery.BackPressedRepo;
import com.anthropicandroid.patterngallery.view.PatternDetailView;

public class GalleryToDetailAnimator
        implements BackPressedRepo.BackPressedHandler {

    public static final String TAG = GalleryToDetailAnimator.class.getSimpleName();
    private BackPressedRepo backPressedRepo;
    private DetailToGalleryAnimator detailToGalleryAnimator;
    private DisplayMetrics displayMetrics;
    private int statusBarHeight;
    private Resources resources;
    private LayoutActivityGalleryBinding binding;

    public GalleryToDetailAnimator(
            BackPressedRepo backPressedRepo,
            DetailToGalleryAnimator detailToGalleryAnimator,
            DisplayMetrics displayMetrics,
            int statusBarHeight,
            Resources resources
    ) {
        this.backPressedRepo = backPressedRepo;
        this.detailToGalleryAnimator = detailToGalleryAnimator;
        this.displayMetrics = displayMetrics;
        this.statusBarHeight = statusBarHeight;
        this.resources = resources;
    }

    @Override
    public boolean backPressedConsumed() {
        return binding != null && detailToGalleryAnimator.returnToGallery(binding);
    }


    public void zoomToReplace(
            Rect tappedRect,
            View mattingLayout,
            PatternDetailView detailImage,
            float trueImageRatio,
            int clickRawX,
            int clickRawY,
            RecyclerView galleryGrid
    ) {
        // hold a reference to the data binding to request unanimation
        binding = DataBindingUtil.findBinding(galleryGrid);
        // get rect to draw into
        Rect mattingTargetRect = new Rect();
        Point mattingTargetOffset = new Point();
        if (galleryGrid.getGlobalVisibleRect(mattingTargetRect, mattingTargetOffset)) {

            mattingTargetRect.offset(-mattingTargetOffset.x, statusBarHeight - mattingTargetOffset.y);

            // get imageview animation
            detailImageAnim(
                    tappedRect,
                    detailImage,
                    trueImageRatio,
                    mattingTargetRect);

            // get animation for fast matte
            detailMattingAnim(
                    mattingLayout,
                    mattingTargetRect,
                    clickRawX,
                    clickRawY);

            detailToGalleryAnimator.storeRecentRect(tappedRect);
            backPressedRepo.addHandler(this);
        }
    }

    private void detailImageAnim(
            Rect tappedRect,
            final PatternDetailView detailImage,
            float trueImageRatio,
            Rect mattingTargetRect
    ) {
        final SVGItemViewModel svgItemViewModel = ((LayoutActivityGalleryBinding) DataBindingUtil.findBinding(detailImage)).getSvgItemViewModel();

        // Ratio of image starting width to screen width
        final float widthRatio = (float) tappedRect.width()
                / ((float) detailImage.getWidth());

        Log.d(getClass().getSimpleName(), "defining detail image anim");
        float galleryPadding = dipToPixels(displayMetrics, resources.getDimension(R.dimen.gallery_padding));
        detailImage.setTranslationX(galleryPadding + tappedRect.left - detailImage.getLeft());
        detailImage.setTranslationY(galleryPadding + tappedRect.top - detailImage.getTop());
        detailImage.setTranslationZ(-4);
        detailImage.setScaleX(widthRatio);
        detailImage.setScaleY(widthRatio);
        detailImage.setVisibility(View.VISIBLE);

        final Paint paint = new Paint() {{
            setAntiAlias(false);
            setStyle(Style.STROKE);
            setStrokeWidth(4f);
            setColor(Color.WHITE);
            setAlpha(255);
        }};


        detailImage.animate()
                .translationY(0)
                .translationX(0)
                .translationZ(0)
                .scaleX(1)
                .scaleY(1)
                .setListener(getNoFocusWhileAnimatingListener(detailImage))
                .setStartDelay(resources.getInteger(R.integer.duration_gallery_to_detail)
                        / 4)
                .setDuration(resources.getInteger(R.integer.duration_gallery_to_detail)
                        * 3 / 4)
                .setInterpolator(new LinearInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Log.d(getClass().getSimpleName(), "image animation ended");
                        super.onAnimationEnd(animation);
                        Canvas canvas = new Canvas(Bitmap.createBitmap((int) detailImage.getWidth(), (int) detailImage.getHeight(), Bitmap.Config.ARGB_8888));
                        canvas.save();
                        canvas.drawPath(svgItemViewModel.getPath(), paint);
                        canvas.translate(detailImage.getLeft(), detailImage.getTop());
                        detailImage.draw(canvas);
                        canvas.restore();

                    }

                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        Log.d(getClass().getSimpleName(), "image animation started");
                        Log.d(getClass().getSimpleName(), "Y: " + detailImage.getY());
                        Log.d(getClass().getSimpleName(), "Y trans: " + detailImage.getTranslationY());
                    }
                });
    }

    private void detailMattingAnim(
            final View mattingLayout,
            Rect targetRect,
            int clickRawX,
            int clickRawY
    ) {
        // matting anim init
        int mattingWidthScaleShift = mattingLayout.getWidth() / 2;
        int mattingHeightScaleShift = mattingLayout.getHeight() / 2;
        int offsetY = clickRawY - mattingHeightScaleShift;
        int offsetX = clickRawX - mattingWidthScaleShift;

        mattingLayout.setScaleX(0);
        mattingLayout.setScaleY(0);
        mattingLayout.setY(offsetY);
        mattingLayout.setX(offsetX);
        mattingLayout.setZ(resources.getInteger(R.integer.z_position_matting_start));
        mattingLayout.setAlpha(1);
        mattingLayout.setPivotX(clickRawX);
        mattingLayout.setPivotY(clickRawY);
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
                .setListener(getNoFocusWhileAnimatingListener(mattingLayout))
                .setInterpolator(new DecelerateInterpolator(2f));
    }

    public static int dipToPixels(
            DisplayMetrics metrics,
            float dipValue
    ) {
        // Converts DP to Pixels
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    @NonNull
    private static AnimatorListenerAdapter getNoFocusWhileAnimatingListener(
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
