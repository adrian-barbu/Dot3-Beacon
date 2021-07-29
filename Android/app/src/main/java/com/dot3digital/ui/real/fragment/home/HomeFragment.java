package com.dot3digital.ui.real.fragment.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dot3digital.R;
import com.dot3digital.ui.real.fragment.OnReplaceFragmentListener;

/**
 * @description Home Fragment
 *              This fragment has its several fragment such as ViewCategoryFragment, ViewSubCategoryFragment, ViewEntryFragment
 *
 * @author      Stelian
 */

public class HomeFragment extends Fragment
{
    // Constant
    public final int FRAGMENT_CATEGORY = 100;
    public final int FRAGMENT_CATEGORY_SUB = 101;
    public final int FRAGMENT_CATEGORY_SUB_ENTRY = 102;

    View mRootView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_home, container, false);
        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addFragment(FRAGMENT_CATEGORY, null);
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
            case FRAGMENT_CATEGORY:
                fragment = new ViewCategoryFragment();
                break;

            case FRAGMENT_CATEGORY_SUB:
                fragment = new ViewCategoryFragment();
                break;

            case FRAGMENT_CATEGORY_SUB_ENTRY:
                fragment = new ViewCategoryFragment();
                break;
        }

        if (fragment != null) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            //if (direction)
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
            //else
            //    transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
		    transaction.replace(R.id.root_frame, fragment);
            transaction.commit();
        }
    }

    /**
     * Fragment Replace Listener
     *
     * It will be shared throughout several fragments
     */
    private OnReplaceFragmentListener mOnReplaceFragmentListener = new OnReplaceFragmentListener() {
        @Override
        public void onReplace(int which, Object param) {
            addFragment(which, param);
        }
    };
}
