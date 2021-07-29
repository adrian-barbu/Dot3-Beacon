// See: http://thiranjith.com/2011/07/15/actionbar-design-pattern-example-for-android/

package com.dot3digital.ui.actionbar.widget;
//package com.thira.examples.actionbar.widget;

import com.dot3digital.R;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ActionBarOldCustom extends RelativeLayout {

    /**
     * Reusable {@link LayoutInflater}
     */
    private LayoutInflater mInflater;
    /**
     * Holds the home-icon logo
     */
    private ImageView mLogoView;
    /**
     * Displays the {@link Activity} text
     */
    private TextView mTitleView;
    /**
     * Represents the progress bar (i.e. busy-icon)
     */
    private ProgressBar mProgress;
    /**
     * Contains the ActionIcons.
     */
    private LinearLayout mActionIconContainer;

    public ActionBarOldCustom(Context context, AttributeSet attrs) {
        super(context, attrs);

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        RelativeLayout barView = (RelativeLayout) mInflater.inflate(R.layout.actionbar, null);
        addView(barView);

        mLogoView = (ImageView) barView.findViewById(R.id.actionbar_home_logo);
        ProgressBar progressBar = (ProgressBar) barView.findViewById(R.id.actionbar_progress); 
        setProgressBar(progressBar);
        mTitleView = (TextView) barView.findViewById(R.id.actionbar_title);
        mActionIconContainer = (LinearLayout) barView.findViewById(R.id.actionbar_actionIcons);
    }

    public void setHomeLogo(int resId) {
        setHomeLogo(resId, null);
    }
    
    public void setHomeLogo(int resId, OnClickListener onClickListener) {
        getLogoView().setImageResource(resId);
        getLogoView().setVisibility(View.VISIBLE);
        getLogoView().setOnClickListener(onClickListener);
        if (onClickListener != null) {
        }
    }

    public void setTitle(CharSequence title) {
        mTitleView.setText(title);
    }

    public void setTitle(int resid) {
        mTitleView.setText(resid);
    }

    public void showProgressBar() {
        setProgressBarVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        setProgressBarVisibility(View.GONE);
    }

    /**
     * Adds ActionIcons to the ActionBarOldCustom (adds to the left-end)
     * 
     * @param iconResourceId
     * @param onClickListener to handle click actions on the ActionIcon.
     */
    public void addActionIcon(int iconResourceId, OnClickListener onClickListener) {
        // Inflate
        View view = mInflater.inflate(R.layout.actionbar_icon, mActionIconContainer, false);
        ImageButton imgButton = (ImageButton) view.findViewById(R.id.actionbar_item);
        imgButton.setImageResource(iconResourceId);
        imgButton.setOnClickListener(onClickListener);

        mActionIconContainer.addView(view, mActionIconContainer.getChildCount());
    }

    /**
     * Remove the action icon from the given index (0 based)
     * 
     * @param index
     * @return <code>true</code> if the item was removed
     */
    public boolean removeActionIconAt(int index) {
        int count = mActionIconContainer.getChildCount();
        if (count > 0 && index >= 0 && index < count) {
            mActionIconContainer.removeViewAt(index);
            return true;
        }
        return false;
    }

    /**
     * @return <code>true</code> if the progressbar is visible
     * @see View#VISIBLE
     */
    public boolean isProgressBarVisible() {
        return getProgressBar().getVisibility() == View.VISIBLE;
    }

    /**
     * Set the enabled state of the progress bar.
     * 
     * @param One of {@link View#VISIBLE}, {@link View#INVISIBLE}, or {@link View#GONE}.
     */
    private void setProgressBarVisibility(int visibility) {
        getProgressBar().setVisibility(visibility);
    }
    
    public ImageView getLogoView() {
        return mLogoView;
    }
    public void setLogoView(ImageView mLogoView) {
        this.mLogoView = mLogoView;
    }
    public ProgressBar getProgressBar() {
        return mProgress;
    }
    public void setProgressBar(ProgressBar mProgress) {
        this.mProgress = mProgress;
    }

}

