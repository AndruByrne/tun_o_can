package com.anthropicandroid.patterngallery.view.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.anthropicandroid.patterngallery.R;
import com.anthropicandroid.patterngallery.databinding.LayoutActivityGalleryBinding;

/*
 * Created by Andrew Brin on 7/19/2016.
 */
public class DetailToGalleryAnimator {

    private Rect currentRect;
    private Resources resources;
    private int imageViewHeight;
    private int statusBarHeight;
    private int screenWidth;

    public DetailToGalleryAnimator(
            Resources resources,
            int statusBarHeight,
            int imageViewHeight,
            int screenWidth
    ) {
        this.resources = resources;
        this.imageViewHeight = imageViewHeight;
        this.statusBarHeight = statusBarHeight;
        this.screenWidth = screenWidth;
    }

    public void recentRect(Rect currentRect) { this.currentRect = currentRect; }

    public boolean returnToGallery(
            LayoutActivityGalleryBinding binding
    ) {
        if (currentRect == null || binding == null)
        {
            Log.w(getClass().getSimpleName(), "unexpected state while returning to gallery");
            return false;
        }

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(getImageAnim(binding.alphaDetailImageView, currentRect))
                .with(getMattingAnim(binding.alphaDetailMattingLayout, currentRect));
        animatorSet.setDuration(resources.getInteger(R.integer.duration_detail_to_gallery_animator));
        animatorSet.addListener(getActionListener(binding));
        animatorSet.start();
        binding.alphaDetailImageView.setImageBitmap(null);
        return true;
    }

    private AnimatorSet getMattingAnim(
            FrameLayout frameLayout,
            Rect currentRect
    ) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet
                .play(ObjectAnimator.ofFloat(
                        frameLayout,
                        View.Z,
                        resources.getInteger(R.integer.z_position_matting_end),
                        resources.getInteger(R.integer.z_position_matting_start)))
                .with(ObjectAnimator.ofFloat(
                        frameLayout,
                        View.X,
                        0,
                        currentRect.left - screenWidth / 2))
                .with(ObjectAnimator.ofFloat(
                        frameLayout,
                        View.Y,
                        statusBarHeight,
                        currentRect.top + currentRect.height() - imageViewHeight / 2))
                .with(ObjectAnimator.ofFloat(frameLayout, View.SCALE_X, 0))
                .with(ObjectAnimator.ofFloat(frameLayout, View.SCALE_Y, 0));
        return animatorSet;
    }

    private AnimatorSet getImageAnim(
            ImageView imageView,
            Rect currentRect
    ) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet
                .play(ObjectAnimator.ofFloat(imageView, View.Z, 12, 8))
                .with(ObjectAnimator.ofFloat(
                        imageView,
                        View.X,
                        0,
                        currentRect.left - screenWidth / 2))
                .with(ObjectAnimator.ofFloat(
                        imageView,
                        View.Y,
                        statusBarHeight,
                        currentRect.top + currentRect.height() - imageViewHeight / 2))
                .with(ObjectAnimator.ofFloat(imageView, View.SCALE_X, 0))
                .with(ObjectAnimator.ofFloat(imageView, View.SCALE_Y, 0));
        animatorSet.setInterpolator(new LinearInterpolator());
        return animatorSet;
    }

    @NonNull
    private AnimatorListenerAdapter getActionListener(
            final LayoutActivityGalleryBinding binding
    ) {
        return new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                binding.alphaDetailMattingLayout.setVisibility(View.GONE);
                binding.alphaDetailImageView.setVisibility(View.GONE);
                binding.alphaDetailImageView.setImageBitmap(null);
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        };
    }

}
