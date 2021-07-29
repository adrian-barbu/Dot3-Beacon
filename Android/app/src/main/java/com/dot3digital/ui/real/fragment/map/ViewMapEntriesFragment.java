package com.dot3digital.ui.real.fragment.map;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

import com.dot3digital.R;
import com.dot3digital.framework.D3Get;
import com.dot3digital.framework.model.BaseModel;
import com.dot3digital.framework.model.EntryForViewCat;
import com.dot3digital.framework.model.ViewCats;
import com.dot3digital.framework.model.ViewEntry;
import com.dot3digital.ui.real.Shared;
import com.dot3digital.ui.real.fragment.BaseFragment;
import com.dot3digital.ui.real.fragment.places.PlacesFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.ViewScaleType;
import com.nostra13.universalimageloader.core.imageaware.NonViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

/**
 * @description ViewEntry Place Entries Fragment
 *
 * @author      Stelian
 */

public class ViewMapEntriesFragment extends BaseFragment implements D3Get.IGet, OnMapReadyCallback
{
    // Constant
    private final int VIEW_MODE_KEW = 1000;             // Kew Region
    private final int VIEW_MODE_WAKEHURST = 1001;       // Wakehurst Region

    // UI Members
    View mRootView;         // Root ViewEntry

    TextView tvMapTitle, tvMapDescription;

    Button btnKew, btnWakehurst;  // Buttons

    // Variables
    String mViewKey;

    int mCallbackCount;     // Count of requested callbacks
    int mCompleteCount;     // Count of completed callbacks

    ArrayList<BaseModel> mEntriesForMap;
    ArrayList<String> mPinImageUrls;

    GoogleMap mMap;
    LatLngBounds.Builder mBoundsBuilder;
    LatLngBounds mMapBounds;

    int mCurrentViewMode;

    private LatLng KEW_REGION = new LatLng(51.478439, -0.297439);           // Kew Region
    private LatLng WAKEHURST_REGION = new LatLng(51.0674235,-0.0898324);     // Wakehurst Region

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_map_view, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewKey = getArguments().getString(PlacesFragment.PARAM_VIEW_KEY);

        // Set Title & Description
        tvMapTitle = (TextView) mRootView.findViewById(R.id.tvMapTitle);
        tvMapDescription = (TextView) mRootView.findViewById(R.id.tvMapDescription);

