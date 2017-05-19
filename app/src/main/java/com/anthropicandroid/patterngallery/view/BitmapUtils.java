package com.anthropicandroid.patterngallery.view;

/*
 * Created by Andrew Brin on 7/14/2016.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.Pair;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class BitmapUtils {

    // The following static utils were snicked from
    // https://developer.android.com/training/displaying-bitmaps/load-bitmap.html
    // then tweaked to optimize for application
    public static int calculateInSampleSize(
            final int outWidth,
            final int outHeight,
            int reqWidth,
            int reqHeight
    ) {
        int inSampleSize = 1;

        if (outHeight > reqHeight || outWidth > reqWidth) {

            final int halfHeight = outHeight / 2;
            final int halfWidth  = outWidth / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Pair<Bitmap, Pair<Integer, Integer>> decodeSampledBitmapAndReturnWithRatio(
            byte[] bitmapBytes,
            int reqWidth,
            int reqHeight
    ) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length, options);

        // Raw height and width of image
        final int width  = options.outWidth;
        final int height = options.outHeight;

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(width, height, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return new Pair<>(
                BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length, options),
                new Pair<>(width, height));
    }

    public static Bitmap decodeSampledBitmapWithPredicate(
            byte[] bitmapBytes,
            final int knownWidth,
            final int knownHeight,
            int reqWidth,
            int reqHeight
    ) {

        final BitmapFactory.Options options = new BitmapFactory.Options();

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(knownWidth, knownHeight, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length, options);
    }

    public static int dipToPixels(
            DisplayMetrics metrics,
            float dipValue
    ) {
        // Converts DP to Pixels
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }
}
