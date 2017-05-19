package com.anthropicandroid.patterngallery.presenters;

/*
 * Created by Andrew Brin on 7/17/2016.
 */

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.anthropicandroid.patterngallery.routers.gallery.GalleryActivityComponent;
import com.anthropicandroid.patterngallery.entities.framework.GalleryImage;
import com.anthropicandroid.patterngallery.view.BitmapUtils;
import com.anthropicandroid.patterngallery.entities.ui.RawBitmapMeasurement;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

public class DetailImageAdapter {

    public static final String TAG = DetailImageAdapter.class.getSimpleName();

    @BindingAdapter(value = {
            "detailImage",
            "rawBitmapDimen"})
    public static void setDetailImage(
            final GalleryActivityComponent galleryActivityComponent,
            final ImageView imageView,
            final Integer detailIndex,
            final RawBitmapMeasurement rawBitmapMeasurement
    ) {

        // pipe for image data
        galleryActivityComponent.getRepository()
                .getImage(detailIndex)
                // scale
                .map(new Func1<GalleryImage, Bitmap>() {
                    @Override
                    public Bitmap call(GalleryImage galleryImage) {
                        // Should null-check this, because user may click on image placeholder
                        imageView.setContentDescription(galleryImage.getDescription());
                        return BitmapUtils.decodeSampledBitmapWithPredicate(
                                galleryImage.getImage(),
                                rawBitmapMeasurement.getRawWidth(),
                                rawBitmapMeasurement.getRawWidth(),
                                galleryActivityComponent.getScreenWidthInPx(),
                                galleryActivityComponent.getDetailHeightInPx()
                        );
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
                                Log.e(
                                        TAG,
                                        "error populating detail view: " + throwable.getMessage());
                                throwable.printStackTrace();
                            }
                        }
                );
    }
}