        // Set Action Button
        btnKew = (Button) mRootView.findViewById(R.id.btnKew);
        btnKew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewMode(VIEW_MODE_KEW);
            }
        });

        btnWakehurst = (Button) mRootView.findViewById(R.id.btnWakehurst);
        btnWakehurst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewMode(VIEW_MODE_WAKEHURST);
            }
        });

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapViewer);
        mapFragment.getMapAsync(this);
    }

    /**
     * Set View Mode
     *
     * @param viewMode
     */
    private void setViewMode(int viewMode) {
        if (mCurrentViewMode == viewMode)
            return;

        switch (viewMode) {
            case VIEW_MODE_KEW:
                btnKew.setBackgroundColor(getResources().getColor(R.color.map_switch_color));
                btnKew.setTextColor(getResources().getColor(android.R.color.white));

                btnWakehurst.setBackgroundColor(getResources().getColor(android.R.color.white));
                btnWakehurst.setTextColor(getResources().getColor(R.color.map_switch_color));

                moveCameraWithZoom(KEW_REGION, 14);
                break;

            case VIEW_MODE_WAKEHURST:
                btnKew.setBackgroundColor(getResources().getColor(android.R.color.white));
                btnKew.setTextColor(getResources().getColor(R.color.map_switch_color));

                btnWakehurst.setBackgroundColor(getResources().getColor(R.color.map_switch_color));
                btnWakehurst.setTextColor(getResources().getColor(android.R.color.white));

                moveCameraWithZoom(WAKEHURST_REGION, 14);
                break;
        }

        mCurrentViewMode = viewMode;
    }

    // interface D3Get.IGet
    @Override
    public void handleFirebaseResponse(BaseModel data) {
        // Set Title & Description
        if (data != null) {
            ViewEntry viewEntry = (ViewEntry) data;

            tvMapTitle.setText(viewEntry.getName());

            if (!viewEntry.getDescription().isEmpty())
                tvMapDescription.setText(viewEntry.getDescription());
            else
                tvMapDescription.setVisibility(View.GONE);

            initLayouts();

            // Now load map datas
            D3Get.getViewCatsArrayWithViewKey(new GetViewCatsArrayCallback(), mViewKey);
        } else {
            showLoadFailDialog();
        }
    }

    @Override
    public void handleFirebaseResponse(ArrayList<BaseModel> listValues) {}

    /**
     * Init Layout Before Data Loading
     *
     * Here, show the content layout and hide progress layout
     */
    protected void initLayouts() {
        View layoutLoading = (View) mRootView.findViewById(R.id.layoutLoading);
        View layoutContent = (View) mRootView.findViewById(R.id.layoutContent);

        if (layoutLoading != null)
            layoutLoading.setVisibility(View.GONE);

        if (layoutContent != null)
            layoutContent.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Now, start to get data
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                D3Get.getViewWithViewKey(ViewMapEntriesFragment.this, mViewKey);
            }
        }, 2000);
    }

    /**
     * Get View Cats Callback For Map Pin
     */
    private class GetViewCatsArrayCallback implements D3Get.IGet {

        @Override
        public void handleFirebaseResponse(ArrayList<BaseModel> datas) {
            if (datas == null || datas.size() == 0) {
                showToast(R.string.no_entries_for_map);
                return;
            }

            mCallbackCount = 0;
            mCompleteCount = 0;

            mEntriesForMap = new ArrayList<BaseModel>();
            mPinImageUrls = new ArrayList<String>();

            // Call api to get entries for each items
            for (BaseModel data : datas) {
                // Cast to ViewCats object
                if (data instanceof ViewCats) {
                    ViewCats vc = (ViewCats) data;

                    // Get cat key and pin image url
                    String catKey = vc.getNodeKey();
                    String pinImageUrl = vc.getImage();

                    // Call api to get entries
                    D3Get.getEntriesArrayWithCatKey(new GetEntriesCallback(catKey, pinImageUrl), catKey);

                    mCallbackCount++;
                }
            }
        }

        @Override
        public void handleFirebaseResponse(BaseModel data) {
            // no used
        }
    };

    /**
     * Get Entries with Cat Key
     *
     * Maybe one more of this callback will be created because each category has its callback.
     */
    private class GetEntriesCallback implements D3Get.IGet {

        private String mCatKey;
        private String mPinImageUrl;

        public GetEntriesCallback(String catKey, String pinImageUrl) {
            mCatKey = catKey;
            mPinImageUrl = pinImageUrl;
        }

        @Override
        public void handleFirebaseResponse(ArrayList<BaseModel> datas) {
            mCompleteCount++;

            for (BaseModel data : datas) {
                mEntriesForMap.add(data);
                mPinImageUrls.add(mPinImageUrl);
            }

            // if all of callbacks is called, then set bounds on map
            if (mCompleteCount == mCallbackCount)
                addMapEntries();
        }

        @Override
        public void handleFirebaseResponse(BaseModel data) {
            // no used
        }
    };

    /**
     * Add Map Entries
     */
    private void addMapEntries() {
        if (mEntriesForMap.size() == 0) {
            showToast(R.string.no_entries_for_map);
            return;
        }

        if (mBoundsBuilder == null)
            mBoundsBuilder = new LatLngBounds.Builder();

        // Add map marker
        for (int i = 0; i < mEntriesForMap.size(); i++) {
            final BaseModel entry = mEntriesForMap.get(i);
            final String pinImageUrl = mPinImageUrls.get(i);

            if (entry instanceof EntryForViewCat) {
                try {
                    EntryForViewCat evc = (EntryForViewCat) entry;

                    final double latitude, longitude;
                    latitude = Double.parseDouble(evc.getMapLatitude());
                    longitude = Double.parseDouble(evc.getMapLongitude());

                    addMapRegion(latitude, longitude);

                    loadPinImage(pinImageUrl);
                } catch (Exception e) {}
            }
        }

        // Set bound for camera
        //moveCameraWithPadding(100);

        setViewMode(VIEW_MODE_KEW);
    }

    /**
     * Load Pin Image From Network
     *
     * @param pinImageUrl
     */
    private void loadPinImage(String pinImageUrl) {
        // load pin image
        ImageLoader.getInstance().loadImage(pinImageUrl, Shared.gMapPinImageOption, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                for (int i = 0; i < mPinImageUrls.size(); i++) {
                    if (mPinImageUrls.get(i).equals(imageUri)) {
                        BaseModel entry = (BaseModel) mEntriesForMap.get(i);
                        addMapMarker((EntryForViewCat) entry, null);
                    }
                }
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                // Find all entries which have this pin image url
                for (int i = 0; i < mPinImageUrls.size(); i++) {
                    if (mPinImageUrls.get(i).equals(imageUri)) {
                        BaseModel entry = (BaseModel) mEntriesForMap.get(i);
                        addMapMarker((EntryForViewCat) entry, loadedImage);
                    }
                }
            }
        });
    }

    /**
     * Add map region with its latitude & longitude
     */
    private void addMapRegion(double latitude, double longitude) {
        final LatLng location = new LatLng(latitude, longitude);
        mBoundsBuilder.include(location);
    }

    /**
     * Add map marker based on entries for map
     */
    private void addMapMarker(final EntryForViewCat entry, Bitmap pinImage) {

        try {
            final double latitude, longitude;
            latitude = Double.parseDouble(entry.getMapLatitude());
            longitude = Double.parseDouble(entry.getMapLongitude());

            final LatLng location = new LatLng(latitude, longitude);

            mMap.addMarker(new MarkerOptions()
                    .position(location)
                    .title(entry.getHeadline())
                    .snippet(entry.getTeaserText())
                    .icon((pinImage != null) ? BitmapDescriptorFactory.fromBitmap(pinImage) :
                            BitmapDescriptorFactory.defaultMarker()));

        } catch (Exception e) {}
    }

    /**
     *  Move Camera With Bounding Areas
     *
     * @param padding
     */
    protected void moveCameraWithPadding(final int padding) {
        final View mapView = getChildFragmentManager().findFragmentById(R.id.mapViewer).getView();

        try {
            mMapBounds = mBoundsBuilder.build();
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mMapBounds, padding));
        } catch (Exception e) {
            if (mapView.getViewTreeObserver().isAlive()) {
                mapView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @SuppressWarnings("deprecation") // We use the new method when supported
                    @SuppressLint("NewApi") // We check which build version we are using.
                    @Override
                    public void onGlobalLayout() {
                        try {
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                                mapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            } else {
                                mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            }

                            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mMapBounds, padding));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    /**
     * Move Camera To Special Location
     */
    protected void moveCameraWithZoom(final LatLng center, final int zoomLevel) {
        final View mapView = getChildFragmentManager().findFragmentById(R.id.mapViewer).getView();
        final CameraUpdate zoom = CameraUpdateFactory.zoomTo(zoomLevel);

        try {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(center, zoomLevel));
        } catch (Exception e) {
            if (mapView.getViewTreeObserver().isAlive()) {
                mapView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @SuppressWarnings("deprecation") // We use the new method when supported
                    @SuppressLint("NewApi") // We check which build version we are using.
                    @Override
                    public void onGlobalLayout() {
                        try {
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                                mapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            } else {
                                mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            }

                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(center, zoomLevel));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });

            }
        }
    }
}