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

    @BindingAdapter("galleryItem")
    public static void setImageIndex(
            final GalleryActivityComponent galleryActivityComponent,
            final ImageView imageView,
            final GalleryItem galleryItem) {
        int width = galleryItem.getWidth();

        // Set image parameters from assigned width
        final int imageBoundsHeight = width * 3 / 5;
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, imageBoundsHeight);
        imageView.setLayoutParams(layoutParams);

        imageView.setBackgroundColor(ContextCompat.getColor(
                imageView.getContext(),
                galleryItem.getColorResId()));
        // Here rxJava is necessary, as we seem to be on the main thread, /contra/ data binding docs
        galleryActivityComponent.getRepository()
                .getImage(galleryItem.getIndex())
                // scale bitmap
                .map(new Func1<com.anthropicandroid.photogallery.model.GalleryImage, GalleryImageHolder>() {
                    @Override
                    public GalleryImageHolder call(com.anthropicandroid.photogallery.model.GalleryImage galleryImage) {
                        byte[] image = galleryImage.getImage();
                        int itemWidth = galleryItem.getWidth();
                        Pair<Bitmap, Pair<Integer, Integer>> bitmapFields =
                                decodeSampledBitmapAndReturnWithRatio(
                                image,
                                itemWidth,
                                imageBoundsHeight);
                        return new GalleryImageHolder(bitmapFields.first, bitmapFields.second,
                                galleryImage.getDescription());
                    }
                })
                .doOnNext(new Action1<GalleryImageHolder>() {
                    @Override
                    public void call(GalleryImageHolder galleryImageHolder) {
                        RawBitmapMeasurement rawBitmapMeasurement = galleryItem
                                .getRawBitmapMeasurement();

                        galleryItem.setDescription(galleryImageHolder.description);
                        rawBitmapMeasurement.setRawWidth(galleryImageHolder.rawMeasurements.first);
                        rawBitmapMeasurement.setRawHeight(galleryImageHolder.rawMeasurements.second);

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Action1<GalleryImageHolder>() {
                            @Override
                            public void call(GalleryImageHolder galleryImageHolder) {
                                imageView.setImageBitmap(galleryImageHolder.image);
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

    private static class GalleryImageHolder{
        Bitmap image;
        Pair<Integer, Integer> rawMeasurements;
        String description;

        public GalleryImageHolder(
                Bitmap image,
                Pair<Integer, Integer> rawMeasurements,
                String description) {
            this.image = image;
            this.rawMeasurements = rawMeasurements;
            this.description = description;
        }
    }
}
