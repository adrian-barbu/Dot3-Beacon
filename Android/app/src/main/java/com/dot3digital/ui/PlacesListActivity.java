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

public class PlacesListActivity extends BaseListActivity
{
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
                        Toast.makeText(PlacesListActivity.this, "Clicked on an ActionIcon",
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


        // Map Firebase node children to layout view IDs, be used by CustomArrayAdapter via handleFirebaseResponse().
        this.imageURLMapKey = "placeImage";
        String[] fromMapKeys = {"placeName", "placeImage", "placeText"};
        this.fromMapKeys = fromMapKeys;
        int[] toViewIDs = {R.id.text1, R.id.text2, R.id.text3};
        this.toViewIDs = toViewIDs;

        // Calling Dot3 SDK
        D3Get.getPlacesArrayWithCompletion(this); // It will call handleFirebaseResponse()
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
                PlaceEntriesListActivity.class);

        intent.putExtra("PlacesListActivity_key", keys[pos]);
        if (listElement instanceof String[]) {
            intent.putExtra("PlacesListActivity_placeImage", ((String[]) listElement)[1]);
            intent.putExtra("PlacesListActivity_placeText", ((String[]) listElement)[2]);
            intent.putExtra("PlacesListActivity_placeName", ((String[]) listElement)[3]);
        } else if (listElement instanceof HashMap<?, ?>) {
            intent.putExtra("PlacesListActivity_placeImage", ((HashMap<String, String>) listElement).get("placeImage"));
            intent.putExtra("PlacesListActivity_placeText", ((HashMap<String, String>) listElement).get("placeText"));
            intent.putExtra("PlacesListActivity_placeName", ((HashMap<String, String>) listElement).get("placeName"));
        } else {
            throw new IllegalStateException("BaseListActivity: list row type " + listElement.getClass().getSimpleName() + " is not supported.");
        }

        //TODO: implement proper solution
        // Cannot put and then get Bitmap - it needs to be Parcelable.
        //Bitmap clickedItemBitmap = listAdapter.bitmaps[pos];
        //intent.putExtra("PlacesListActivity_clickedItemBitmap", clickedItemBitmap);
        // Temporary work-around:
        myApp.clickedItemBitmap = listAdapter.bitmaps[pos];

        startActivity(intent);
    }

    // Toolbar buttons handlers
    public void onMyClick(View v) {
        if (v.getId() == R.id.btnPlace)
        {
            // Do nothing preventing the BaseListActivity.onMyClick() to go to places?
        }
        else
        {
            super.onMyClick(v);
        }
    }

}

