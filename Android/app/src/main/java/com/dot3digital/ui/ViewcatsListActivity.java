package com.dot3digital.ui;


import com.dot3digital.R;

import com.dot3digital.framework.D3Get;
import com.dot3digital.framework.geofencing.GeofencesRegionManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewcatsListActivity extends BaseListActivity
{
    protected String selectedKey = null;

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
                        Toast.makeText(ViewcatsListActivity.this, "Clicked on an ActionIcon",
                            Toast.LENGTH_SHORT).show();
                    }
                });
        */
        // The Back button
        mActionBar.addActionIcon(R.mipmap.back_arrow, //.backward,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
        /* For QA and debugging only - commented out, do not delete.
        // The Dot3 SDK test button
        mActionBar.addActionIcon(R.mipmap.ic_launcher,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(),
                                Dot3SDKTestActivity.class);
                        intent.putExtra("TestParam", "nothing");
                        startActivity(intent);
                    }
                });
        */

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            selectedKey = extras.getString("ViewsActivity_key");
        }
        if (selectedKey == null) {
            selectedKey = Dot3Application.getInstance().getHomeViewKey();
        }

        // Map Firebase node children to layout view IDs, be used by CustomArrayAdapter via handleFirebaseResponse().
        this.imageURLMapKey = "viewCatImage";
        String[] fromMapKeys = {"viewCatName", "viewCatImage", "viewCatText","viewCatTagline"};
        this.fromMapKeys = fromMapKeys;
        int[] toViewIDs = {R.id.text1, R.id.text2, R.id.text3, R.id.viewCatTagline};
        this.toViewIDs = toViewIDs;

        // Calling Dot3 SDK
        D3Get.getViewCatsArrayWithViewKey(this, selectedKey); // It will call handleFirebaseResponse()
    }

    @Override
    protected int getIntendedContentView() {
        return R.layout.viewcats_list_layout;
    }

    @Override
    protected int getListItemLayout() {
        return R.layout.viewcats_list_item_layout;
    }
    @Override
    protected int getListParentItemLayout()
    {
        return 0;
    }

    @Override
    protected float defineTargetImageViewDp() {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        //TODO: return device's display width or height depending if in portrait or landscape orientation)
        return width;
    }


    @Override
    protected void onListItemClickAction(ListView l, View v, int pos, long id, Object listElement) {
        Intent intent = new Intent(getApplicationContext(),
                ViewcatEntriesListActivity.class);

        intent.putExtra("ViewcatsListActivity_key", keys[pos]);
        if (listElement instanceof String[]) {
            intent.putExtra("ViewcatsListActivity_viewCatImage", ((String[]) listElement)[1]);
            intent.putExtra("ViewcatsListActivity_viewCatText", ((String[]) listElement)[2]);
            intent.putExtra("ViewcatsListActivity_viewCatName", ((String[]) listElement)[3]);
        } else if (listElement instanceof HashMap<?, ?>) {
            intent.putExtra("ViewcatsListActivity_viewCatImage", ((HashMap<String, String>) listElement).get("viewCatImage"));
            intent.putExtra("ViewcatsListActivity_viewCatText", ((HashMap<String, String>) listElement).get("viewCatText"));
            intent.putExtra("ViewcatsListActivity_viewCatName", ((HashMap<String, String>) listElement).get("viewCatName"));
        } else {
            throw new IllegalStateException("BaseListActivity: list row type " + listElement.getClass().getSimpleName() + " is not supported.");
        }

        //TODO: implement proper solution
        // Cannot put and then get Bitmap - it needs to be Parcelable.
        //Bitmap clickedItemBitmap = listAdapter.bitmaps[pos];
        //intent.putExtra("ViewcatsListActivity_clickedItemBitmap", clickedItemBitmap);
        // Temporary work-around:
        myApp.clickedItemBitmap = listAdapter.bitmaps[pos];

        startActivity(intent);
    }

}

