package com.dot3digital.ui;

import com.dot3digital.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.provider.Browser;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomArrayAdapter<T> extends ArrayAdapter<Object>
{
    protected Context context;
    protected int listItemlayoutId;
    protected int listParentItemlayoutId;

    public ArrayList<T> realValues;

    public Bitmap[] bitmaps = null;
    public Bitmap[] bitmapsUnscaled = null;

    protected View rootView = null;
    protected Bitmap placeholderIcon = null;
    protected Dot3Application myApp = null;
    protected float targetImageViewDp;
    protected Bitmap parentItemBitmap = null;

    protected int[] mTo;
    protected String[] mFrom;
    protected String imageURLMapKey;

    // Experimental fix of the timing issue of icon rendering:
    protected int firstNonNullBitmapIndex = -1;

    LayoutInflater mInflater;       //[2015.09.28][Stelian]

    DisplayImageOptions options;

    public CustomArrayAdapter(Context context,
                              int listItemlayoutId, int listParentItemlayoutId,

                              ArrayList<T> realValues,

                              String[] from, int[] to, String imageURLMapKey,

                              float targetImageViewDp,
                              Bitmap parentItemBitmap)
    {
        super(context, listItemlayoutId,
                // Send the super class constructor, ArrayAdapter, a fake empty array (but of the same size),
                // since the real functionality is overridden here.
                new Object[ realValues.size() ]);

        this.context = context;
        this.listItemlayoutId = listItemlayoutId;
        this.listParentItemlayoutId = listParentItemlayoutId;
        this.realValues = realValues;

        this.mFrom = from;
        this.mTo = to;
        this.imageURLMapKey = imageURLMapKey;

        this.targetImageViewDp = targetImageViewDp;
        this.parentItemBitmap = parentItemBitmap;

        myApp = (Dot3Application) ((android.app.Activity)context).getApplication();

        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = mInflater.inflate(listItemlayoutId, /*parent*/ null /*???*/, false);

        prepareBitmaps();

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(android.R.color.transparent)
                .showImageForEmptyUri(android.R.color.transparent)
                .showImageOnFail(android.R.color.transparent)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(500))
                .build();
    }
    
    private void prepareBitmaps() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        int size = realValues.size();
        bitmaps = new Bitmap[size]; // For use in this Activity.
        bitmapsUnscaled = new Bitmap[size]; // For possible use in child Activities.
        for (int i=0; i < size; i++) {
            bitmaps[i] = null;

            /*
            //TODO: Need to think how we can correctly use if we pre-load images from web. Temporarily commented-out.
            // Loading a Bitmap here from web by image's URL (if the URL is non-null and non-empty)
            // Do it either in prepareBitmaps() with further handling download complete notification,
            // or just do it in bytesToIcon()
            String imageURL = realValues.get(i)[1];
            new DownloadImageTask(null, i, bitmaps, bitmapsUnscaled, targetImageViewDp)
                    .execute(imageURL); //test//    .execute("http://23.253.36.191/PublicForImages/20150630225319.jpg");
            */
        }

        placeholderIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.world_aqua);
        placeholderIcon = CustomArrayAdapter.scaleBitmap(placeholderIcon, targetImageViewDp, false); // true);
    }

    /**
     * View Holder
     */
    private static class ViewHolder {
        public View rootView;
        public TextView text1;
        public TextView text2;
        public TextView text3;
        public ImageView imageView1;
    }

    // Note: This method is really important to a ListView 
    // as it is called for each view that is going to be displayed.  
    // Inflating a layout is fairly expensive. 
    // So, we are to improve the performance of your ListView: 
    // if the convertView parameter is not null, then we don�t need to inflate a View.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        
        // Check if this getView() is called with previously used 
        //  header view, i.e R.id.layout_header
        LinearLayout convertView_header = null;
        LinearLayout convertView_parent_item = null;  //TODO: see how it is improving the performance, as I stated below...
        if (convertView != null) {
            convertView_header = // Non-null if called with header view.
                    (LinearLayout) convertView.findViewById(R.id.layout_header);
            convertView_parent_item = // Non-null if called with parent_item view.
                    (LinearLayout) convertView.findViewById(R.id.layout_parent_item);
        }

        Object ob = realValues.get(position);
        String[] stRow = null;
        HashMap<String, String> hmRow = null;
        boolean isHeader = false;
        boolean isParentItem = false;
        String parentItemURL = null;
        String parentItemText = null;
        if (ob instanceof String[]) {
            stRow = (String[]) ob;
            if (stRow[0] == Dot3Application.HEADER_INDICATOR) {
                isHeader = true;
            }
            else if (stRow[0] == Dot3Application.PARENT_ITEM_INDICATOR) {
                isParentItem = true;
                parentItemURL = stRow[1];
                parentItemText = stRow[2];
            }
        }
        else if (ob instanceof HashMap<?, ?>) {
            hmRow = (HashMap<String, String>) ob;
            // Note, we store the status of the list row as the HashMap's value for the null key.
            // TODO: Can we also store Firebase node's keys as the HashMap's value for the HashMap's null key?! No collision?!! Do it then...
            if (hmRow.get(null) == Dot3Application.HEADER_INDICATOR) {
                isHeader = true;
            }
            else if (hmRow.get(null) == Dot3Application.PARENT_ITEM_INDICATOR) {
                isParentItem = true;
                parentItemURL = hmRow.get(Dot3Application.PARENT_ITEM_URL_HASHMAPKEY);
                parentItemText = hmRow.get(Dot3Application.PARENT_ITEM_TEXT_HASHMAPKEY);
            }
        }

        ViewHolder holder;

        if (isHeader)
        {
            // Improving the performance!
            if (convertView == null || convertView_header == null)
            {
                convertView = mInflater.inflate(R.layout.list_header, parent, false);
                holder = new ViewHolder();
                holder.text1 = (TextView) convertView.findViewById(R.id.text1);
                holder.text2 = (TextView) convertView.findViewById(R.id.text2);
                holder.text3 = (TextView) convertView.findViewById(R.id.text3);
                holder.imageView1 = (ImageView) convertView.findViewById(R.id.imageView1);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
        }
        else if (isParentItem)
        {
            // Improving the performance!
            if (convertView == null || convertView_parent_item == null)
            {
                convertView = mInflater.inflate(listParentItemlayoutId, parent, false);
                holder = new ViewHolder();
                holder.text1 = (TextView) convertView.findViewById(R.id.text1);
                holder.text2 = (TextView) convertView.findViewById(R.id.text2);
                holder.text3 = (TextView) convertView.findViewById(R.id.text3);
                holder.imageView1 = (ImageView) convertView.findViewById(R.id.imageView1);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // Set parent item content.
            holder.text1.setText(Dot3Application.PARENT_ITEM_INDICATOR);
            holder.text2.setText(parentItemURL); // urlIdx
            if (holder.text3 != null) {
                holder.text3.setText(parentItemText);
            }

            // Set bitmap to the list item's image view.
            if (holder.imageView1 != null) {
                if (parentItemBitmap != null) {
                    CustomArrayAdapter.setScaledBitmap(holder.imageView1, parentItemBitmap);
                } else {
                    Log.e("CustomArrayAdapter", "findViewById(R.id.imageView1) returned null");
                }
            }

        }
        else // Neither a parent item nor a header, but just a regular data.
        {
            // Improving the performance!
            if (convertView == null || convertView_header != null)
            {
                if (position == 0)
                    convertView = mInflater.inflate(R.layout.viewcats_list_first_item_layout, parent, false);
                else
                    convertView = mInflater.inflate(listItemlayoutId, parent, false);

                holder = new ViewHolder();
                holder.text1 = (TextView) convertView.findViewById(R.id.text1);
                holder.text2 = (TextView) convertView.findViewById(R.id.text2);
                holder.text3 = (TextView) convertView.findViewById(R.id.text3);
                holder.imageView1 = (ImageView) convertView.findViewById(R.id.imageView1);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ///////////////////////////
            // Set regular row content.
            String imageURL;
            if (ob instanceof String[]) {

                // Note: Scrolling efficiency seems to be OK ... But there is no method TextView.isSingleLine() - how can I get this attribute, to avoid )unnecessary) setting it on every row?
                //if ( text1.isSingleLine() != myApp.AreTitlesSingleLine ) {
                holder.text1.setSingleLine( myApp.AreTitlesSingleLine );
                //}
                //if ( text2.isSingleLine() != myApp.AreURLsSingleLine ) {
                holder.text2.setSingleLine(myApp.AreURLsSingleLine);
                //}

                holder.text2.setText(stRow[1]); // image URL
                if (holder.text3 != null) {
                    holder.text3.setText(stRow[2]);
                }
                holder.text1.setText(stRow[3]);

                imageURL = stRow[1];
            }
            else if (ob instanceof HashMap<?, ?>) {
                // See SimpleAdapter class for the approach.
                final String[] from = mFrom;
                final int[] to = mTo;
                final int count = to.length;

                for (int i = 0; i < count; i++) {
                    final View v = convertView.findViewById( to[i] );
                    final Object data = hmRow.get( from[i] );
                    String text = (data == null) ? "" : data.toString();
                    if (text == null) {
                        text = "";
                    }

                    if (v instanceof TextView) {
                        ((TextView) v).setText(text);
                    }
                    else {
                        // May be TODO in future if we need other widgets in the list row's layout.
                    }
                }

                imageURL = hmRow.get( imageURLMapKey );
            }
            else {
                throw new IllegalStateException("CustomArrayAdapter: list row type " + ob.getClass().getSimpleName() + " is not supported.");
            }
            ///////////////////////////

            // Convert the byte array to a bitmap and set it to the list item's image view.
            // bytesToIcon(view, position, imageURL);

            holder.imageView1.setImageResource(android.R.color.transparent);

            //[2015.09.28][Stelian] Bug fix of loading image
            ImageLoader.getInstance().displayImage(imageURL, holder.imageView1, options);
        }
        
        return convertView;
    }


    void bytesToIcon(View view, int position, String imageURL) {
        String tag = "CustomArrayAdapter.bytesToIcon";

        ImageView imageView = (ImageView) view.findViewById(R.id.imageView1);

        int indexToUse = -1;
        if (bitmaps[position] != null) {
            //Log.w(tag, "bitmap is not null");
            indexToUse = position;
        } else {
            //Log.w(tag, "bitmap is null");

            // // Experimental fix for the timing issue of icon rendering, see how it's set in prepareBitmaps()
            //indexToUse = firstNonNullBitmapIndex;

            // Leave indexToUse == -1, to indicate that bitmaps[position] == null
        }

        if (imageView != null) {
            // Check if we already cached an image of the list - very simple so far
            if (indexToUse >= 0 && indexToUse < bitmaps.length
                    && bitmaps[indexToUse] != null)
            {
                CustomArrayAdapter.setScaledBitmap(imageView, bitmaps[indexToUse]);
            } else {
                // The indexToUse == -1, i.e. bitmaps[position] == null, likely because the Bitmap download isn't completed yet
                // So, start downloading Bitmap now.

                // Loading a Bitmap here from web by image's URL (if the URL is non-null and non-empty)
                // Do it either in prepareBitmaps() with further handling download complete notification,
                // or just do it in bytesToIcon()

//                new DownloadImageTask(imageView, position, bitmaps, bitmapsUnscaled, targetImageViewDp)
//                        .execute(imageURL); //test: .execute("http://23.253.36.191/PublicForImages/20150630225319.jpg");

                 // // Else just show placeholderIcon
                //setScaledBitmap(imageView, placeholderIcon);
            }
        } else {
            // It's an issue if we are here: cannot get an ImageView.
            Log.e(tag, "findViewById(R.id.imageView1) returned null");
        }
    }



    // It's the 1st part of scaling Bitmap, see setScaledBitmap() for the 2nd part.
    // http://stackoverflow.com/questions/8232608/fit-image-into-imageview-keep-aspect-ratio-and-then-resize-imageview-to-image-d
    static public Bitmap scaleBitmap(Bitmap bitmap, float targetImageViewDp, boolean scaleDownOnly)
    {
        if (bitmap == null) {
            return bitmap;
        }

//        // Get the ImageView and its bitmap
//        ImageView view = (ImageView) rootView.findViewById(R.id.imageView1);
//        Drawable drawing = view.getDrawable();
//        if (drawing == null) {
//            return; // Checking for null & return, as suggested in comments
//        }
//        Bitmap bitmap = ((BitmapDrawable)drawing).getBitmap();

        // Get current dimensions AND the desired bounding box
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // The dpToPx() conversion gives inconsistent result between devices of
        // different resolution, e.g. old and new devices ...
        //int bounding = dpToPx( targetImageViewDp );
        //float boundingFloat = dpToPxFloat( targetImageViewDp );
        // ... however, not using the dpToPx() conversion givers consistent results
        // between devices, so we just use it:
        float boundingFloat = targetImageViewDp;

        //Log.i("Test", "original width = " + Integer.toString(width));
        //Log.i("Test", "original height = " + Integer.toString(height));
        //Log.i("Test", "bounding = " + Integer.toString(bounding));

        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        //float xScale = ((float) bounding) / width;
        //float yScale = ((float) bounding) / height;
        float xScale = boundingFloat / width;
        float yScale = boundingFloat / height;
        float scale
                //= (xScale <= yScale) ? xScale : yScale;
                = xScale; // Let's always scale by image's width.
        //Log.i("Test", "xScale = " + Float.toString(xScale));
        //Log.i("Test", "yScale = " + Float.toString(yScale));
        //Log.i("Test", "scale = " + Float.toString(scale));

        if (scaleDownOnly && scale > 1.0) {
            return bitmap; // For efficiency, don't scale up, if not required.
        }

        // Create a matrix for the scaling and add the scaling data
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap and convert it to a format understood by the ImageView
        // Crop the image's height, supposing the width is OK and the cropped height is to be width/2,
        // while initial scaled height might be too big.
        int newHeight = Math.min(width/2, height);
        int newY = (height - newHeight)/2;
        //Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, newY, width, newHeight, matrix, true);
        return scaledBitmap;
    }
    // It's the 2nd part of scaling Bitmap, see scaleBitmap() for the 1st part.
    // http://stackoverflow.com/questions/8232608/fit-image-into-imageview-keep-aspect-ratio-and-then-resize-imageview-to-image-d
    static public void setScaledBitmap(ImageView imageView, Bitmap scaledBitmap)
    {
        int width;
        int height;
        width = scaledBitmap.getWidth(); // re-use
        height = scaledBitmap.getHeight(); // re-use

        BitmapDrawable result = new BitmapDrawable(scaledBitmap);
        //Log.i("Test", "scaled width = " + Integer.toString(width));
        //Log.i("Test", "scaled height = " + Integer.toString(height));

        // Apply the scaled bitmap
        // What is the difference between ImageView.setImageBitmap() and ImageView.setImageDrawable()?
        // http://stackoverflow.com/questions/12001793/android-imageview-setimagebitmap-vs-setimagedrawable
        // "There is no difference between the two internally setImageBitmap is calling setImageDrawable."
        imageView.setImageDrawable(result);

        // Now change ImageView's dimensions to match the scaled image
        //LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        // ... using ViewGroup, not to depend on particular layout like LinearLayout ... is it correct?
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.width = width;
        params.height = height;
        imageView.setLayoutParams(params);

        //Log.i("Test", "done");
    }
    private int dpToPx(int dp) {
        return dpToPx((float)dp);
    }
    private int dpToPx(float dp)
    {
        float density = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    }
    private float dpToPxFloat(float dp)
    {
        float density = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return dp * density;
    }

}



