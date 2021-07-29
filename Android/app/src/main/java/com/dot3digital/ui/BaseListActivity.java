package com.dot3digital.ui;


import com.dot3digital.R;

import com.dot3digital.framework.D3Get;
import com.dot3digital.framework.model.BaseModel;
import com.dot3digital.ui.actionbar.widget.ActionBarOldCustom;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.WrapperListAdapter;

abstract public class BaseListActivity extends ListActivity implements D3Get.IGet
{
    protected static final String LOG_TAG = Class.class.getSimpleName();

    protected Dot3Application myApp = null;
    protected ActionBarOldCustom mActionBar;
    protected CustomArrayAdapter listAdapter = null;
    protected String[] keys = null;

    protected ArrayList<?> listValuesGeneric = null;
    protected ArrayList<String[]> listValuesAsStringArrays = null;
    protected ArrayList<HashMap<String, String>> listValuesAsHashMaps = null;

    protected String imageURLMapKey = null;
    protected String[] fromMapKeys = null;
    protected int[] toViewIDs = null;

    protected Bitmap parentItemBitmap = null;

    protected String selectedPlaceKey = null;
    protected String selectedPlaceName = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        myApp = (Dot3Application) getApplication();
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView( getIntendedContentView() );

        // Set the Action Bar
        mActionBar = (ActionBarOldCustom) findViewById(R.id.actionBar);
        mActionBar.setTitle(R.string.app_name);
        //mActionBar.setHomeLogo(R.mipmap.tree); // Set app logo icon (currently does nothing when the user clicks it).

