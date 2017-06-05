package com.anthropicandroid.patterngallery.presenters;

/*
 * Created by Andrew Brin on 7/17/2016.
 */

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.anthropicandroid.patterngallery.entities.framework.PatternMetaData;
import com.anthropicandroid.patterngallery.entities.ui.SVGDetailViewModel;
import com.anthropicandroid.patterngallery.routers.gallery.GalleryActivityComponent;
import com.anthropicandroid.patterngallery.view.BitmapUtils;
import com.anthropicandroid.patterngallery.entities.ui.RawBitmapMeasurement;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

public class SVGDetailAdapter {

    public static final String TAG = SVGDetailAdapter.class.getSimpleName();

    @BindingAdapter("svgDetailViewModel")
    public static void setSVGDetailViewModel(
            final GalleryActivityComponent galleryActivityComponent,
            final ImageView imageView,
            final SVGDetailViewModel svgDetailViewModel
    ) {

        final RawBitmapMeasurement rawBitmapMeasurement = svgDetailViewModel.getRawBitmapMeasurement();

        // pipe for image data
        galleryActivityComponent.getRepository()
                .getImage(svgDetailViewModel.getDetailIndex())
                // scale
                .map(new Func1<PatternMetaData, Bitmap>() {
                    @Override
                    public Bitmap call(PatternMetaData patternMetaData) {
                        // Should null-check this, because user may click on image placeholder
                        imageView.setContentDescription(patternMetaData.getDescription());
                        return BitmapUtils.decodeSampledBitmapWithPredicate(
                                patternMetaData.getImage(),
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
