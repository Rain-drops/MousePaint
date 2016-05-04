package com.sgj.john.mousepaint.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

/**
 * Created by John on 2016/1/21.
 */
public class NormalGridView extends GridView {

    private static final String TAG = "NormalGridView";

    private boolean isAutoSetNumColumns = true;//自动设置列数
    private int horizontalSpacing = 0; //横向间距
    private int columnWidth = 0;
    private boolean isScroll = false;

    public NormalGridView(Context context) {
        super(context);
    }

    public NormalGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NormalGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        if(isScroll){
            int gridViewWidth = 0; //网格视图宽度
            int adapterCount = adapter.getCount();
            if(columnWidth == 0){
                for(int i = 0; i < adapter.getCount(); i++){
                    View rowView = adapter.getView(i, null, null);// 用来刷新它所在的ListView, 在每一次item从屏幕外滑进屏幕内的时候，或者程序刚开始的时候创建第一屏item的时候

                    //测量视图
                    measureView(rowView);

                    gridViewWidth +=rowView.getMeasuredWidth();
                }
            }
            else {
                gridViewWidth += columnWidth * adapterCount;
            }

            if(isAutoSetNumColumns){
                gridViewWidth += this.getPaddingLeft() + this.getPaddingRight();
                gridViewWidth += this.horizontalSpacing + (adapterCount - 1);
                this.setColumnWidth(adapterCount);
            }
            this.setLayoutParams(new LinearLayout.LayoutParams(gridViewWidth, ViewGroup.LayoutParams.WRAP_CONTENT) );
        }
        super.setAdapter(adapter);
    }

    private void measureView(View child) {

        ViewGroup.LayoutParams p = child.getLayoutParams();
        if(p == null){
            p = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec ;
        if(lpHeight > 0){
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        }else{
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    @Override
    public void setNumColumns(int numColumns) {

        isAutoSetNumColumns = false;

        super.setNumColumns(numColumns);
    }

    /**
     * View本身大小多少，这由onMeasure()决定
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //measureSpec设置合适的布局大小
        //MeasureSpec.EXACTLY：父视图希望子视图的大小应该是specSize中指定的。
        //MeasureSpec.AT_MOST：子视图的大小最多是specSize中指定的值，也就是说不建议子视图的大小超过specSize中给定的值。
        //MeasureSpec.UNSPECIFIED：我们可以随意指定视图的大小。
        Log.e(TAG, "onMeasure----> : widthMeasureSpec = " + widthMeasureSpec + " ; heightMeasureSpec = " + heightMeasureSpec);
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        Log.i(TAG, "--- setOnItemClickListener ---");
        super.setOnItemClickListener(listener);
    }

    @Override
    public void setHorizontalSpacing(int horizontalSpacing) {
        this.horizontalSpacing = horizontalSpacing;
        super.setHorizontalSpacing(horizontalSpacing);
    }

    @Override
    public void setColumnWidth(int columnWidth) {
        this.columnWidth = columnWidth;
        super.setColumnWidth(columnWidth);
    }

    /**
     * 设置启用水平滚动
     * @param isScroll
     */
    public void setEnableHorizontalScroll(boolean isScroll)
    {
        this.isScroll = isScroll;
    }

    /**
     * View在ViewGroup中的位置如何，这由onLayout()决定
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    /**
     * 绘制View，onDraw()定义了如何绘制这个View
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
