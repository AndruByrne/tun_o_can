package com.anthropicandroid.photogallery.viewmodel;

/*
 * Created by Andrew Brin on 7/13/2016.
 */

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.anthropicandroid.photogallery.injectionmodules.GalleryActivityComponent;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class GridItemAdapter {

    public static final String TAG = GridItemAdapter.class.getSimpleName();

    @BindingAdapter("imageIndex")
    public static void setImageIndex(
            GalleryActivityComponent galleryActivityComponent,
            final ImageView imageView,
            Integer imageIndex) {
        // Here rxJava is necessary, as we seem to be on the main thread, contra data binding docs
        galleryActivityComponent
                .getThumbnailRepository()
                .getThumbnail(imageIndex)
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
