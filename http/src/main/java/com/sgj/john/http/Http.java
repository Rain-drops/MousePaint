package com.sgj.john.http;

import com.sgj.john.listener.CallbackListener;

import java.util.Map;

/**
 * Created by John on 2015/12/30.
 */
public class Http extends BaseHttp{
    /**
     * get 请求
     * @param url
     * @param listener
     */
    public static void get(String url, CallbackListener<?> listener){
        getInstance().baseGet(url, listener);
    }

    /**
     * post 请求
     * @param url
     * @param params
     * @param listener
     */
    public static void post(String url, Map<String, String> params, CallbackListener<?> listener){
        getInstance().basePost(url, params, listener);
    }

    /**
     * 无参post 请求
     * @param url
     * @param listener
     */
    public static void post(String url, CallbackListener<?> listener){
        getInstance().basePost(url, null, listener);
    }

    /**
     * 取消request
     */
    public static void cancel(String url){
        getInstance().getmOkHttpClient().cancel(url);
    }

}
