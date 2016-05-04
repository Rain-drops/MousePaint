package com.sgj.john.mousepaint.contral.control;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 *  精选漫画中与横滑List对应的下标List
 * Created by John on 2016/1/18.
 */
public class RhythmLayout extends HorizontalScrollView {
    private Context mContext;

    private RhythmAdapter mAdapter;

    private Handler mHandler;

//    private ShiftMonitorTimer mTimer;

    private IRhythmItemListener mListener;


    private LinearLayout mLinearLayout;

    private float mItemWidth;

    private int mMaxTranslationHeight;

    private int mIntervalHeight;

    private int mCurrentItemPosition;

    private int mEdgeSizeForShiftRhythm;

    private int mScreenWidth;

    private long mFingerDownTime;

    private int mScrollStartDelayTime;

    private int mLastDisplayItemPosition;

    public RhythmLayout(Context context) {
        super(context);
    }

    public RhythmLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RhythmLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void showRhythmAtPosition(int position) {

    }

    public void setScrollRhythmStartDelayTime(int scrollRhythmStartDelayTime) {
        this.mScrollStartDelayTime = scrollRhythmStartDelayTime;
    }

    public Object getRhythmItemWidth() {
        return mItemWidth;
    }

    public void setRhythmListener(IRhythmItemListener rhythmListener) {
        this.mListener = rhythmListener;
    }
}
