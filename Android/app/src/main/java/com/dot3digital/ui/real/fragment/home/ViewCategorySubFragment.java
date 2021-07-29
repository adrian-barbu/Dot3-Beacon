package com.dot3digital.ui.real.fragment.home;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dot3digital.R;
import com.dot3digital.framework.D3Get;
import com.dot3digital.framework.model.BaseModel;
import com.dot3digital.framework.model.EntryForViewCat;
import com.dot3digital.framework.model.ViewCats;
import com.dot3digital.ui.real.adapter.CategorySubArrayAdapter;
import com.dot3digital.ui.real.fragment.BaseFragment;

import java.util.ArrayList;

/**
 * @description ViewEntry Category Sub Fragment
 *
 * @author      Stelian
 */

public class ViewCategorySubFragment extends BaseFragment implements D3Get.IGet
{
    View mRootView;         // Root ViewEntry
    ListView lvCategory;    // Sub Items
    CategorySubArrayAdapter mListAdapter = null;

    ImageView ivBack;       // Back Button
    TextView ivTitle;       // Title

    // Variables
    ViewCats mViewCats;     // ViewCats From Parameter
    String mKey, mName;

    boolean isOneOpened;        // The flag to prevent multiple detail page is opened.
    ArrayList<BaseModel> mEntriesForViewCat;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_home_view_category_sub, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewCats = (ViewCats) getArguments().getParcelable("item");

        mKey = mViewCats.getNodeKey();
        mName = mViewCats.getName();

        // Set a List Adapter
        mListAdapter = new CategorySubArrayAdapter<BaseModel>(getActivity());

        lvCategory = (ListView) mRootView.findViewById(R.id.lvCategory);
        lvCategory.setAdapter(mListAdapter);
        lvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                onClickItem(position);
            }
        });

        ivBack = (ImageView) mRootView.findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
                transaction.remove(ViewCategorySubFragment.this);
                transaction.commit();
            }
        });

        ivTitle = (TextView) mRootView.findViewById(R.id.ivTitle);
        ivTitle.setText(mName);

        // Now, start to get data
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                D3Get.getEntriesArrayWithCatKey(ViewCategorySubFragment.this, mKey);
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
            EntryForViewCat entry = (EntryForViewCat) mEntriesForViewCat.get(position);

            // Make Fragment
            Fragment fragment = new ViewEntryFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("item", entry);
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
    public void handleFirebaseResponse(ArrayList<BaseModel> values) {
        // [2015.09.28][Stelian][Fix Bug #23]
        initLayouts();

        if (values == null) {
            showLoadFailDialog();
            return;
        }

        mEntriesForViewCat = values;

        mListAdapter.setData(values);
        mListAdapter.notifyDataSetChanged();
    }

    // interface D3Get.IGet
    @Override
    public void handleFirebaseResponse(BaseModel entry) {}

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
