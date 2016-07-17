package com.anthropicandroid.photogallery.ViewModel;

/*
 * Created by Andrew Brin on 7/12/2016.
 */

import android.app.Activity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.anthropicandroid.photogallery.PhotoGalleryApplication;

final public class UserActionHandlers {

    public static final String TAG = UserActionHandlers.class.getSimpleName();

    public boolean aTouch(View view, MotionEvent motionEvent){
        //  check for databinding
        Log.d(TAG, "got clikc on onTouchbutton");
        Activity context = (Activity) view.getContext();
        PhotoGalleryApplication application = (PhotoGalleryApplication) context.getApplication();
        return true;
    }

}
