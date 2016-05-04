package com.sgj.john.mousepaint.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.sgj.john.mousepaint.MosuePaintAppication;

import java.util.UnknownFormatConversionException;

/**
 * Created by John on 2016/1/5.
 * 读取缓存信息
 */
public class SPUtils {
    private static final Context context = MosuePaintAppication.appication;
    private static final String APP_ID = "fuck_cjj";   //相当于文件名
    private static final Gson gson = new Gson();   //获取Gson

    SPUtils(){
        throw new UnsupportedOperationException();
    }

    /**
     * 缓存信息
     * @param cacheName	存入名字
     * @param obj	对象
     */
    public static void saveObject(String cacheName, Object obj){
        SharedPreferences.Editor e = context.getSharedPreferences(APP_ID, Context.MODE_APPEND).edit();
        String json = gson.toJson(obj);
        e.putString(cacheName, json);
        e.commit();
    }

    /**
     * 读取缓存信息
     * @param <T>
     * @param cacheName	缓存名
     * @param classes	对象类型
     * @param defaultValue	默认参数
     */
    public static <T> T getObject(String cacheName,Class<T> classes,T defaultValue){
        SharedPreferences s = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE);
        String jsonString = s.getString(cacheName, "");
        if(!"".equals(jsonString)){
            try {
                T o = (T)gson.fromJson(jsonString, classes);
                return o;
            }catch (Exception e){
                e.printStackTrace();
                return defaultValue;
            }
        }
        return defaultValue;
    }
    /**
     * 删除信息
     * @param cacheName 缓存名
     */
    public static void delObject(String cacheName){
        SharedPreferences s = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE);
        s.edit().remove(cacheName).commit();
    }
}
