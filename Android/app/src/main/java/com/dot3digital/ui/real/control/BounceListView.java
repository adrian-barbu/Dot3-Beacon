package com.dot3digital.ui.real.control;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.AbsListView;
import android.widget.EdgeEffect;
import android.widget.ListView;

import java.lang.reflect.Field;

/**
 * @description Customized List ViewEntry
 *
 * @author      Stelian
 */
public class BounceListView extends ListView {

    private static final String TAG = "BounceListView";

    private static final String TOP_EDGE_EFFECT_FIELD = "mEdgeGlowTop"; // Variable to change if field changes.

    private static final int DEFAULT_MAX_Y = 150;

    private int mMaxOverScrollY = DEFAULT_MAX_Y;

    private boolean didStartOverScroll = false;
    private boolean didFinishOverScroll = false;
    private boolean isClamped;

    private EdgeEffect mTopEdgeEffect;

    private OverScrolledListener mListener;

    public interface OverScrolledListener {
        void overScrolled(int scrollY, int maxY, boolean exceededOffset, boolean didFinishOverScroll);
    }

    public BounceListView(Context context) {
        super(context);
        init();
    }

    public BounceListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BounceListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setOverScrollListener(OverScrolledListener listener) {
        mListener = listener;
    }

    public void setOverScrollOffsetY(int offset) {
        mMaxOverScrollY = offset;
    }

    private void init() {
        setFadingEdgeLength(0);
        setVerticalFadingEdgeEnabled(false);
        getPrivateFieldMembers();
    }

    private void getPrivateFieldMembers() {
        try {
            mTopEdgeEffect = getTopEdgeEffect();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "The Reflection Failed! Check if the field name changed in AbsListView.java inside the AOSP!");
        }
    }

    private EdgeEffect getTopEdgeEffect() throws NoSuchFieldException, IllegalAccessException {
        Field f = AbsListView.class.getDeclaredField(TOP_EDGE_EFFECT_FIELD);
        if (f != null) {
            f.setAccessible(true);
            return (EdgeEffect) f.get(this);
        }
        return null;
    }

    private void reset() {
        smoothScrollToPosition(0);
        didFinishOverScroll = true;
        didStartOverScroll = false;
        mListener.overScrolled(0, mMaxOverScrollY, false, true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                if (didStartOverScroll) {
                    if (isClamped) {
                        reset();
                        return true;
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, mMaxOverScrollY, isTouchEvent);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);

        if (scrollY < 0 && !didStartOverScroll) {

            didStartOverScroll = true;
            didFinishOverScroll = false;
        }

        if (scrollY == 0 && didStartOverScroll) {
            didStartOverScroll = false;
            didFinishOverScroll = true;
        }

        if (mListener != null && scrollY < 1) {
            mListener.overScrolled(Math.abs(scrollY), mMaxOverScrollY, clampedY, didFinishOverScroll);
        } else {
            Log.v(TAG, "No scroll listener set");
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mTopEdgeEffect != null) {
            mTopEdgeEffect.finish();
        }
    }
}
