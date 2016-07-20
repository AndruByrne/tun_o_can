package com.anthropicandroid.photogallery.viewmodel.animation;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/*
 * Created by Andrew Brin on 7/19/2016.
 */
public class DetailDragBehavior extends CoordinatorLayout.Behavior<ImageView> {

    public DetailDragBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //
    @Override
    public boolean onTouchEvent(CoordinatorLayout parent, ImageView child, MotionEvent ev) {
        if(child.getVisibility()!= View.VISIBLE) return false;
        child.setTranslationX(ev.getRawX() - child.getWidth() / 2);
        child.setTranslationY(ev.getRawY() - child.getHeight() / 2);
        return true;
    }

}
