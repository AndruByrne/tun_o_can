package com.anthropicandroid.patterngallery.presenters;

import android.app.Application;
import android.databinding.DataBindingUtil;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.v4.view.VelocityTrackerCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.anthropicandroid.patterngallery.databinding.LayoutActivityGalleryBinding;
import com.anthropicandroid.patterngallery.entities.ui.SVGGalleryViewModel;
import com.anthropicandroid.patterngallery.view.PatternDetailView;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static java.lang.Math.*;

/*
 * Created by Andrew Brin on 7/17/2016.
 */
public class BottomNavActionHandlers
{

    private VelocityTracker sliderVelocityTracker;

    public BottomNavActionHandlers(Application context) {

    }

    public void writePatternToDevice(View view) {
        LayoutActivityGalleryBinding binding     = DataBindingUtil.findBinding(view);
        final PatternDetailView      svgDetail   = binding.svgDetail;
        final Button                 abscissaBox = binding.abscissaBox;
        final Button                 ordinalBox  = binding.ordinalBox;

        Observable
                .just(binding.getDetailPath())
                .filter(new Func1<Path, Boolean>()
                {
                    @Override
                    public Boolean call(Path path) {
                        boolean empty = path.isEmpty();

                        return !empty;
                    }
                })
                .flatMap(new Func1<Path, Observable<float[]>>()
                {
                    @Override
                    public Observable<float[]> call(Path path) {
                        final PathMeasure pathMeasure = new PathMeasure(path, false);
                        final float       length      = pathMeasure.getLength();

                        return Observable
                                .interval(100, TimeUnit.MILLISECONDS)
                                .takeUntil(new Func1<Long, Boolean>()
                                {
                                    @Override
                                    public Boolean call(Long count) {
                                        return count > length;
                                    }
                                })
                                .map(new Func1<Long, float[]>()
                                {
                                    @Override
                                    public float[] call(Long distance) {
                                        float[] pos = new float[2];
                                        float[] tan = new float[2];
                                        pathMeasure.getPosTan(
                                                distance,
                                                pos,
                                                tan);
                                        return pos;
                                    }
                                });
                    }
                })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<float[]>()
                {
                    @Override
                    public void onCompleted() {
                        Log.i(getClass().getSimpleName(), "write completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(
                                getClass().getSimpleName(),
                                "error while writing pattern: " + e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(float[] floats) {
                        float  xFloat = floats[0];
                        float  yFloat = floats[1];
                        double radial = hypot(xFloat, yFloat);
                        //  This is not the final number we want; it is the degree turn relative to the center of the object, not the machine
                        double degreesFromOrigin = toDegrees(acos(xFloat / radial));

                        abscissaBox.setText(
                                new BigDecimal(radial)
                                        .setScale(
                                                2,
                                                BigDecimal.ROUND_DOWN)
                                        .toString());
                        ordinalBox.setText(
                                new BigDecimal(
                                        0 < yFloat ?
                                                degreesFromOrigin :
                                                360 - degreesFromOrigin)
                                        .setScale(
                                                2,
                                                BigDecimal.ROUND_DOWN)
                                        .toString());
                        svgDetail.showSparkAt(xFloat, yFloat);
                    }
                });
    }

    public void selectTool(View view) {
        Toast.makeText(view.getContext(), "tool selection not yet implemented", Toast.LENGTH_SHORT)
             .show();
    }

    public void reverseDirection(View view) {
        Toast.makeText(
                view.getContext(),
                "direction reversal not yet implemented",
                Toast.LENGTH_SHORT)
             .show();
    }

    public boolean valueIncreaser(
            View view,
            MotionEvent motionEvent
    ) {
        LayoutActivityGalleryBinding binding                  = DataBindingUtil.findBinding(view);
        SVGGalleryViewModel          svgGalleryViewModel      = binding.getSvgGalleryViewModel();
        int                          unitOfScreenWidthTouched = (int) (5 * motionEvent.getX() / binding.galleryGrid.getWidth() % 5) + 1;
        int                          action                   = motionEvent.getActionMasked();
        int                          actionIndex              = motionEvent.getActionIndex();
        int                          pointerId                = motionEvent.getPointerId(actionIndex);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                binding.sliderControlLayout.setVisibility(View.VISIBLE);
                if (sliderVelocityTracker == null) sliderVelocityTracker = VelocityTracker.obtain();
                else sliderVelocityTracker.clear();
                sliderVelocityTracker.addMovement(motionEvent);
                return true;
            case MotionEvent.ACTION_UP:
                binding.sliderControlLayout.setVisibility(View.GONE);
                BigDecimal sliderValue = svgGalleryViewModel.clearSliderValue();
                if (unitOfScreenWidthTouched > 3) svgGalleryViewModel
                        .setOrdinalValue(sliderValue.toPlainString());
                return true;
            case MotionEvent.ACTION_CANCEL:
                binding.sliderControlLayout.setVisibility(View.GONE);
                sliderVelocityTracker.recycle();
                return true;
            case MotionEvent.ACTION_MOVE:
                // Continue to track movement regardless of X axis position
                sliderVelocityTracker.addMovement(motionEvent);
                if (unitOfScreenWidthTouched < 4) {
                    sliderVelocityTracker.computeCurrentVelocity(
                            1000,
                            SVGGalleryViewModel.SLIDER_MAX_VELOCITY);
                    int historySize = motionEvent.getHistorySize();
                    svgGalleryViewModel.setSliderValue(
                            VelocityTrackerCompat.getYVelocity(
                                    sliderVelocityTracker,
                                    pointerId),
                            (historySize < 1 ? 0 : motionEvent.getHistoricalY(historySize - 1)),
                            motionEvent.getY());
                }
                return true;
            default:
                return false;
        }
    }
}
