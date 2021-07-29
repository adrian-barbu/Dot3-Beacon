package com.dot3digital.ui;


import com.dot3digital.R;

import com.dot3digital.framework.D3Constants;
import com.dot3digital.framework.D3Get;
import com.dot3digital.framework.model.BaseModel;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewcatEntriesListActivity extends BaseListActivity
{
    String selectedKey;
    String selected_viewCatImage;
    String selected_viewCatText;
    String selected_viewCatName;

    /////
    static final String STATE_SELECTEDKEY = "selectedKey";
    static final String STATE_KEYS = "keys";

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putString(STATE_SELECTEDKEY, selectedKey);
        savedInstanceState.putStringArray(STATE_KEYS, keys);
        // See BaseListActivity.handleFirebaseResponse() instead of this;
        //savedInstanceState.putParcelableArrayList(STATE_LISTADAPTERVALUES, listAdapterValues);
        //savedInstanceState.putParcelableArrayList(STATE_LISTADAPTER, listAdapter);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }
    /////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Note: The base onCreate() calls setContentView() with this getIntendedContentView()
        super.onCreate(savedInstanceState);

        // Set action icons in the Action Bar that do actions upon clicking.
        /* Do not delete - it's a template for future use (see also in the base class)
        mActionBar.addActionIcon(R.mipmap.actionbar_btn,
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ViewcatEntriesListActivity.this, "Clicked on an ActionIcon",
                            Toast.LENGTH_SHORT).show();
                    }
                });
        */
        mActionBar.addActionIcon(R.mipmap.back_arrow, //.backward,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

        String newSelectedKey = getIntent().getExtras().getString("ViewcatsListActivity_key");
        selected_viewCatImage = getIntent().getExtras().getString("ViewcatsListActivity_viewCatImage");
        selected_viewCatText = getIntent().getExtras().getString("ViewcatsListActivity_viewCatText");
        selected_viewCatName = getIntent().getExtras().getString("ViewcatsListActivity_viewCatName");
        mActionBar.setTitle(selected_viewCatName);

        //TODO: implement proper solution
        // Cannot put and then get Bitmap - it needs to be Parcelable.
        //this.parentItemBitmap = (Bitmap) getIntent().getExtras().get("ViewcatsListActivity_clickedItemBitmap");
        // Temporary work-around:
        this.parentItemBitmap = myApp.clickedItemBitmap;


        /////
        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            selectedKey = savedInstanceState.getString(STATE_SELECTEDKEY);
            keys = savedInstanceState.getStringArray(STATE_KEYS);
        } else {
            // Probably initialize members with default values for a new instance
        }
        /////

        // Map Firebase node children to layout view IDs, be used by CustomArrayAdapter via handleFirebaseResponse().
        this.imageURLMapKey = "entryImage";
        String[] fromMapKeys = {"entryHeadline", "entryImage", "entryTeaserText"};
        this.fromMapKeys = fromMapKeys;
        int[] toViewIDs = {R.id.text1, R.id.text2, R.id.text3};
        this.toViewIDs = toViewIDs;

        if (selectedKey == null || newSelectedKey.compareTo(selectedKey) != 0
                || (listValuesAsStringArrays == null && listValuesAsHashMaps == null)
                || listAdapter == null || keys == null)
        {
            selectedKey = newSelectedKey;

            // Calling Dot3 SDK
            D3Get.getEntriesArrayWithCatKey( this, selectedKey); // It will call handleFirebaseResponse()
        } else {
            // If onCreate() is called e.g. because of orientation change
            // then the "ViewcatsListActivity_key" extra doesn't change
            // and listAdapterValues and keys are non-null and actual,
            // and listView and listAdapter are actual,
            // so no need to call D3Get and handleFirebaseResponse(),
            // so CustomArrayAdapter and its bitmaps stay the same as before,
            // so repeating web image re-loads do not happen.
            renderUIFinally();
        }
    }

    @Override
    protected int getIntendedContentView() {
        return R.layout.viewcatentries_list_layout;
    }

    @Override
    protected int getListItemLayout() {
        return R.layout.viewcatentries_list_item_layout;
    }

    @Override
    protected int getListParentItemLayout()
    {
        return R.layout.viewcatentries_list_parent_item_layout;
    }

    @Override
    protected float defineTargetImageViewDp() {
        //TextView text1 = (TextView) rootView.findViewById(R.id.text1);
        //float targetImageViewDp = (float) (text1.getTextSize() * 1.5);
        //return targetImageViewDp;

        // or:

        // This results in: CustomArrayAdapter.scaleBitmap() >>> Bitmap.createBitmap() >>> IllegalArgumentException: width and height must be > 0
        //LinearLayout textContents = (LinearLayout) rootView.findViewById(R.id.textContents);
        //float targetImageViewDp = (float) (textContents .getMeasuredHeight()); //.getHeight()); // .getMeasuredHeight());
        //return targetImageViewDp;

        // or:

        //int id = R.dimen.icon_size_dp;
        //return this.getResources()
        //        //.getDimensionPixelSize(id);
        //        .getDimension(id);

        // or:

        return this.getResources().getDisplayMetrics().widthPixels / 3; // Using width of device's screen.

    }


    // interface D3Get.IGet
    @Override
    public void handleFirebaseResponse(ArrayList<BaseModel> listAdapterValues) {
        //[2015.11.15][Stelian] Temporailiy commented, need to check after this

        /*
        boolean parentItemAndHeader_newSolution = true;
        if (parentItemAndHeader_newSolution) {
            // Use ListView API to show a parent item and a header in the list.

            // Note, on devices with Android version earlier than 4.4 KitKat,
            // the ListView.addHeaderView(v) should be called
            // before setting the adapter with setAdapter(ListAdapter),
            // that is before handleFirebaseResponse()

            ListView lv = this.getListView();

            // Set parent item content.
            View parentView = View.inflate(this, getListParentItemLayout(), null);
            lv.addHeaderView(parentView, null, false); //(parentView);
            ((TextView) parentView.findViewById(R.id.text1)).setText(Dot3Application.PARENT_ITEM_INDICATOR);
            ((TextView) parentView.findViewById(R.id.text2)).setText(selected_viewCatImage); // urlIdx
            ((TextView) parentView.findViewById(R.id.text3)).setText(selected_viewCatText);
            // Set bitmap to the list parent item's image view.
            ImageView imageView = (ImageView) parentView.findViewById(R.id.imageView1);
            if (parentItemBitmap != null) {
                CustomArrayAdapter.setScaledBitmap(imageView, parentItemBitmap);
            } else {
                Log.e(LOG_TAG, "parentItemBitmap is null");
            }

            // Set header item.
            View headerView = View.inflate(this, R.layout.list_header, null);
            lv.addHeaderView(headerView, null, false); //(headerView);
        }
        else {
            // Alternatively, showing a parent item and a header in the list
            // (or any extra items in the list, at any position!)
            // can be done as inserting them in the list as "special" rows (e.g. as PARENT_ITEM_INDICATOR or HEADER_INDICATOR),
            // since I've implemented appropriate rendering support in CustomArrayAdapter.getView().
            // So, it can be used in future to show headers etc, e.g. in the middle of the list.
            // For example:

            // Insert a parent item to the list, to be indicated in an according ListView of the app
            HashMap<String, String> stArrParent = new HashMap<String, String>();
            byte[] iconBytesParent = null;
            stArrParent.put(null, Dot3Application.PARENT_ITEM_INDICATOR);
            stArrParent.put(Dot3Application.PARENT_ITEM_URL_HASHMAPKEY, selected_viewCatImage);
            stArrParent.put(Dot3Application.PARENT_ITEM_TEXT_HASHMAPKEY, selected_viewCatText);
            listAdapterValues.add(0, stArrParent);

            // Insert a header (a label) to the list, to be indicated in an according ListView of the app
            HashMap<String, String> stArrHeader = new HashMap<String, String>();
            stArrHeader.put(null, Dot3Application.HEADER_INDICATOR);
            listAdapterValues.add(1, stArrHeader);

            // Do not forget to use correct keys including ones the parent item and header.
            keys = new String[listAdapterValues.size()];
            for (int j = 0; j < listAdapterValues.size(); j++) {
                keys[j] = listAdapterValues.get(j).get(D3Constants.FIREBASE_NODE_KEY);
            }
        }

        super.handleFirebaseResponse(keys, listAdapterValues);
        */
    }

    @Override
    protected void onListItemClickAction(ListView l, View v, int pos, long id, Object listElement) {
        Intent intent = new Intent(getApplicationContext(),
                ViewcatEntryDetailsActivity.class);

        intent.putExtra("ViewcatEntriesListActivity_viewCatName", selected_viewCatName);
        intent.putExtra("ViewcatEntriesListActivity_key", keys[pos]); // Note, no nee to correct the pos because of parent and header rows are in the list.
        if (listElement instanceof String[]) {
            intent.putExtra("ViewcatEntriesListActivity_entryImage", ((String[]) listElement)[1]);
            intent.putExtra("ViewcatEntriesListActivity_entryHeadline", ((String[]) listElement)[3]);
            intent.putExtra("ViewcatEntriesListActivity_entryText", ((String[]) listElement)[4]);
        } else if (listElement instanceof HashMap<?, ?>) {
            intent.putExtra("ViewcatEntriesListActivity_entryImage", ((HashMap<String, String>) listElement).get("entryImage"));
            intent.putExtra("ViewcatEntriesListActivity_entryHeadline", ((HashMap<String, String>) listElement).get("entryHeadline"));
            intent.putExtra("ViewcatEntriesListActivity_entryText", ((HashMap<String, String>) listElement).get("entryText"));
        } else {
            throw new IllegalStateException("BaseListActivity: list row type " + listElement.getClass().getSimpleName() + " is not supported.");
        }

        //TODO: implement proper solution
        // Cannot put and then get Bitmap - it needs to be Parcelable.
        //Bitmap clickedItemBitmap = listAdapter.bitmaps[pos];
        //intent.putExtra("ViewcatsListActivity_clickedItemBitmap", clickedItemBitmap);
        // Temporary work-around:
        myApp.clickedItemBitmap = listAdapter.bitmapsUnscaled[pos]; // = listAdapter.bitmaps[pos]; // Note the bitmaps array has scaled bitmaps.

        startActivity(intent);
    }

}

