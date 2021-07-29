package com.dot3digital.ui.real.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dot3digital.R;
import com.dot3digital.framework.model.BaseModel;
import com.dot3digital.framework.model.EntryForZone;
import com.dot3digital.ui.real.Shared;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * @description     Discover Entry Pager Adapter
 *
 * @author          Stelian
 */
public class DiscoverEntryPagerAdapterOld extends PagerAdapter {

    // Variables
    List<BaseModel> mEntries;       // EntriesForZone

    Context mContext;
    LayoutInflater mInflater;
    boolean isImageBasedMode;                     // Indicator current view mode

    OnItemChangedListener mOnItemChangedListener;

    /**
     * Interface to interact with pager
     */
    public interface OnItemChangedListener {
        void onItemChanged(int position);
    }

    public DiscoverEntryPagerAdapterOld(Context context) {
        super();

        mContext = context;
        mInflater = LayoutInflater.from(context);
        isImageBasedMode = true;
    }

    /**
     * Set Listener
     *
     * @param listener
     */
    public void setOnItemChangedListener(OnItemChangedListener listener) {
        mOnItemChangedListener = listener;
    }

    /**
     * Switch ViewEntry Mode
     */
    public void switchMode() {
        isImageBasedMode = !isImageBasedMode;
    }

    /**
     * Set Data
     *
     * @param entries
     */
    public void setData(List<BaseModel> entries) {
        mEntries = entries;
    }

    @Override
    public int getCount() {
        return (mEntries != null) ? mEntries.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View v = mInflater.inflate(R.layout.pager_item_discover_entry_old, container, false);

        // There are two sub view groups.
        // One is image based, another is text-based.
        // These will be replaced with animation when user tapped more info or back button

        final View layoutImageBased = (View) v.findViewById(R.id.layoutImageBased);
        ImageView ivImage = (ImageView) v.findViewById(R.id.ivImage);
        TextView tvEntryHeadline = (TextView) v.findViewById(R.id.tvEntryHeadline);
        TextView tvTeaserText = (TextView) v.findViewById(R.id.tvTeaserText);
        Button btnMoreInfo = (Button) v.findViewById(R.id.btnMoreInfo);

        final View  layoutTextBased = (View) v.findViewById(R.id.layoutTextBased);
        TextView tvEntryText = (TextView) v.findViewById(R.id.tvEntryText);
        Button btnBack = (Button) v.findViewById(R.id.btnBack);

        // Get Data
        EntryForZone entry = (EntryForZone) mEntries.get(position);

        tvEntryHeadline.setText(entry.getHeadline());
        tvTeaserText.setText(entry.getTeaserText());
        ImageLoader.getInstance().displayImage(entry.getImage(), ivImage, Shared.gImageOption);

        tvEntryText.setText(entry.getText());

        if (isImageBasedMode) {
            layoutImageBased.setVisibility(View.VISIBLE);
            layoutTextBased.setVisibility(View.GONE);
        } else {
            layoutImageBased.setVisibility(View.GONE);
            layoutTextBased.setVisibility(View.VISIBLE);
        }

        btnMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemChangedListener != null)
                    mOnItemChangedListener.onItemChanged(position);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemChangedListener != null)
                    mOnItemChangedListener.onItemChanged(position);
            }
        });

        container.addView(v, 0);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
