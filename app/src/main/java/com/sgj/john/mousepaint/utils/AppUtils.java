package com.sgj.john.mousepaint.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by John on 2016/1/19.
 */
public class AppUtils {

    /**
     * 屏幕显示尺寸
     * @param context
     * @return
     */
    public static DisplayMetrics getScreenDisplayMetrics(Context context){
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        return localDisplayMetrics;
    }

    public static int getDrawableIdByName(Context context, String drawableName){

        return context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());
    }
}
