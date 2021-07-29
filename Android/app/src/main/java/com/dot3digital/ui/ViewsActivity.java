package com.dot3digital.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.dot3digital.R;
import com.dot3digital.framework.D3Constants;
import com.dot3digital.framework.D3Core;
import com.dot3digital.framework.D3Get;
import com.dot3digital.framework.model.BaseModel;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

public class ViewsActivity extends Activity implements D3Get.IGet
        //outdated//,D3Get.IGetPair
{
    private ListView listView;

    private ValueEventListener mConnectedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.views_list_layout);
        listView = (ListView) findViewById(R.id.listView);

        boolean using_D3Get_getViewWithViewKey = false; //= true;
        if (using_D3Get_getViewWithViewKey) {
            // Calling Dot3 SDK
            D3Get.getViewWithViewKey(this, Dot3Application.getInstance().getHomeViewKey()); // It will call handleFirebaseResponse()
        } else {
            // Calling Dot3 SDK
            D3Get.getViewsArrayWithCompletion(this); // It will call handleFirebaseResponse()
        }
    }

    // interface D3Get.IGet
    @Override
    public void handleFirebaseResponse(ArrayList<BaseModel> viewList) {

        //TODO: get Bitmap by URL to ImageView the right way, via a custom list adapter

        //[2015.11.15][Stelian] Temporailiy commented, need to check after this

        /*
        // Fill-up ListView layout
        String[] from = {"viewName", "viewDescription", "viewImage"};
        // Ids of views in listview_layout
        int[] to = {R.id.viewNameTextView, R.id.viewDescriptionTextView, R.id.viewImageURLTextView}; //R.id.viewImageImageView};
        SimpleAdapter adapter = new SimpleAdapter(ViewsActivity.this, viewList, R.layout.views_list_item_layout, from, to);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),
                        ViewcatsListActivity.class);
                intent.putExtra("ViewsActivity_key", keys[position]);
                startActivity(intent);
            }
        });
        */
    }


    ///// Use of D3Get.getViewWithViewKey() - start /////////////
    protected CustomArrayAdapter listAdapter = null;
    protected String[] keys = null;
    ArrayList<String[]> listAdapterAsStringArrays = null;
    ArrayList<HashMap<String, String>> listAdapterAsHashMaps = null;
    Bitmap parentItemBitmap = null;

//    //outdated//
//    // interface D3Get.IGet
//    @Override
//    public void handleFirebaseResponse(ArrayList<String[]> listAdapterValues, final String[] keys) {
//        // nothing
//    }
    // interface D3Get.IGet
    @Override
    public void handleFirebaseResponse(BaseModel data) {

        //[2015.11.15][Stelian] Temporailiy commented, need to check after this

        /*
        ///// simulating array needed for CustomArrayAdapter and this view layout.
        ArrayList<HashMap<String, String>> listAdapterValues = new ArrayList<HashMap<String, String>>();
        listAdapterValues.add(hashMap);

        String[] viewKeys = new String[ listAdapterValues.size() ];
        int j = 0;
        for (HashMap<String, String> viewData : listAdapterValues) {
            viewKeys[j] = viewData.get(D3Constants.FIREBASE_NODE_KEY); // view.getKey();
            j++;
        }
        /////

        this.listAdapterAsHashMaps = listAdapterValues;
        this.keys = viewKeys;

        // Map Firebase node children to layout view IDs, be used by CustomArrayAdapter via handleFirebaseResponse().
        String imageURLMapKey = "viewCatImage";
        String[] fromMapKeys = {"viewCatName", "viewCatImage", "viewCatText"};
        int[] toViewIDs = {R.id.text1, R.id.text2, R.id.text3};

        // Set a List Adapter

        listAdapter = new CustomArrayAdapter(
                this,
                R.layout.viewcats_list_item_layout, //R.layout.viewcatentries_list_item_layout, //R.layout.views_list_item_layout, // CustomArrayAdapter wants ids: text1, text2, text3
                0,
                listAdapterValues,

                fromMapKeys, toViewIDs, imageURLMapKey,

                this.getResources().getDisplayMetrics().widthPixels, // width of device's screen
                parentItemBitmap
        );

        renderUIFinally();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),
                        ViewcatsListActivity.class);
                intent.putExtra("ViewsActivity_key", keys[position]);
                startActivity(intent);
            }
        });
        */

    }
    ////outdated//
    //// interface D3Get.IGet
    //@Override
    //public void handleFirebaseResponse(String[] st, final String key) {
    //    ///// simulating array needed for CustomArrayAdapter and this view layout.
    //    ArrayList<String[]> listAdapterValues = new ArrayList<String[]>();
    //    listAdapterValues.add(st);
    //
    //    String[] viewKeys = new String[ listAdapterValues.size() ];
    //    int j = 0;
    //    for (String[] viewData : listAdapterValues) {
    //        viewKeys[j] = viewData[0]; // view.getKey();
    //        j++;
    //    }
    //    /////
    //
    //    this.listAdapterAsStringArrays = listAdapterValues;
    //    this.keys = viewKeys;
    //
    //    // Set a List Adapter
    //
    //    listAdapter = new CustomArrayAdapter(
    //            this,
    //            R.layout.viewcats_list_item_layout, //R.layout.viewcatentries_list_item_layout, //R.layout.views_list_item_layout, // CustomArrayAdapter wants ids: text1, text2, text3
    //            0,
    //            listAdapterValues,
    //
    //            null, null, null,
    //
    //            this.getResources().getDisplayMetrics().widthPixels, // width of device's screen
    //            parentItemBitmap
    //    );
    //
    //    renderUIFinally();
    //
    //    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    //        @Override
    //        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    //            Intent intent = new Intent(getApplicationContext(),
    //                    ViewcatsListActivity.class);
    //            intent.putExtra("ViewsActivity_key", keys[position]);
    //            startActivity(intent);
    //        }
    //    });
    //}

    protected void renderUIFinally() {
        //ListView listView = (ListView) findViewById(R.id.listview); // Useful e.g. if the layout have a few ListView views, if it possible at all?
        //ListView listView = getListView(); // Note, layout must have a ListView with id="@android:id/list" because the base class ListActivity wants it: mList = (ListView)findViewById(com.android.internal.R.id.list);
        listView.setAdapter(listAdapter);
        // To eliminate the issue: "While it scrolls, the
        // background drawable disappers. When i release the mouse, the
        // background still disappear till i move mouse to touch the screen."
        listView.setCacheColorHint(0);
    }
    ///// Use of D3Get.getViewWithViewKey() - end /////////////


    @Override
    public void onStart() {
        super.onStart();

        // indication of connection status
        mConnectedListener = D3Core.getFirebaseRef()
            .getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean connected = (Boolean) dataSnapshot.getValue();
                    if (connected) {
                        Toast.makeText(ViewsActivity.this, "Connected to Firebase", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ViewsActivity.this, "Disconnected from Firebase", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    // No-op
                }
            });
    }

    @Override
    public void onStop() {
        super.onStop();
        D3Core.getFirebaseRef()
            .getRoot().child(".info/connected").removeEventListener(mConnectedListener);
        //...
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent views_list_layout in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
