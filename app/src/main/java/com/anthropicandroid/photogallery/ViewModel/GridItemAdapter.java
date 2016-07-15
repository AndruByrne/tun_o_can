package com.anthropicandroid.photogallery.viewmodel;

/*
 * Created by Andrew Brin on 7/13/2016.
 */

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.anthropicandroid.photogallery.injectionmodules.GalleryActivityComponent;
import com.anthropicandroid.photogallery.model.Thumbnail;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.anthropicandroid.photogallery.model.utils.Utils.decodeSampledBitmap;
import static com.anthropicandroid.photogallery.model.utils.Utils.dipToPixels;

public class GridItemAdapter {

    public static final String TAG = GridItemAdapter.class.getSimpleName();

    @BindingAdapter("imageIndex")
    public static void setImageIndex(
            final GalleryActivityComponent galleryActivityComponent,
            final ImageView imageView,
            Integer imageIndex) {
        // Here rxJava is necessary, as we seem to be on the main thread, contra data binding docs
        galleryActivityComponent
                .getRepository()
                .getThumbnail(imageIndex)
                .map(new Func1<Thumbnail, Bitmap>() {
                    @Override
                    public Bitmap call(Thumbnail thumbnail) {
                        int widthAndHeight = galleryActivityComponent.getScreenWidth() / 2 -
                                dipToPixels(galleryActivityComponent.getDisplayMetrics(), 20);
                        return decodeSampledBitmap(
                                thumbnail.getThumbnail(),
                                widthAndHeight,
                                widthAndHeight);
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
