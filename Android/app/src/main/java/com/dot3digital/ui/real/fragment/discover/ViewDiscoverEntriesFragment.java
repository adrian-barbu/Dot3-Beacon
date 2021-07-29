package com.dot3digital.ui.real.fragment.discover;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

public class ViewDiscoverEntriesFragment extends BaseFragment implements D3Get.IGet
{
    // Variables
    ViewPager pagerEntries;                     // ViewEntry Pager For Entries
    CirclePageIndicator pagerIndicator;         // Pager Indicator
    DiscoverEntryPagerAdapter mPagerAdapter;    // Pager Adapter

    // UI Members
    View mRootView;                             // Root ViewEntry
    View layoutHeader;

    TextView tvZoneName;

    // Variables
    String mZoneKey;                            // ZoneKey

    ArrayList<BaseModel> mEntries;              // Entries

    Animation mFadeInAnim, mFadeOutAnim;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_discover_view_with_zone, container, false);
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
                D3Get.getZoneWithZoneKey(ViewDiscoverEntriesFragment.this, mZoneKey);

            }
        }, 2000);

        mFadeInAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
        mFadeOutAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
    }

    // interface D3Get.IGet
    @Override
    public void handleFirebaseResponse(BaseModel value) {
        // Set Title & Description
        if (value != null) {
            Zone zone = (Zone) value;
            tvZoneName.setText(zone.getName());

            // Now load zone entries data
            D3Get.getEntriesArrayWithZoneKey(ViewDiscoverEntriesFragment.this, mZoneKey);
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

        // Init pager
        layoutHeader = (View) mRootView.findViewById(R.id.layoutHeader);
        layoutHeader.post(new Runnable() {
            @Override
            public void run() {
                // Get Header's Height
                int headerHeight = layoutHeader.getHeight();
                initPager(headerHeight);
            }
        });
    }

    protected void initPager(int headerHeight) {

        // Set Pager
        mPagerAdapter = new DiscoverEntryPagerAdapter(getActivity(), headerHeight);
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
                layoutHeader.setBackgroundColor((isUp) ? getResources().getColor(R.color.discover_header) :
                                                         getResources().getColor(R.color.discover_header_transparent));
            }

            @Override
            public void onRequestContentShow(int position, boolean show) {
                if (show)
                {
                    // Hide
                    mFadeOutAnim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {}
                        @Override
                        public void onAnimationRepeat(Animation animation) {}

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            layoutHeader.setVisibility(View.INVISIBLE);
                        }
                    });
                    layoutHeader.startAnimation(mFadeOutAnim);
                }
                else
                {
                    // Show
                    mFadeInAnim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {}
                        @Override
                        public void onAnimationRepeat(Animation animation) {}

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            layoutHeader.setVisibility(View.VISIBLE);
                        }
                    });
                    layoutHeader.startAnimation(mFadeInAnim);
                }
            }
        });

        pagerEntries = (ViewPager) mRootView.findViewById(R.id.pagerEntries);
        pagerEntries.setAdapter(mPagerAdapter);
        pagerEntries.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                View view = (View) pagerEntries.findViewWithTag(position);
                mPagerAdapter.setInitialYPosition(view);

                // Set Header
                layoutHeader.setBackgroundColor(getResources().getColor(R.color.discover_header_transparent));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

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
