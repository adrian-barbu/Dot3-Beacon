package com.dot3digital.ui;


import com.dot3digital.R;

import com.dot3digital.framework.D3Get;
import com.dot3digital.ui.actionbar.widget.ActionBarOldCustom;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewcatEntryDetailsActivity extends Activity
{
    protected Dot3Application myApp = null;
    protected ActionBarOldCustom mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myApp = (Dot3Application) getApplication();

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView( R.layout.viewcatentry_details_layout );

        // Set the Action Bar
        mActionBar = (ActionBarOldCustom) findViewById(R.id.actionBar);
        mActionBar.setTitle(R.string.app_name);
        //mActionBar.setHomeLogo(R.mipmap.tree); // Set app logo icon (currently does nothing when the user clicks it).

        // Set action icons in the Action Bar that do actions upon clicking.
        /* Do not delete - it's a template for future use (see also in the base class)
        mActionBar.addActionIcon(R.mipmap.actionbar_btn,
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ViewcatEntryDetailsActivity.this, "Clicked on an ActionIcon",
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

        String selected_viewCatName = getIntent().getExtras().getString("ViewcatEntriesListActivity_viewCatName");
        mActionBar.setTitle(selected_viewCatName);

        //String selectedKey = getIntent().getExtras().getString("ViewcatEntriesListActivity_key");
        String selected_entryImage = getIntent().getExtras().getString("ViewcatEntriesListActivity_entryImage");
        String selected_entryHeadline = getIntent().getExtras().getString("ViewcatEntriesListActivity_entryHeadline");
        String selected_entryText = getIntent().getExtras().getString("ViewcatEntriesListActivity_entryText");

        TextView text1 = (TextView) findViewById(R.id.text1);
        TextView text2 = (TextView) findViewById(R.id.text2);
        TextView text3 = (TextView) findViewById(R.id.text3);
        text1.setText(selected_entryImage);
        text2.setText(selected_entryHeadline);
        text3.setText(selected_entryText);

        //TODO: implement proper solution
        // Cannot put and then get Bitmap - it needs to be Parcelable.
        //this.parentItemBitmap = (Bitmap) getIntent().getExtras().get("ViewcatEntriesListActivity_clickedItemBitmap");
        // Temporary work-around:
        Bitmap parentItemBitmap = myApp.clickedItemBitmap;

        // Scaling and setting a Bitmap as the content of an ImageView.
   //     int targetImageViewDp = this.getResources().getDisplayMetrics().widthPixels; // Using width of device's screen.
  //      parentItemBitmap = CustomArrayAdapter.scaleBitmap(parentItemBitmap, targetImageViewDp, false); // true);
  //      ImageView imageView = (ImageView) findViewById(R.id.imageView1);
//        CustomArrayAdapter.setScaledBitmap(imageView, parentItemBitmap); // Or just //imageView.setImageBitmap(parentItemBitmap);
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
        else if (v.getId() == R.id.btnMap || v.getId() == R.id.buttonViewOnMap)
        {
            Intent intent = new Intent(getApplicationContext(),
                    MapsAsyncActivity.class); //MapsActivity.class);

            //TODO: See how it's done in the BaseListActivity.
            // Temporary demo solution:
            intent.putExtra("MapsAsyncActivity_geoLat", 0.0);
            intent.putExtra("MapsAsyncActivity_geoLong", 0.0);
            intent.putExtra("MapsAsyncActivity_geoRadius", 100.0f);
            intent.putExtra("MapsAsyncActivity_geofenceName", "demo");
            intent.putExtra("MapsAsyncActivity_placeName", "demo");

            startActivity(intent);
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
    }

}

