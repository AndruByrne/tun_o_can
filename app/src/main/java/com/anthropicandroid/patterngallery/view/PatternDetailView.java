package com.anthropicandroid.patterngallery.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.anthropicandroid.patterngallery.databinding.LayoutActivityGalleryBinding;
import com.anthropicandroid.patterngallery.entities.ui.SVGItemViewModel;


/**
 * Created by Andrew Brin on 6/17/2017.
 */
public class PatternDetailView
        extends View {

    private SVGItemViewModel viewModel;
    private Path viewPath;
    private Matrix viewMatrix = new Matrix();
    private RectF computedPathBounds = new RectF();
    private RectF viewRect = new RectF();
    private Paint writePaint;

    public PatternDetailView(Context context) {
        super(context);
        initialize();
    }

    public PatternDetailView(
            Context context,
            AttributeSet attrs
    ) {
        super(context, attrs);
        initialize();
    }

    public PatternDetailView(
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
                writePaint = new Paint() {{
                    setAntiAlias(false);
                    setStyle(Style.STROKE);
                    setStrokeWidth(36f);
                    setColor(Color.WHITE);
                    setAlpha(255);
                }};

                viewModel = ((LayoutActivityGalleryBinding) DataBindingUtil.findBinding(PatternDetailView.this))
                        .getSvgItemViewModel();

                viewPath = viewModel.getPath();
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

        int widthPadding = getWidth() / 5;
        int heightPadding = getHeight() / 5;

        viewMatrix.reset();
        viewPath.computeBounds(computedPathBounds, true);

        viewRect.set(
                (float) (widthPadding),
                (float) (heightPadding),
                (float) (widthPadding * 4),
                (float) (heightPadding * 4));

        boolean b = viewMatrix.setRectToRect(
                computedPathBounds,
                viewRect,
                Matrix.ScaleToFit.FILL
        );

        canvas.concat(viewMatrix);
        canvas.drawPath(viewPath, writePaint);
    }

    public void setPath(Path path) {
        this.viewPath = path;
    }
}
