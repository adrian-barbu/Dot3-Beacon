package com.dot3digital.ui.real.fragment.home;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.dot3digital.R;
import com.dot3digital.framework.D3Get;
import com.dot3digital.ui.Dot3Application;
import com.dot3digital.framework.model.BaseModel;
import com.dot3digital.framework.model.ViewCats;
import com.dot3digital.ui.real.adapter.CategoryArrayAdapter;
import com.dot3digital.ui.real.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @description ViewEntry Category Fragment
 *              This fragment is top fragment of home fragment
 *
 * @author      Stelian
 */

public class ViewCategoryFragment extends BaseFragment implements D3Get.IGet
{
    // UI Members
    View mRootView;             // Root ViewEntry

    ListView lvCategory;        // List ViewEntry
    CategoryArrayAdapter mListAdapter;

    ImageView ivLogo;           // Logo
    View layoutAlpha;           // Alpha Effected Background
    int mCurrentScroll;         // Scroll Variable

    // Variables
    String[] mKeys;             // Key Array When used to go detail
    boolean isOneOpened;        // The flag to prevent multiple detail page is opened.

    ArrayList<BaseModel> mViewCats;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_home_view_category, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set a List Adapter
        mListAdapter = new CategoryArrayAdapter<HashMap<String, String>>(getActivity());

        lvCategory = (ListView) mRootView.findViewById(R.id.lvCategory);
        lvCategory.setAdapter(mListAdapter);
        lvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                onClickItem(position);
            }
        });

        ivLogo = (ImageView) mRootView.findViewById(R.id.ivLogo);
        layoutAlpha = (View) mRootView.findViewById(R.id.layoutAlpha);

        // Set Scroll Listener
        lvCategory.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView list, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (list != null) {
                    if (list.getChildAt(0) != null) {
                        if (mCurrentScroll != list.getChildAt(0).getTop()) {
                            int firstVisible = list.getFirstVisiblePosition();
                            if (firstVisible == 0) {
                                mCurrentScroll = list.getChildAt(0).getTop();

                                // Set Alpha Of Header Logo's Background
                                int height = ivLogo.getHeight();
                                layoutAlpha.setAlpha(-mCurrentScroll / (float) height);
                            }
                        }
                    }
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView list, int scrollState) {
            }
        });

        // Now, start to get data
        String fbKey = Dot3Application.getInstance().getHomeViewKey();
        D3Get.getViewCatsArrayWithViewKey(this, fbKey); // It will call handleFirebaseResponse()
    }

    /**
     * On Click Item Listener
     *
     * @param position
     */
    private void onClickItem(int position) {
        if (!isOneOpened) {
            ViewCats viewcatsItem = (ViewCats) mViewCats.get(position);
            String screenCategoryType = viewcatsItem.getScreenCategoryType();

            // Make Fragment
            Fragment fragment;

            // [2015.10.12][Stelian] Fix Bug #32
            if (screenCategoryType != null && screenCategoryType.equals("standard-no-entries")) {
                fragment = new ViewEntryFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("item", viewcatsItem);
                fragment.setArguments(bundle);
            } else {
                fragment = new ViewCategorySubFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("item", viewcatsItem);
                fragment.setArguments(bundle);
            }

            // Add Fragment
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
            transaction.add(R.id.root_frame, fragment);
            transaction.addToBackStack(null);
            transaction.commit();

            isOneOpened = true;

            // Prevent user try to open double
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isOneOpened = false;
                }
            }, 2000);
        }
    }

    // interface D3Get.IGet
    @Override
    public void handleFirebaseResponse(BaseModel view) {}

    // interface D3Get.IGet
    @Override
    public void handleFirebaseResponse(ArrayList<BaseModel> values) {
        // [2015.09.28][Stelian][Fix Bug #23]
        initLayouts();

        if (values == null) {
            showLoadFailDialog();
            return;
        }

        mViewCats = values;

        mListAdapter.setData(values);
        mListAdapter.notifyDataSetChanged();
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
