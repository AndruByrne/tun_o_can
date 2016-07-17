package com.anthropicandroid.photogallery.viewmodelz;

/*
 * Created by Andrew Brin on 7/13/2016.
 */

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.anthropicandroid.photogallery.injectionmodulz.GalleryActivityComponent;
import com.anthropicandroid.photogallery.model.GalleryImage;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.anthropicandroid.photogallery.model.utils.Utils.decodeSampledBitmap;

public class GalleryImageAdapter {

    public static final String TAG = GalleryImageAdapter.class.getSimpleName();

    @BindingAdapter("galleryItem")
    public static void setImageIndex(
            final GalleryActivityComponent galleryActivityComponent,
            final ImageView imageView,
            final GalleryItem galleryItem) {
        int width = galleryItem.getWidth();

        // Set image parameters from assigned width
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, width*3/5);
        imageView.setLayoutParams(layoutParams);

        // Here rxJava is necessary, as we seem to be on the main thread, /contra/ data binding docs
        imageView.setBackgroundColor(ContextCompat.getColor(
                imageView.getContext(),
                galleryItem.getColorResId()));
        galleryActivityComponent.getRepository()
                .getImage(galleryItem.getIndex())
                // scale bitmap
                .map(new Func1<GalleryImage, Bitmap>() {
                    @Override
                    public Bitmap call(GalleryImage galleryImage) {
                        byte[] image = galleryImage.getImage();
                        int itemWidth = galleryItem.getWidth();
                        return decodeSampledBitmap(
                                image,
                                itemWidth,
                                itemWidth);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Action1<Bitmap>() {
                            @Override
                            public void call(Bitmap bitmap) {
                                imageView.setImageBitmap(bitmap);
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
