package com.dot3digital.ui.real.fragment.places;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.dot3digital.R;
import com.dot3digital.framework.D3Get;
import com.dot3digital.framework.model.BaseModel;
import com.dot3digital.framework.model.EntryForViewCat;
import com.dot3digital.framework.model.ViewEntry;
import com.dot3digital.ui.real.adapter.CategorySubArrayAdapter;
import com.dot3digital.ui.real.fragment.BaseFragment;

import java.util.ArrayList;

/**
 * @description ViewEntry Place Entries Fragment
 *
 * @author      Stelian
 */

public class ViewPlaceEntriesFragment extends BaseFragment implements D3Get.IGet
{
    // UI Members
    View mRootView;         // Root ViewEntry
    ListView lvEntry;       // Sub Items
    CategorySubArrayAdapter mListAdapter = null;

    TextView tvPlaceTitle, tvPlaceDescription;

    // Variables
    String mViewKey, mViewCateKey;

    boolean isOneOpened;        // The flag to prevent multiple detail page is opened.
    ArrayList<BaseModel> mEntriesForViewCat;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_places_view_entries, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewKey = getArguments().getString(PlacesFragment.PARAM_VIEW_KEY);
        mViewCateKey = getArguments().getString(PlacesFragment.PARAM_VIEW_CATE_KEY);

        // Set Title & Description
        tvPlaceTitle = (TextView) mRootView.findViewById(R.id.tvPlaceTitle);
        tvPlaceDescription = (TextView) mRootView.findViewById(R.id.tvPlaceDescription);

        // Set a List Adapter
        mListAdapter = new CategorySubArrayAdapter<BaseModel>(getActivity());

        lvEntry = (ListView) mRootView.findViewById(R.id.lvEntry);
        lvEntry.setAdapter(mListAdapter);
        lvEntry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                onClickItem(position);
            }
        });

        // Now, start to get data
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                D3Get.getViewWithViewKey(ViewPlaceEntriesFragment.this, mViewKey);
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

            // Get Cate Name
            EntryForViewCat item = (EntryForViewCat) mEntriesForViewCat.get(position);

            // Make Fragment
            Fragment fragment = new ViewPlaceEntryFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("item", item);
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

            tvPlaceTitle.setText(viewEntry.getName());

            if (!viewEntry.getDescription().isEmpty())
                tvPlaceDescription.setText(viewEntry.getDescription());
            else
                tvPlaceDescription.setVisibility(View.GONE);

            // Now load list data
            D3Get.getEntriesArrayWithCatKey(ViewPlaceEntriesFragment.this, mViewCateKey);
        } else {
            showLoadFailDialog();
        }
    }

    // interface D3Get.IGet
    @Override
    public void handleFirebaseResponse(ArrayList<BaseModel> listValues) {
        initLayouts();

        if (listValues == null) {
            showLoadFailDialog();
            return;
        }

        mEntriesForViewCat = listValues;

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
