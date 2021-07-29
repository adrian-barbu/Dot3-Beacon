package com.dot3digital.ui.real.fragment.discover;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dot3digital.R;
import com.dot3digital.framework.D3Get;
import com.dot3digital.framework.model.BaseModel;
import com.dot3digital.framework.model.Zone;
import com.dot3digital.ui.real.adapter.DiscoverEntryPagerAdapter;
import com.dot3digital.ui.real.control.CirclePageIndicator;
import com.dot3digital.ui.real.fragment.BaseFragment;

import java.util.ArrayList;

/**
 * @description Discovered Zone Entries Fragment
 *              This fragment will be called when beacon is detected
 *
 * @author      Stelian
 */

public class ViewDiscoverEntriesFragmentOld extends BaseFragment implements D3Get.IGet
{
    // Variables
    ViewPager pagerEntries;                     // ViewEntry Pager For Entries
    CirclePageIndicator pagerIndicator;         // Pager Indicator
    DiscoverEntryPagerAdapter mPagerAdapter;    // Pager Adapter

    // UI Members
    View mRootView;                             // Root ViewEntry

    TextView tvZoneName;

    // Variables
    String mZoneKey;                            // ZoneKey

    ArrayList<BaseModel> mEntries;              // Entries

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_discover_view_with_zone_old, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mZoneKey = getArguments().getString(DiscoverFragment.PARAM_ZONE_KEYS);

        tvZoneName = (TextView) mRootView.findViewById(R.id.tvZoneName);

        // Now, start to get data
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                D3Get.getZoneWithZoneKey(ViewDiscoverEntriesFragmentOld.this, mZoneKey);

            }
        }, 2000);
    }

    // interface D3Get.IGet
    @Override
    public void handleFirebaseResponse(BaseModel value) {
        // Set Title & Description
        if (value != null) {
            Zone zone = (Zone) value;
            tvZoneName.setText(zone.getName());

            // Now load zone entries data
            D3Get.getEntriesArrayWithZoneKey(ViewDiscoverEntriesFragmentOld.this, mZoneKey);
        } else {
            showLoadFailDialog();
        }
    }

    // interface D3Get.IGet
    @Override
    public void handleFirebaseResponse(ArrayList<BaseModel> listValues) {
        if (listValues == null) {
            showLoadFailDialog();
            return;
        }

        mEntries = listValues;

        initLayouts();
        initPager();
    }

    protected void initPager() {
        // Set Pager
        mPagerAdapter = new DiscoverEntryPagerAdapter(getActivity(), 0);
        mPagerAdapter.setData(mEntries);
        mPagerAdapter.setOnItemChangedListener(new DiscoverEntryPagerAdapter.OnItemChangedListener() {
            @Override
            public void onItemChanged(int position) {
                // Switch ViewEntry Mode
                mPagerAdapter.switchMode();
                pagerEntries.setAdapter(mPagerAdapter);
                pagerEntries.setCurrentItem(position);
            }

            @Override
            public void onItemToggled(int position, boolean isUp) {

            }

            @Override
            public void onRequestContentShow(int position, boolean show) {

            }
        });

        pagerEntries = (ViewPager) mRootView.findViewById(R.id.pagerEntries);
        pagerEntries.setAdapter(mPagerAdapter);

        pagerIndicator = (CirclePageIndicator) mRootView.findViewById(R.id.pagerIndicator);
        pagerIndicator.setViewPager(pagerEntries);
    }

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
}
