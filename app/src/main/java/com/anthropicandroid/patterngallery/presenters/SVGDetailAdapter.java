package com.anthropicandroid.patterngallery.presenters;

/*
 * Created by Andrew Brin on 7/17/2016.
 */

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.anthropicandroid.patterngallery.R;
import com.anthropicandroid.patterngallery.databinding.LayoutActivityGalleryBinding;
import com.anthropicandroid.patterngallery.entities.interactions.PatternMetaData;
import com.anthropicandroid.patterngallery.entities.ui.SVGItemViewModel;
import com.anthropicandroid.patterngallery.routers.gallery.GalleryActivityComponent;
import com.anthropicandroid.patterngallery.entities.ui.RawBitmapMeasurement;
import com.anthropicandroid.patterngallery.view.PatternDetailView;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

public class SVGDetailAdapter {

    public static final String TAG = SVGDetailAdapter.class.getSimpleName();

    @BindingAdapter("svgDetailPath")
    public static void setSVGDetailPath(
            final GalleryActivityComponent galleryActivityComponent,
            final PatternDetailView imageView,
            final Path path
    ) {
        Paint paint = new Paint() {{
            setAntiAlias(false);
            setStyle(Style.STROKE);
            setStrokeWidth(4f);
            setColor(Color.WHITE);
            setAlpha(255);
        }};

        //  Keeping this RectF instantiation in the method, as it would be a static field
        RectF pathBounds = new RectF();
        CoordinatorLayout parent = (CoordinatorLayout) imageView.getParent();
        LayoutActivityGalleryBinding binding = (LayoutActivityGalleryBinding) DataBindingUtil.findBinding(parent);
        ImageView chordOutline = binding.chordOutline;
        AppBarLayout rootAppBarLayout = binding.rootAppBarLayout;

        path.computeBounds(pathBounds, false);

        int targetWidth = parent.getWidth() / 3;
        float targetHeight = targetWidth * pathBounds.height() / pathBounds.width();
        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(
                targetWidth,
                (int) targetHeight);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.setMargins(200, rootAppBarLayout.getBottom() + chordOutline.getTop(), 200, 200);
        imageView.setLayoutParams(params);

        imageView.setPath(path);

//        dd
//        if (!pathBounds.isEmpty()) {
//            Canvas canvas = new Canvas(Bitmap.createBitmap((int) pathBounds.width(), (int) pathBounds.height(), Bitmap.Config.ARGB_8888));
//            canvas.save();
//            canvas.drawPath(path, paint);
//            canvas.translate(imageView.getLeft(), imageView.getTop());
//            imageView.draw(canvas);
//            canvas.restore();
//        }
    }
}
