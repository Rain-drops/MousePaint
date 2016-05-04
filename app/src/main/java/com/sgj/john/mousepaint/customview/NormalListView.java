package com.sgj.john.mousepaint.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by John on 2016/1/27.
 */
public class NormalListView extends ListView {
    private boolean isAutoSetNumColumns = true;
    private int horizontalSpacing = 0;
    private int columnWidth = 0;
    private boolean isScroll = false;

    public NormalListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec,expandSpec);
    }
}
