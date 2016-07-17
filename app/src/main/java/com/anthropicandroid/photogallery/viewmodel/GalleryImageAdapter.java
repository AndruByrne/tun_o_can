package com.anthropicandroid.photogallery.viewmodel;

/*
 * Created by Andrew Brin on 7/13/2016.
 */

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.anthropicandroid.photogallery.injectionmodules.GalleryActivityComponent;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.anthropicandroid.photogallery.model.utils.Utils
        .decodeSampledBitmapAndReturnWithRatio;

public class GalleryImageAdapter {

    public static final String TAG = GalleryImageAdapter.class.getSimpleName();

    @BindingAdapter(value={"rawDimen","galleryItem"})
    public static void setImageIndex(
            final GalleryActivityComponent galleryActivityComponent,
            final ImageView imageView,
            final RawBitmapMeasurement rawBitmapMeasurement,
            final GalleryItem galleryItem) {
        int width = galleryItem.getWidth();

        // Set image parameters from assigned width
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, width * 3 / 5);
        imageView.setLayoutParams(layoutParams);

        imageView.setBackgroundColor(ContextCompat.getColor(
                imageView.getContext(),
                galleryItem.getColorResId()));
        // Here rxJava is necessary, as we seem to be on the main thread, /contra/ data binding docs
        galleryActivityComponent.getRepository()
                .getImage(galleryItem.getIndex())
                // scale bitmap
                .map(new Func1<com.anthropicandroid.photogallery.model.GalleryImage, Pair<Bitmap, Pair<Integer, Integer>>>() {
                    @Override
                    public Pair<Bitmap, Pair<Integer, Integer>> call(com.anthropicandroid.photogallery.model.GalleryImage galleryImage) {
                        byte[] image = galleryImage.getImage();
                        int itemWidth = galleryItem.getWidth();
                        return decodeSampledBitmapAndReturnWithRatio(
                                image,
                                itemWidth,
                                itemWidth);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Action1<Pair<Bitmap, Pair<Integer, Integer>>>() {
                            @Override
                            public void call(Pair<Bitmap, Pair<Integer, Integer>> pair) {
                                imageView.setImageBitmap(pair.first);
                                rawBitmapMeasurement.setRawWidth(pair.second.first);
                                rawBitmapMeasurement.setRawHeight(pair.second.second);
                            }
                        },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Log.d(TAG, "error retrieving gallery item bitmap: "
                                        + throwable.getMessage());
                                throwable.printStackTrace();
                            }
                        });
    }
}
