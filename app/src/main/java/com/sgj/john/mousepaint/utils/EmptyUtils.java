package com.sgj.john.mousepaint.utils;

import java.util.List;
import java.util.Map;

/**
 * Created by John on 2015/12/30.
 * 判断是否为空
 */
public class EmptyUtils {

    public static boolean emptyOfString(String params){
        if(params != null && params.length() > 0){
            return false;
        }
//        return true;
        return params.isEmpty();
    }

    public static boolean emptyOfList(List<?> params){
        if(params != null && params.size()>0){
            return false;
        }
//        return true;
        return params.isEmpty();
    }

    public static boolean emptyOfArray(Object params[]){
        if(params != null && params.length>0){
            return false;
        }
        return true;
    }

    public static boolean emptyOfObject(Object params){

        if(params != null ){
            return false;
        }
        return true;
    }

    public static <K, V>boolean emptyOfMap(Map<K, V> params){
        if(params != null && params.size()>0){
            return false;
        }
//        return true;
        return params.isEmpty();
    }
}
