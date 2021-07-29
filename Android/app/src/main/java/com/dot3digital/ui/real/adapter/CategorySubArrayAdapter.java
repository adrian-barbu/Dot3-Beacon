package com.dot3digital.ui.real.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dot3digital.R;
import com.dot3digital.framework.model.EntryForViewCat;
import com.dot3digital.ui.real.Shared;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * @description  Category Sub Adapter
 *
 * @author       Stelian
 *
 * @param <T>
 */
public class CategorySubArrayAdapter<T> extends ArrayAdapter<Object>
{
    private Context mContext;
    private LayoutInflater mInflater;

    /**
     * ViewEntry Holder
     */
    private static class ViewHolder {
        public TextView tvHeadLine;
        public TextView tvText;
        public ImageView ivImage;
    }

    public CategorySubArrayAdapter(Context context) {
        super(context, R.layout.category_sub_list_item);

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
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.category_sub_list_item, parent, false);

            holder = new ViewHolder();
            holder.tvHeadLine = (TextView) convertView.findViewById(R.id.tvHeadLine);
            holder.tvText = (TextView) convertView.findViewById(R.id.tvText);
            holder.ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Show Data
        Object ob = getItem(position);

        // Set regular row content.
        String imageURL;
        if (ob instanceof EntryForViewCat) {
            EntryForViewCat item = (EntryForViewCat) ob;

            holder.tvHeadLine.setText(item.getHeadline());
            holder.tvText.setText(item.getTeaserText());

            // [2015.10.12][Stelian] Fix bug #33 (Support for two-image)
            if (item.getDisplayType().contains("two-images"))
                imageURL = item.getInitialImage();
            else
                imageURL = item.getImage();

            holder.ivImage.setImageResource(android.R.color.transparent);

            //[2015.09.28][Stelian] Bug fix of loading image
            ImageLoader.getInstance().displayImage(imageURL, holder.ivImage, Shared.gImageOption);
        }
        else {
            throw new IllegalStateException("CategoryArrayAdapter: list row type " + ob.getClass().getSimpleName() + " is not supported.");
        }

        return convertView;
    }
}



