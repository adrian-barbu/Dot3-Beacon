package com.dot3digital.ui.real;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.dot3digital.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;

/**
 * @description     Shared Data
 *
 * @author          Stelian
 */
public class Shared {
    public static DisplayImageOptions gImageOption = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.mipmap.ic_kew_small)
            .showImageForEmptyUri(R.mipmap.ic_kew_small)
            .showImageOnFail(R.mipmap.ic_kew_small)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .displayer(new FadeInBitmapDisplayer(500))
            .build();;

    /**
     * Map Pin Option
     */
    public static BitmapFactory.Options resizeOptions = new BitmapFactory.Options();

    static {
        resizeOptions.inSampleSize = 3; // decrease size 3 times
        resizeOptions.inScaled = true;
    }

    public static DisplayImageOptions gMapPinImageOption = new DisplayImageOptions.Builder()
        .cacheInMemory(true)
        .cacheOnDisk(true)
        .displayer(new FadeInBitmapDisplayer(300))
        .postProcessor(new BitmapProcessor() {
                @Override
                public Bitmap process(Bitmap bmp) {
                        return Bitmap.createScaledBitmap(bmp, 100, 80, false);
                }
        })
        .build();

    public static DisplayImageOptions gPrefetchImageOption = new DisplayImageOptions.Builder()
            .cacheOnDisk(true)
            .build();
}
