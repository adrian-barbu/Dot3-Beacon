package com.dot3digital.ui.real;

import com.dot3digital.R;
import com.dot3digital.framework.beaconing.BeaconService;
import com.dot3digital.ui.real.fragment.discover.DiscoverFragment;
import com.dot3digital.ui.real.fragment.home.HomeFragment;
import com.dot3digital.ui.real.fragment.map.MapFragment;
import com.dot3digital.ui.real.fragment.places.PlacesFragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

/**
 * @description     Main Activity
 *                  It include pager & toolbar
 *
 * @author          Stelian
 */
public class MainActivity extends FragmentActivity
{
    // Intent Params
    public final static String PARAM_BEACON_DISCOVERED = "BeaconDiscovered";
    public final static String PARAM_BEACON_CLEARED= "BeaconCleared";
    public final static String PARAM_REQUEST_FROM_NOTIFICATION = "RequestFromNotification";
    public final static String PARAM_ZONE_NAME = "ZoneName";
    public final static String PARAM_ZONE_KEYS = "ZoneKeys";

    // Constant
    public final int FRAGMENT_HOME = 10;
    public final int FRAGMENT_PLACES = 11;
    public final int FRAGMENT_DISCOVER = 12;
    public final int FRAGMENT_MAP = 13;

    // Variables
    private int mSelectedTab;
    private Fragment mSelectedFragment;

    // Discover Related Variables
    Bundle discoverBundle;

    // UI Member
    private Button btnHome, btnPlace, btnDiscover, btnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnHome = (Button) findViewById(R.id.btnHome);
        btnPlace = (Button) findViewById(R.id.btnPlace);
        btnDiscover = (Button) findViewById(R.id.btnDiscover);
        btnMap = (Button) findViewById(R.id.btnMap);

        openTab(FRAGMENT_HOME);

        // Start Beacon Service Monitoring
        startPrepareBeaconService();
    }

    private void startPrepareBeaconService() {
        // Create Discover Bundle
        discoverBundle = new Bundle();

        BeaconService.getInstance(this).setContext(this);
        BeaconService.getInstance(this).startBeaconService();
    }

    @Override
    protected void onResume() {
        super.onResume();

//        BeaconService.getInstance(this).onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

//        BeaconService.getInstance(this).onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        BeaconService.getInstance(this).onDestroy();
    }

    /**
     * Respond From Notification
     *
     * @param intent
     */
    @Override
    public void onNewIntent(Intent intent){
        super.onNewIntent(intent); // Do we need it? Anyway it's empty base implementation.

        Bundle extras = intent.getExtras();
        if(extras != null){
            boolean isBeaconDiscovered = extras.getBoolean(PARAM_BEACON_DISCOVERED);
            boolean isBeaconCleared = extras.getBoolean(PARAM_BEACON_CLEARED);
            if (isBeaconDiscovered) {
                String zoneKeys = extras.getString(PARAM_ZONE_KEYS);
                if (zoneKeys.isEmpty())
                    return;

                // Start Discover Tab With Param
                discoverBundle.putString(DiscoverFragment.PARAM_ZONE_KEYS, zoneKeys);

                // If intent is from notification bar, then open discover tab
                boolean needToOpen = extras.getBoolean(PARAM_REQUEST_FROM_NOTIFICATION);
                if (needToOpen)
                    openTab(FRAGMENT_DISCOVER);
            }
            else if (isBeaconCleared) {
                // Clear Bundle
                discoverBundle.remove(DiscoverFragment.PARAM_ZONE_KEYS);
                if (isOpenDiscoverTab())
                    openTab(FRAGMENT_DISCOVER);
            }
        }
    }

    /**
     * Get status whether user is openning Discover Tab
     *
     * @return
     */
    public boolean isOpenDiscoverTab() {
       return (mSelectedFragment != null && mSelectedFragment instanceof DiscoverFragment);
    }

    /**
     * Open Discover Tab Specially
     */
    private void openTab(int which) {
        switch (which) {
            case FRAGMENT_HOME:
                mSelectedTab = R.id.btnHome;
                break;

            case FRAGMENT_PLACES:
                mSelectedTab = R.id.btnPlace;
                break;

            case FRAGMENT_DISCOVER:
                mSelectedTab = R.id.btnDiscover;
                break;

            case FRAGMENT_MAP:
                mSelectedTab = R.id.btnMap;
                break;
        }

        addFragment(which);
        setToolbarButton();
    }

    /**
     * Add Sub Fragment
     *
     * @param which  : Fragment ID
     */
    private void addFragment(int which) {
        Fragment fragment = null;

        switch (which) {
            case FRAGMENT_HOME:
                fragment = new HomeFragment();
                break;

            case FRAGMENT_PLACES:
                fragment = new PlacesFragment();
                break;

            case FRAGMENT_DISCOVER:
//                param = new Bundle();
//                param.putString(DiscoverFragment.PARAM_ZONE_NAME, "Palm House");
//                param.putString(DiscoverFragment.PARAM_ZONE_KEYS, "-JugYkgrDYrhLKQQSziC");
                fragment = new DiscoverFragment();
                fragment.setArguments(discoverBundle);
                break;

            case FRAGMENT_MAP:
                fragment = new MapFragment();
                break;
        }

        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
            transaction.replace(R.id.container, fragment);
            transaction.commit();

            mSelectedFragment = fragment;
        }
    }

    /**
     * Toolbar Button Click Event Handler
     */
    public void onClick(View v) {
        if (mSelectedTab == v.getId())
            return;

        switch (v.getId()) {
            case R.id.btnHome:
                addFragment(FRAGMENT_HOME);
                break;

            case R.id.btnPlace:
                addFragment(FRAGMENT_PLACES);
                break;

            case R.id.btnDiscover:
                addFragment(FRAGMENT_DISCOVER);
                break;

            case R.id.btnMap:
                addFragment(FRAGMENT_MAP);
                break;
        }

        mSelectedTab = v.getId();

        setToolbarButton();
    }

    /**
     * Set Toolbar Button
     */
    private void setToolbarButton() {
        setButton(btnHome, R.mipmap.home_tab_icon, R.mipmap.home_tab_icon_selected);
        setButton(btnDiscover, R.mipmap.discover_tab_icon, R.mipmap.discover_tab_icon_selected);
        setButton(btnPlace, R.mipmap.places_icon, R.mipmap.places_icon_selected);
        setButton(btnMap, R.mipmap.map_tab_icon, R.mipmap.map_tab_icon_selected);
    }

    private void setButton(Button button, int normal_id, int select_id) {
        Drawable image;
        int color;

        if (mSelectedTab == button.getId())
        {
            image = getResources().getDrawable( select_id );
            color = getResources().getColor(R.color.toolbar_button_selected);
        }
        else
        {
            image = getResources().getDrawable( normal_id );
            color = getResources().getColor(R.color.toolbar_button_normal);
        }

        int h = image.getIntrinsicHeight();
        int w = image.getIntrinsicWidth();
        image.setBounds(0, 0, w, h);

        button.setCompoundDrawables(null, image, null, null);
        button.setTextColor(color);
    }

}