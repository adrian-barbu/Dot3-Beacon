package com.dot3digital.ui.real;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.dot3digital.R;
import com.dot3digital.framework.D3Get;
import com.dot3digital.framework.D3PrefetchImage;
import com.dot3digital.framework.D3Set;
import com.dot3digital.framework.beaconing.BeaconService;
import com.dot3digital.framework.model.BaseModel;
import com.dot3digital.framework.model.EntryForViewCat;
import com.dot3digital.framework.model.Fleet;
import com.dot3digital.ui.real.control.dot3Dialog;
import com.dot3digital.ui.utils.CommonUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @description Splash Activity
 *
 * @author Stelian
 */
public class SplashActivity extends Activity {
    private static final int DELAY_DURATION = 1 * 1000;
    private static final int PERMISSION_REQUEST = 100;

    private static final boolean USE_PREFETCH = true;      // [2015.12.01][Stelian] Use prefetch?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        //[2015.12.01][Stelian] Add feature for pre-fetching
        if (!USE_PREFETCH) {
            View layoutPrefetch = (View) findViewById(R.id.layoutPrefetch);
            layoutPrefetch.setVisibility(View.GONE);
        }

        // Check Bluetooth and Network connectivity
        if (!CommonUtil.isBluetoothAvailable(this)) {
            dot3Dialog.showWarningDialog(this, dot3Dialog.DIALOG_REASON.NOT_ENABLED_BLUETOOTH);
            return;
        }

        if (!CommonUtil.isNetworkAvailable(this)) {
            dot3Dialog.showWarningDialog(this, dot3Dialog.DIALOG_REASON.NOT_CONNECTED_NETWORK);
            return;
        }

        // [2015.11.26][Stelian] Set user to firebase
        D3Set.registerAnonymousUser(this, new D3Set.IGet() {
            @Override
            public void handleFirebaseResponse(BaseModel data) {
                // Now, start to engine
                D3Get.getFleetArrayWithCompletion(mGetFleetsCallback);
            }
        });
    }

    /**
     * Define Get Fleet Array Callback
     */
    private D3Get.IGet mGetFleetsCallback = new D3Get.IGet() {

        @Override
        public void handleFirebaseResponse(ArrayList<BaseModel> fleetArray) {
            ArrayList<Fleet> fleets = new ArrayList<Fleet>();

            for (BaseModel fleet : fleetArray)
                fleets.add((Fleet) fleet);

            setBeaconRegions((ArrayList<Fleet>) fleets);

            if (USE_PREFETCH) { // If the app need to prefetch zones, then start it
                startPrefetchAllZones();
            }
            else { // Else, go to main activity
                goMainActivity();
            }
        }

        @Override
        public void handleFirebaseResponse(BaseModel fleet) {}
    };

    /**
     * Set Beacon Regions From Fleets
     *
     * @param fleets
     */
    private void setBeaconRegions(List<Fleet> fleets) {
        BeaconService.getInstance(this).setRegion(fleets);
    }

    /*****************************************************************/
    /************************** Prefetch Part ************************/
    /*****************************************************************/

    ArrayList<String> mImageUrls = new ArrayList<String>();     // Download Image Urls
    int mDownloadedCount = 0;

    /**
     * Start prefetch all zones
     */
    private void startPrefetchAllZones() {
        D3PrefetchImage.prefetchAllZoneImagesWithProgress(new D3PrefetchImage.IGet() {
            @Override
            public void onSuccess(ArrayList<String> imageUrls, ArrayList<BaseModel> datas) {
                mImageUrls.addAll(imageUrls);

                startPrefetchAllZoneEntries(datas); // Prefetch all zone entries
            }

            @Override
            public void onFailed(String reason) {
                goMainActivity();
            }
        });
    }

    /**
     * Start prefetch all zone entries
     */
    private void startPrefetchAllZoneEntries(ArrayList<BaseModel> zones) {
        D3PrefetchImage.prefetchAllZoneEntriesImagesWithProgress(zones, new D3PrefetchImage.IGet() {
            @Override
            public void onSuccess(ArrayList<String> imageUrls, ArrayList<BaseModel> datas) {
                mImageUrls.addAll(imageUrls);

                if (mImageUrls.size() > 0) { // start download image
                    startDownloadImage();
                } else {
                    goMainActivity();
                }
            }

            @Override
            public void onFailed(String reason) {
                if (mImageUrls.size() > 0) { // start download image
                    startDownloadImage();
                } else {
                    goMainActivity();
                }
            }
        });
    }

    /**
     * Start download image in background
     */
    private void startDownloadImage() {
        for (String imageUrl : mImageUrls) {
            ImageLoader.getInstance().loadImage(imageUrl, Shared.gPrefetchImageOption, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    processImageDownloadComplete();
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    processImageDownloadComplete();
                }
            });
        }
    }

    private void processImageDownloadComplete() {
        mDownloadedCount++;

        String text = String.format("[%d/%d]", mDownloadedCount, mImageUrls.size());
        TextView tvCount = (TextView) findViewById(R.id.tvCount);
        tvCount.setText(text);

        if (mDownloadedCount >= mImageUrls.size())
            goMainActivity();
    }

    /**
     * Go Main Activity
     */
    private void goMainActivity() {
        // Some Delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);

                finish();
            }
        }, DELAY_DURATION);
    }

}