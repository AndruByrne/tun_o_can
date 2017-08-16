package com.anthropicandroid.patterngallery.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.anthropicandroid.patterngallery.R;
import com.anthropicandroid.patterngallery.databinding.LayoutGalleryImageBinding;
import com.anthropicandroid.patterngallery.entities.ui.SVGItemViewModel;


/**
 * Created by Andrew Brin on 6/17/2017.
 */
public class PatternView
        extends View {

    final Paint writePaint = new Paint() {{
        setAntiAlias(false);
        setStyle(Style.STROKE);
        setStrokeWidth(1f);
        setStrokeCap(Cap.BUTT);
        setStrokeJoin(Join.MITER);
        setColor(Color.argb(150, 0,255,0));
        setAlpha(255);
    }};
    private SVGItemViewModel viewModel;
    private Path viewPath = new Path();
    private Matrix viewMatrix = new Matrix();
    private RectF computedPathBounds = new RectF();
    private RectF viewRect = new RectF();

    public PatternView(Context context) {
        super(context);
        initialize(context);
    }

    public PatternView(
            Context context,
            AttributeSet attrs
    ) {
        super(context, attrs);
        initialize(context);
    }

    public PatternView(
            Context context,
            AttributeSet attrs,
            int defStyleAttr
    ) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    private void initialize(Context context) {
        addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View view) {
                viewModel = ((LayoutGalleryImageBinding) DataBindingUtil.findBinding(PatternView.this))
                        .getViewModel();

                int maxChildWidth = viewModel.getMaxChildWidth();
                int width = viewModel.getLastKnownWidth();
                int height = viewModel.getLastKnownHeight();
                float heightToWidthRatio;
                if (height == 0 || width == 0)
                    heightToWidthRatio = 1;
                else heightToWidthRatio = height / width;
                setBackgroundColor(ContextCompat.getColor(
                        view.getContext(),
                        R.color.colorLightMatteGray));
//                viewModel.getColorResId()));
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                        maxChildWidth,  // The horizontal dimension will always be the maximum allowed
                        (int) (maxChildWidth * heightToWidthRatio));  // The vertical dimension will be variable
                setLayoutParams(layoutParams);
            }

            @Override
            public void onViewDetachedFromWindow(View view) {

            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);

        int widthPadding = getWidth()/5;
        int heightPadding = getHeight()/5;

        viewMatrix.reset();
        viewPath.reset();

        viewPath.moveTo(0, 0);
        viewPath.rLineTo(10, 0);
        viewPath.rLineTo(5, 5);
        viewPath.rLineTo(5, -5);
        viewPath.rLineTo(10, 1);
        viewPath.rLineTo(0, 19);
        viewPath.rLineTo(-20, 0);
        viewPath.rLineTo(-4, -10);
        viewPath.rLineTo(-6, 0);
        viewPath.rLineTo(0, -10);
        viewPath.computeBounds(computedPathBounds, true);

        viewRect.set(
                (float) (widthPadding ),
                (float) (heightPadding),
                (float) (widthPadding * 4),
                (float) (heightPadding * 4));

        boolean b = viewMatrix.setRectToRect(
                computedPathBounds,
                viewRect,
                Matrix.ScaleToFit.FILL
        );

        Log.d(getClass().getSimpleName(), "able to set Rect? " + b);

        canvas.concat(viewMatrix);
        canvas.drawPath(viewPath, writePaint);

    }
}
