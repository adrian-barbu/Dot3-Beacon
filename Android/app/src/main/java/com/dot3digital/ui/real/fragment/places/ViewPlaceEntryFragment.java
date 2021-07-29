package com.dot3digital.ui.real.fragment.places;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dot3digital.R;
import com.dot3digital.framework.model.EntryForViewCat;
import com.dot3digital.ui.real.Shared;
import com.dot3digital.ui.real.fragment.BaseFragment;
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

/**
 * @description ViewEntry Place Entry Fragment
 *              This fragment include Map Viewer
 *
 * @author      Stelian
 */

public class ViewPlaceEntryFragment extends BaseFragment implements OnMapReadyCallback {
    // Constant
    private final int VIEW_MODE_INFORMATION = 1000; // Text ViewEntry Mode
    private final int VIEW_MODE_MAP = 1001;         // Map-Based Mode

    // Buttons
    View mRootView;         // Root ViewEntry

    ImageView ivBack;       // Back Button
    TextView ivTitle;       // Title
    TextView tvContent;     // Content
    ImageView ivImage;      // Image Viewer

    Button btnInformation, btnMap;  // Buttons
    View layoutInformation, mapViewer;

    // Variables
    EntryForViewCat mEntryForViewCat;

    GoogleMap mMap;                             // Map Object
    LatLngBounds.Builder mBoundsBuilder;        // Map Bounder
    SupportMapFragment mapFragment;             // Map Viewer

    int mCurrentViewMode;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_places_view_entry, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEntryForViewCat = getArguments().getParcelable("item");

        ivBack = (ImageView) mRootView.findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
                transaction.remove(ViewPlaceEntryFragment.this);
                transaction.commit();
            }
        });

        ivTitle = (TextView) mRootView.findViewById(R.id.ivTitle);
        ivTitle.setText(mEntryForViewCat.getHeadline());

        tvContent = (TextView) mRootView.findViewById(R.id.tvContent);
        tvContent.setText(mEntryForViewCat.getText());

        ivImage = (ImageView) mRootView.findViewById(R.id.ivImage);
        ImageLoader.getInstance().displayImage(mEntryForViewCat.getImage(), ivImage, Shared.gImageOption);

        // Set ViewEntry Mode Layer
        layoutInformation = (View) mRootView.findViewById(R.id.layoutInformation);
        mapViewer = (View) mRootView.findViewById(R.id.mapViewer);

        // Set Action Button
        btnInformation = (Button) mRootView.findViewById(R.id.btnInformation);
        btnInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewMode(VIEW_MODE_INFORMATION);
            }
        });

        btnMap = (Button) mRootView.findViewById(R.id.btnMap);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewMode(VIEW_MODE_MAP);
            }
        });

        setViewMode(VIEW_MODE_INFORMATION);

        // Init Google Map
        initMapView();
    }

    private void setViewMode(int viewMode) {
        if (mCurrentViewMode == viewMode)
            return;

        switch (viewMode) {
            case VIEW_MODE_INFORMATION:
                btnInformation.setBackgroundColor(getResources().getColor(R.color.place_header));
                btnMap.setBackgroundResource(R.drawable.place_view_type_button_selector);
                layoutInformation.setVisibility(View.VISIBLE);
                mapViewer.setVisibility(View.INVISIBLE);
                break;

            case VIEW_MODE_MAP:
                btnMap.setBackgroundColor(getResources().getColor(R.color.place_header));
                btnInformation.setBackgroundResource(R.drawable.place_view_type_button_selector);
                mapViewer.setVisibility(View.VISIBLE);
                layoutInformation.setVisibility(View.INVISIBLE);

                moveCameraWithZoom(15);
                break;
        }

        mCurrentViewMode = viewMode;
    }

    /**
     * Initialize Map Viewer
     */
    protected void initMapView() {
        mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapViewer));
        mapFragment.getMapAsync(this);

        mBoundsBuilder = new LatLngBounds.Builder();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        addMapPin();
    }

    /**
     * Add Map Pin
     */
    protected void addMapPin() {
        double latitude, longitude;
        try {
            latitude = Double.parseDouble(mEntryForViewCat.getMapLatitude());
            longitude = Double.parseDouble(mEntryForViewCat.getMapLongitude());
        } catch (Exception e) {
            showToast(R.string.error_add_map_info);
            return;
        }

        LatLng location = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions()
                .position(location)
                .title(mEntryForViewCat.getHeadline())
                .snippet(mEntryForViewCat.getAddress())
                .icon(BitmapDescriptorFactory.defaultMarker()));

        mBoundsBuilder.include(location);
    }

    /**
     * Move Camera To Special Location
     */
    protected void moveCameraWithZoom(int zoomLevel) {
        final View mapView = mapFragment.getView();
        final CameraUpdate zoom = CameraUpdateFactory.zoomTo(zoomLevel);

        LatLngBounds mapBounds = null;

        try {
            mapBounds = mBoundsBuilder.build();
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mapBounds, 0));
            mMap.animateCamera(zoom, 300, null);
        } catch (Exception e) {
            if (mapView.getViewTreeObserver().isAlive()) {
                final LatLngBounds finalMapBounds = mapBounds;
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

                            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(finalMapBounds, 0));
                            mMap.animateCamera(zoom, 300, null);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });

            }
        }
    }
}
