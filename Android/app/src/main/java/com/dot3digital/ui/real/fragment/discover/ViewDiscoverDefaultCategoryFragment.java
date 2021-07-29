package com.dot3digital.ui.real.fragment.discover;

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
import android.widget.ListView;
import android.widget.TextView;

import com.dot3digital.R;
import com.dot3digital.framework.D3Get;
import com.dot3digital.framework.model.BaseModel;
import com.dot3digital.framework.model.ViewCats;
import com.dot3digital.framework.model.ViewEntry;
import com.dot3digital.ui.real.adapter.DiscoverCategoryArrayAdapter;
import com.dot3digital.ui.real.fragment.BaseFragment;
import com.dot3digital.ui.real.fragment.places.PlacesFragment;

import java.util.ArrayList;

/**
 * @description Discover Category Fragment
 *              This fragment is top fragment of discover fragment
 *
 * @author      Stelian
 */

public class ViewDiscoverDefaultCategoryFragment extends BaseFragment implements D3Get.IGet
{
    // UI Members
    View mRootView;             // Root ViewEntry

    ListView lvCategory;        // List ViewEntry
    DiscoverCategoryArrayAdapter mListAdapter;

    View layoutHeader;          // Header Layout Wrapped Alpha
    View layoutAlpha;           // Alpha Effected Background
    int mCurrentScroll;         // Scroll Variable

    TextView tvDiscoverTitle, tvDiscoverDescription;

    // Variables
    String mViewKey;            // ViewEntry Key
    boolean isOneOpened;        // The flag to prevent multiple detail page is opened.

    ArrayList<BaseModel> mValues;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_discover_default_view_category, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewKey = getArguments().getString(PlacesFragment.PARAM_VIEW_KEY);

        // Set a List Adapter
        mListAdapter = new DiscoverCategoryArrayAdapter<BaseModel>(getActivity());

        lvCategory = (ListView) mRootView.findViewById(R.id.lvCategory);
        lvCategory.setAdapter(mListAdapter);
        lvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                onClickItem(position);
            }
        });

        layoutHeader = (View) mRootView.findViewById(R.id.layoutHeader);
        layoutAlpha = (View) mRootView.findViewById(R.id.layoutAlpha);
        tvDiscoverTitle = (TextView) mRootView.findViewById(R.id.tvDiscoverTitle);
        tvDiscoverDescription = (TextView) mRootView.findViewById(R.id.tvDiscoverDescription);

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
                                int height = layoutHeader.getHeight();
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                D3Get.getViewWithViewKey(ViewDiscoverDefaultCategoryFragment.this, mViewKey);
            }
        }, 2000);
    }

    /**
     * On Click Item Listener
     *
     * @param position
     */
    private void onClickItem(int position) {
        if (!isOneOpened) {
            ViewCats viewcatsItem = (ViewCats) mValues.get(position);

            // Make Fragment
            Fragment fragment = new ViewDiscoverDefaultEntryFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("item", viewcatsItem);
            fragment.setArguments(bundle);

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
    public void handleFirebaseResponse(BaseModel data) {
        // Set Title & Description
        if (data != null) {
            ViewEntry viewEntry = (ViewEntry) data;

            tvDiscoverTitle.setText(viewEntry.getTitle());
            tvDiscoverDescription.setText(viewEntry.getDescription());

            // Now load list data
            D3Get.getViewCatsArrayWithViewKey(ViewDiscoverDefaultCategoryFragment.this, mViewKey);
        } else {
            showLoadFailDialog();
        }
    }

    // interface D3Get.IGet
    @Override
    public void handleFirebaseResponse(ArrayList<BaseModel> listValues) {
        // [2015.09.28][Stelian][Fix Bug #23]
        initLayouts();

        if (listValues == null) {
            showLoadFailDialog();
            return;
        }

        mValues = listValues;

        mListAdapter.setData(listValues);
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
