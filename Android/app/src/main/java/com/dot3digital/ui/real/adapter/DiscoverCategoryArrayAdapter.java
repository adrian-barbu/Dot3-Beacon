package com.dot3digital.ui.real.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dot3digital.R;
import com.dot3digital.framework.model.ViewCats;
import com.dot3digital.ui.real.Shared;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * @description Discover Category List Adapter
 *
 * @author      Stelian
 *
 * @param <T>
 */
public class DiscoverCategoryArrayAdapter<T> extends ArrayAdapter<Object>
{
    private Context mContext;
    private LayoutInflater mInflater;

    /**
     * ViewEntry Holder
     */
    private static class ViewHolder {
        public View rootView;
        public TextView tvTagline;
        public TextView tvCatName;
        public TextView tvTeaserText;
        public ImageView ivImage;
    }

    public DiscoverCategoryArrayAdapter(Context context) {
        super(context, R.layout.viewcats_list_item_layout);

        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /*
     * Set List Item Datas
     *
     */
    public void setData(ArrayList<T> data) {
        clear();
        if (data != null) {
            addAll(data);
        }
    }

    @Override
    public int getItemViewType(int position) {
        // Define a way to determine which layout to use, here it's just evens and odds.
        return (position == 0) ? 0 : 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2; // Count of different layouts
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null)
        {
            if (getItemViewType(position) == 0)
                convertView = mInflater.inflate(R.layout.discover_list_item_1, parent, false);
            else
                convertView = mInflater.inflate(R.layout.discover_list_item_2, parent, false);

            holder = new ViewHolder();
            holder.tvTagline = (TextView) convertView.findViewById(R.id.tvTagline);
            holder.tvCatName = (TextView) convertView.findViewById(R.id.tvCatName);
            holder.tvTeaserText = (TextView) convertView.findViewById(R.id.tvTeaserText);
            holder.ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Show Data
        Object ob = getItem(position);

        // Set regular row content.
        String imageURL;
        if (ob instanceof ViewCats) {
            ViewCats item = (ViewCats) ob;

            if (holder.tvTagline != null)
                holder.tvTagline.setText(item.getTagline());

            holder.tvCatName.setText(item.getName());
            holder.tvTeaserText.setText(item.getTeaserText());

            imageURL = item.getImage();

            //[2015.09.28][Stelian] Bug fix of loading image
            ImageLoader.getInstance().displayImage(imageURL, holder.ivImage, Shared.gImageOption);
        }
        else {
            throw new IllegalStateException("CategoryArrayAdapter: list row type " + ob.getClass().getSimpleName() + " is not supported.");
        }

        return convertView;
    }
}



