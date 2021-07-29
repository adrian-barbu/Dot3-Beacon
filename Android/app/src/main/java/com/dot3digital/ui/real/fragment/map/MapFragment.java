package com.dot3digital.ui.real.fragment.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dot3digital.R;
import com.dot3digital.ui.real.fragment.BaseFragment;
import com.dot3digital.ui.real.fragment.home.ViewCategoryFragment;
import com.dot3digital.ui.real.fragment.places.ViewPlaceEntriesFragment;

/**
 * @description Map Fragment
 *              This fragment has its two fragment such as ViewPlaceEntriesFragment, ViewPlaceEntryDetailFragment
 *
 * @author      Stelian
 */

public class MapFragment extends BaseFragment
{
    // Constant
    public final int FRAGMENT_ENTRY = 200;

    // PARAMS
    public final static String mViewKey = "-JyEPjxmu3p-YconNmo9";

    View mRootView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_map, container, false);
        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addFragment(FRAGMENT_ENTRY, null);
    }

    /**
     * Add Sub Fragment
     *
     * @param which  : Fragment ID
     * @param param  : Data which is needed to show fragment
     */
    private void addFragment(int which, Object param) {
        Fragment fragment = null;

        switch (which) {
            case FRAGMENT_ENTRY:
                fragment = new ViewMapEntriesFragment();
                break;
        }

        if (fragment != null) {
            Bundle bundle = new Bundle();
            bundle.putString(PARAM_VIEW_KEY, mViewKey);
            fragment.setArguments(bundle);

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            //if (direction)
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
            //else
            //    transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
		    transaction.replace(R.id.root_frame, fragment);
            transaction.commit();
        }
    }
}
