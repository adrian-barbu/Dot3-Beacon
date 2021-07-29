package com.dot3digital.ui.real.fragment.discover;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dot3digital.R;

/**
 * @description Discover Fragment
 *              This fragment has its several fragment such as ViewDiscoverDefaultCategoryFragment, ViewDiscoverDefaultEntryFragment
 *
 * @author      Stelian
 */

public class DiscoverFragment extends Fragment
{
    // Constant
    public final int FRAGMENT_DEFAULT_CATEGORY = 100;       // Default Category When no beacons is detected
    public final int FRAGMENT_ZONE_ENTRIES = 101;           // Entries When one of beacon is detected

    /**
     * Define Params
     */
    // Default ViewEntry Key When No Beacon is detected
    public final static String PARAM_VIEW_KEY = "viewKey";
    public final static String mDefaultViewKey = "-JubktoQL9VvhrK6FcmO";

    // Params For Beacon Detection
    public final static String PARAM_BEACON_DETECTED = "isBeaconDetected";
    public final static String PARAM_ZONE_NAME = "zoneName";
    public final static String PARAM_ZONE_KEYS = "zoneKeys";

    // UI Member
    View mRootView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_discover, container, false);
        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // If fragment has zone key, then call sub fragment for zone entries.
        // Otherwise, call default fragment

        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(PARAM_ZONE_KEYS)) {
            String zoneKeys = bundle.getString(PARAM_ZONE_KEYS);
            if (!zoneKeys.isEmpty()) {
                addFragment(FRAGMENT_ZONE_ENTRIES, bundle);
                return;
            }
        }

        // Otherwise, call default fragment
        Bundle defaultBundle = new Bundle();
        defaultBundle.putString(PARAM_VIEW_KEY, mDefaultViewKey);
        addFragment(FRAGMENT_DEFAULT_CATEGORY, defaultBundle);
    }

    /**
     * Add Sub Fragment
     *
     * @param which  : Fragment ID
     */
    private void addFragment(int which, Bundle argument) {
        Fragment fragment = null;

        Bundle bundle = null;

        switch (which) {
            case FRAGMENT_DEFAULT_CATEGORY:
                fragment = new ViewDiscoverDefaultCategoryFragment();
                break;

            case FRAGMENT_ZONE_ENTRIES:
                fragment = new ViewDiscoverEntriesFragment();
                break;
        }

        if (fragment != null) {
            if (argument != null)
                fragment.setArguments(argument);

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            //transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            transaction.replace(R.id.root_frame, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