        /* Do not delete - it's a template for future use (see also in the derived classes)
        // Set action icons in the Action Bar that do actions upon clicking.
        mActionBar.addActionIcon(R.mipmap.actionbar_btn,
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(BaseListActivity.this, "Clicked on an ActionIcon",
                            Toast.LENGTH_SHORT).show();
                    }
                });
        */
    }

    abstract protected int getIntendedContentView();
    abstract protected int getListItemLayout();
    abstract protected int getListParentItemLayout();
    abstract protected float defineTargetImageViewDp();
    abstract protected void onListItemClickAction(ListView l, View v, int position, long id, Object listElement);

    @Override
    public void handleFirebaseResponse(ArrayList<BaseModel> listValues) {

        this.listValuesGeneric = listValues;
        this.keys = keys;

        // Set a List Adapter

        listAdapter = new CustomArrayAdapter<HashMap<String, String>>(
                this,
                getListItemLayout(), getListParentItemLayout(),
                this.listValuesAsHashMaps,
                fromMapKeys, toViewIDs, imageURLMapKey,
                defineTargetImageViewDp(),
                parentItemBitmap
        );

        renderUIFinally();
    }
    // interface D3Get.IGet
    @Override
    public void handleFirebaseResponse(BaseModel hashMap) {
        // nothing
    }
    ////outdated, but keep it
    //// interface D3Get.IGet - but it's the old/alternative way of getting a Firebase node as String[]
    //@Override
    //public void handleFirebaseResponse(ArrayList<String[]> listValues, final String[] keys) {
    //
    //    this.listValuesGeneric = this.listValuesAsStringArrays = listValues;
    //    this.keys = keys;
    //
    //    // Set a List Adapter
    //
    //    listAdapter = new CustomArrayAdapter(
    //            this,
    //            getListItemLayout(), getListParentItemLayout(),
    //            this.listValuesAsStringArrays,
    //            null, null, null,
    //            defineTargetImageViewDp(),
    //            parentItemBitmap
    //    );
    //
    //    renderUIFinally();
    //}

    protected void renderUIFinally() {
        //ListView listView = (ListView) findViewById(R.id.listview); // Useful e.g. if the layout have a few ListView views, if it possible at all?
        ListView listView = getListView(); // Note, layout must have a ListView with id="@android:id/list" because the base class ListActivity wants it: mList = (ListView)findViewById(com.android.internal.R.id.list);
        listView.setAdapter(listAdapter);
        // To eliminate the issue: "While it scrolls, the
        // background drawable disappers. When i release the mouse, the
        // background still disappear till i move mouse to touch the screen."
        listView.setCacheColorHint(0);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Or better to put the button names initialisation into onStart() or onCreate() ?
        setButtonWrapTitles();
        setButtonWrapURLs();
    }    
    
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        
        //ListView listView = getListView(); // No need in getListView(), since the l parameter and getListView() point to the same ListView object.
        
        int pos = position - l.getHeaderViewsCount();

        // Get the list values from the CustomArrayAdapter
        CustomArrayAdapter customArrayAdapter = null;
        // The return object of ListView.getAdapter() might not be the same adapter passed to setAdapter(ListAdapter)
        // but might be a WrapperListAdapter or HeaderViewListAdapter
        if (l.getAdapter() instanceof CustomArrayAdapter) {
            customArrayAdapter = (CustomArrayAdapter) l.getAdapter();
        } else if (l.getAdapter() instanceof WrapperListAdapter) {
            customArrayAdapter = (CustomArrayAdapter) ((WrapperListAdapter) l.getAdapter()).getWrappedAdapter();
        } else if (l.getAdapter() instanceof HeaderViewListAdapter) {
            customArrayAdapter = (CustomArrayAdapter) ((HeaderViewListAdapter) l.getAdapter()).getWrappedAdapter();
        }

        ArrayList<Object> listValues =
            customArrayAdapter != null ? customArrayAdapter.realValues
            : this.listValuesAsStringArrays != null ? this.listValuesAsStringArrays
            : this.listValuesAsHashMaps != null ? this.listValuesAsHashMaps
            : null;
        Object listElement = listValues.get(pos);
        String listElementStatus;
        if (listElement instanceof String[]) {
            listElementStatus = ((String[]) listElement)[0];
        } else if (listElement instanceof HashMap<?, ?>) {
            listElementStatus = ((HashMap<String, String>) listElement).get(null);
        } else {
            throw new IllegalStateException("BaseListActivity: list row type " + listElement.getClass().getSimpleName() + " is not supported.");
        }

        // Checking that use clicked a data row, not a header (including if the header is added via addHeaderView())
        if (0 <= pos && pos < listValues.size()) { // if user clicked on a data element of the ListView, not on header or footer?
            if (listElementStatus == Dot3Application.HEADER_INDICATOR
                    || listElementStatus == Dot3Application.PARENT_ITEM_INDICATOR) {
                // It's a header or a parent item. Ignore.
            } else {
                // Call the subclass.
                onListItemClickAction(l, v, pos, id, listElement); // Abstract, to be implemented in derived classes.
            }
        }
    }


    static public class MapOfPlace implements D3Get.IGet
    {
        private Context context = null;
        private String selectedPlaceKey = null;
        private String selectedPlaceName = null;
        public MapOfPlace(Context context, String selectedPlaceKey, String selectedPlaceName) {
            this.context = context;
            this.selectedPlaceKey = selectedPlaceKey;
            this.selectedPlaceName = selectedPlaceName;
        }
        // interface D3Get.IGet
        @Override
        public void handleFirebaseResponse(ArrayList<BaseModel> geofencesList) {
            //[2015.11.15][Stelian] Temporailiy commented, need to check after this
            /**
            int i = 0;
            for (HashMap<String, String> geofence : geofencesList) {
                if (geofence.get("assignedPlace").equals(this.selectedPlaceKey)) {
                    // A temporary workaround - a toast notification to the user, for the case if there are
                    // more than one geofence with the same assignedPlace, in Firebase,
                    if (++i == 2) {
                        String warning = "NOTE: In Firebase, there are more than one geofence with the same assignedPlace. Showing all geofence locations on the map one-by-one...";
                        Log.d(LOG_TAG, warning);
                        Toast.makeText(this.context, warning, Toast.LENGTH_LONG).show();
                    }

                    // A geofence with assignedPlace == selectedPlaceKey is found. Open its the location in the map activity.
                    Intent intent = new Intent(this.context.getApplicationContext(),
                            MapsAsyncActivity.class); //MapsActivity.class);
                    intent.putExtra("MapsAsyncActivity_geoLat", Double.parseDouble(geofence.get("geoLat")));
                    intent.putExtra("MapsAsyncActivity_geoLong", Double.parseDouble(geofence.get("geoLong")));
                    intent.putExtra("MapsAsyncActivity_geoRadius", Float.parseFloat(geofence.get("geoRadius")));
                    intent.putExtra("MapsAsyncActivity_geofenceName", geofence.get("geofenceName"));
                    intent.putExtra("MapsAsyncActivity_placeName", selectedPlaceName);
                    this.context.startActivity(intent);
                }
            }
             **/
        }

        // interface D3Get.IGet
        @Override
        public void handleFirebaseResponse(BaseModel data) {}
    }

    // Toolbar buttons handlers
    public void onMyClick(View v) {
        if (v.getId() == R.id.btnHome)
        {
            Toast.makeText(this, "Button clicked.", Toast.LENGTH_SHORT).show();
        }
        else if (v.getId() == R.id.btnDiscover)
        {
            Toast.makeText(this, "Button clicked.", Toast.LENGTH_SHORT).show();
        }
        else if (v.getId() == R.id.btnMap)
        {
            // The Map button is clicked.
            // If this.selectedPlaceKey != null, find a geofence with assignedPlace == this.selectedPlaceKey
            // and show the geofence location on the map activity.
            if (this.selectedPlaceKey != null) {
                D3Get.getGeofencesArrayWithCompletion(new MapOfPlace(BaseListActivity.this, this.selectedPlaceKey, this.selectedPlaceName)); // It will callback the parameter's method handleFirebaseResponse()
            } else {
                // Temporary demo solution:
                Intent intent = new Intent(getApplicationContext(),
                        MapsAsyncActivity.class); //MapsActivity.class);

                intent.putExtra("MapsAsyncActivity_geoLat", 0.0);
                intent.putExtra("MapsAsyncActivity_geoLong", 0.0);
                intent.putExtra("MapsAsyncActivity_geoRadius", 100.0f);
                intent.putExtra("MapsAsyncActivity_geofenceName", "demo");
                intent.putExtra("MapsAsyncActivity_placeName", "demo");

                startActivity(intent);
            }
        }
        else if (v.getId() == R.id.btnPlace)
        {
            Intent intent = new Intent(getApplicationContext(),
                    PlacesListActivity.class);
            intent.putExtra("Some_places_parameter?", "TODO?");
            startActivity(intent);
        }
        /* temp commented-out
        else if (v.getId() == R.id.buttonSettings)
        {
            Toast.makeText(this, "Button clicked.", Toast.LENGTH_SHORT).show();
        }
        */
        /* temp commented-out
        else if (v.getId() == R.id.buttonWrapTitles)
        {
            toggleWrapTitles();
        }
        else if (v.getId() == R.id.buttonWrapURLs)
        {
            toggleWrapURLs();
        }
        else if (v.getId() == R.id.buttonCloseApplication)
        {
            finish(); // Is it a right way to exit an application???
        }
        */
    }


    /*package*/
    void toggleWrapTitles() {
        // I take and do it in CustomArrayAdapter.getView()
        myApp.AreTitlesSingleLine = !myApp.AreTitlesSingleLine;
        
        setButtonWrapTitles();
        
        getListView().invalidateViews();
    }
    void setButtonWrapTitles() {
        /* temp
        Button btnWrapTitles = (Button) findViewById(R.id.buttonWrapTitles);
        btnWrapTitles.setText(
                myApp.AreTitlesSingleLine ? R.string.WrapTitles : R.string.UnWrapTitles);
        int drawableId = myApp.AreTitlesSingleLine ? R.mipmap.wrap_titles : R.mipmap.unwrap_titles;
        btnWrapTitles.setCompoundDrawablesWithIntrinsicBounds(0, drawableId, 0, 0);
        */
    }
    /*package*/ 
    void toggleWrapURLs() {
        // I take and do it in CustomArrayAdapter.getView()
        myApp.AreURLsSingleLine = !myApp.AreURLsSingleLine;
        
        setButtonWrapURLs();
        
        getListView().invalidateViews();
    }
    void setButtonWrapURLs() {
        /* temp
        Button btnWrapURLs = (Button) findViewById(R.id.buttonWrapURLs);
        btnWrapURLs.setText(
                myApp.AreURLsSingleLine ? R.string.WrapURLs : R.string.UnWrapURLs);
        int drawableId = myApp.AreURLsSingleLine ? R.mipmap.wrap_urls : R.mipmap.unwrap_urls;
        btnWrapURLs.setCompoundDrawablesWithIntrinsicBounds(0, drawableId, 0, 0);
        */
    }

}

