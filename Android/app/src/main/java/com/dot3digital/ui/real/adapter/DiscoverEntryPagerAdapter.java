package com.dot3digital.ui.real.adapter;

import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
public class DiscoverEntryPagerAdapter extends PagerAdapter {

    // Variables
    List<BaseModel> mEntries;       // EntriesForZone

    int mHeaderHeight;              // Header's Height

    Context mContext;
    LayoutInflater mInflater;
    boolean isImageBasedMode;                     // Indicator current view mode

    Animation mFadeInAnim, mFadeOutAnim;

    OnItemChangedListener mOnItemChangedListener;

    /**
     * Interface to interact with pager
     */
    public interface OnItemChangedListener {
        void onItemChanged(int position);
        void onItemToggled(int position, boolean isUp);
        void onRequestContentShow(int position, boolean show);
    }

    public DiscoverEntryPagerAdapter(Context context, int headerHeight) {
        super();

        mContext = context;
        mInflater = LayoutInflater.from(context);
        isImageBasedMode = true;
        mHeaderHeight = headerHeight;

        mFadeInAnim = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
        mFadeOutAnim = AnimationUtils.loadAnimation(mContext, R.anim.fade_out);
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

    boolean isTextShowed = false;
    int mHeightScreen;
    boolean isContentShowed = true;

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final View v = mInflater.inflate(R.layout.pager_item_discover_entry, container, false);

        // There are two sub view groups.
        // One is image based, another is text-based.
        // These will be replaced with animation when user tapped more info or back button

        final View layoutImageBased = (View) v.findViewById(R.id.layoutImageBased);
        ImageView ivImage = (ImageView) v.findViewById(R.id.ivImage);
        final TextView tvEntryHeadline = (TextView) v.findViewById(R.id.tvEntryHeadline);
        TextView tvTeaserText = (TextView) v.findViewById(R.id.tvTeaserText);
        final ImageView ivShowDetailToggle = (ImageView) v.findViewById(R.id.ivShowDetailToggle);

        final RelativeLayout  layoutContent = (RelativeLayout) v.findViewById(R.id.layoutContent);
        ViewGroup.LayoutParams layoutParams = layoutContent.getLayoutParams();

        final TextView tvEntryText = (TextView) v.findViewById(R.id.tvEntryText);

        // Get Data
        final EntryForZone entry = (EntryForZone) mEntries.get(position);

        tvEntryHeadline.setText(entry.getHeadline());
        tvTeaserText.setText(entry.getTeaserText());
        ImageLoader.getInstance().displayImage(entry.getImage(), ivImage, Shared.gImageOption);

        tvEntryText.setText(entry.getText());

        final View layoutHeadline = (View) v.findViewById(R.id.layoutHeadline);
        v.post(new Runnable() {
            @Override
            public void run() {
                mHeightScreen = v.getHeight();
            }
        });

        setInitialYPosition(v);

        // Resize Scroll View
        final ScrollView scrollView = (ScrollView) v.findViewById(R.id.scrollView);
        tvEntryText.post(new Runnable() {
            @Override
            public void run() {
                tvEntryText.setHeight(tvEntryText.getHeight() + mHeaderHeight);
            }
        });

        isTextShowed = false;
        isContentShowed = true;

        ivShowDetailToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // Get Headline's Top
                final float yContent = layoutContent.getY();

                if (!isTextShowed)  // To show
                {
                    Animation animation = new TranslateAnimation(0, 0, 0, mHeaderHeight - yContent);
                    animation.setDuration(500);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            layoutHeadline.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
                            ivShowDetailToggle.setImageResource(R.mipmap.ic_discover_toggle_down);
                            tvEntryHeadline.setTextColor(mContext.getResources().getColor(R.color.discover_header));
                            tvEntryHeadline.setText(mContext.getString(R.string.discover_hide_information));

                            if (mOnItemChangedListener != null)
                                mOnItemChangedListener.onItemToggled(position, true);

                            layoutContent.setDrawingCacheEnabled(true);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            layoutContent.setTranslationY(mHeaderHeight);

                            Animation anim = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);
                            anim.setDuration(1);
                            layoutContent.startAnimation(anim);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                    layoutContent.startAnimation(animation);
                }
                else    // To hide
                {
                    int heightHeadline = layoutHeadline.getHeight();
                    final int newY = mHeightScreen - heightHeadline;
                    int moveY = newY - mHeaderHeight;
                    Animation animation = new TranslateAnimation(0, 0, 0, moveY);
                    animation.setDuration(500);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            layoutHeadline.setBackgroundColor(mContext.getResources().getColor(R.color.discover_header));
                            ivShowDetailToggle.setImageResource(R.mipmap.ic_discover_toggle_up);

                            tvEntryHeadline.setTextColor(mContext.getResources().getColor(android.R.color.white));
                            tvEntryHeadline.setText(entry.getHeadline());

                            if (mOnItemChangedListener != null)
                                mOnItemChangedListener.onItemToggled(position, false);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            layoutContent.setY(newY);

                            Animation anim = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);
                            anim.setDuration(1);
                            layoutContent.startAnimation(anim);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {}
                    });
                    layoutContent.startAnimation(animation);
                }

                isTextShowed = !isTextShowed;
            }
        });

        container.addView(v, 0);
        v.setTag(position);

        // When click entry area, all of contents will be fade-out
        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isContentShowed)
                {
                    // Hide
                    mFadeOutAnim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {}
                        @Override
                        public void onAnimationRepeat(Animation animation) {}

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            layoutContent.setVisibility(View.INVISIBLE);
                        }
                    });
                    layoutContent.startAnimation(mFadeOutAnim);
                }
                else
                {
                    // Show
                    mFadeInAnim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {}
                        @Override
                        public void onAnimationRepeat(Animation animation) {}

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            layoutContent.setVisibility(View.VISIBLE);
                        }
                    });
                    layoutContent.startAnimation(mFadeInAnim);
                }

                // Send message to parent to show/hide main header
                if (mOnItemChangedListener != null)
                    mOnItemChangedListener.onRequestContentShow(position, isContentShowed);

                isContentShowed = !isContentShowed;
            }
        });
        return v;
    }

    public void setInitialYPosition(final View v) {
        final RelativeLayout layoutContent = (RelativeLayout) v.findViewById(R.id.layoutContent);
        final View layoutHeadline = (View) v.findViewById(R.id.layoutHeadline);
        layoutHeadline.post(new Runnable() {

            @Override
            public void run() {
                // Get width and height here
                // Get height of screen
                int vHeight = v.getHeight();
                int heightHeadline = layoutHeadline.getHeight();
                layoutContent.setY(v.getHeight() - layoutHeadline.getHeight());
            }
        });

        layoutHeadline.setBackgroundColor(mContext.getResources().getColor(R.color.discover_header));

        TextView tvEntryHeadline = (TextView) v.findViewById(R.id.tvEntryHeadline);
        tvEntryHeadline.setTextColor(mContext.getResources().getColor(android.R.color.white));

        ImageView ivShowDetailToggle = (ImageView) v.findViewById(R.id.ivShowDetailToggle);
        ivShowDetailToggle.setImageResource(R.mipmap.ic_discover_toggle_up);

        ScrollView scrollView = (ScrollView) v.findViewById(R.id.scrollView);
        scrollView.fullScroll(scrollView.FOCUS_UP);

        isTextShowed = false;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
