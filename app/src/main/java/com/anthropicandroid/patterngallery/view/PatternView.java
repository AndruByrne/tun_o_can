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

import com.anthropicandroid.patterngallery.databinding.LayoutGalleryImageBinding;
import com.anthropicandroid.patterngallery.entities.ui.SVGItemViewModel;


/**
 * Created by Andrew Brin on 6/17/2017.
 */
public class PatternView
        extends View {

    private SVGItemViewModel viewModel;
    private Path viewPath;
    private Matrix viewMatrix = new Matrix();
    private RectF computedPathBounds = new RectF();
    private RectF viewRect = new RectF();
    private Paint writePaint;

    public PatternView(Context context) {
        super(context);
        initialize();
    }

    public PatternView(
            Context context,
            AttributeSet attrs
    ) {
        super(context, attrs);
        initialize();
    }

    public PatternView(
            Context context,
            AttributeSet attrs,
            int defStyleAttr
    ) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(final View view) {
                final Context context = view.getContext();
                writePaint = new Paint() {{
                    setAntiAlias(false);
                    setStyle(Style.STROKE);
                    setStrokeWidth(36f);
                    setColor(Color.BLACK);
                    setAlpha(255);
                }};

                viewModel = ((LayoutGalleryImageBinding) DataBindingUtil.findBinding(PatternView.this))
                        .getViewModel();

                int maxChildWidth = viewModel.getMaxChildWidth();
                viewPath = viewModel.getPath();
                RectF rectF = new RectF();
                viewPath.computeBounds(rectF, true);
                float heightToWidthRatio;
                if (viewPath.isEmpty())
                    heightToWidthRatio = 1;
                else heightToWidthRatio = rectF.height() / rectF.width();
                setBackgroundColor(ContextCompat.getColor(
                        context,
                viewModel.getColorResId()));
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
