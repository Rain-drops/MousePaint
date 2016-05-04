package com.sgj.john.http;

import android.app.DownloadManager;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sgj.john.listener.CallbackListener;
import com.sgj.john.util.GenericsUtils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.Map;
import java.util.Set;
import java.util.logging.LogRecord;

/**
 * Created by John on 2015/12/30.
 */
public class BaseHttp<T> {
    private static final String TAG="BaseHttp";
    private static BaseHttp mBaseHttp;
    private Gson mGson;
    private OkHttpClient mOkHttpClient;
    private Handler mHandler;

    protected BaseHttp() {
        mGson = new Gson();
        mOkHttpClient = new OkHttpClient();
        mHandler = new Handler(Looper.getMainLooper());
        //cookie enabled
        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));//只接受原服务器Coolkie
    }
    protected static BaseHttp getInstance(){
        if(mBaseHttp == null){
            mBaseHttp = new BaseHttp();
        }
        return mBaseHttp;
    }

    protected OkHttpClient getmOkHttpClient() {
        return mOkHttpClient;
    }
    protected void baseGet(String url, CallbackListener<T> listener){
        Request request = getBaseRequest(url);
        doRequest(request, listener);
    }
    protected void basePost(String url, Map<String, String> params, CallbackListener<T> listener){
        if(params ==null){
            baseGet(url, listener);
            return;
        }
        FormEncodingBuilder builder = new FormEncodingBuilder();
        Set<Map.Entry<String, String>> entrySet = params.entrySet();
        for(Map.Entry<String, String> entry : entrySet){
            builder.add(entry.getKey(), entry.getValue());
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .tag(url)
                .build();
        doRequest(request, listener);

    }

    private void doRequest(final Request request, final CallbackListener<T> listener) {
        // .enqueue ---> 异步
        // .execute ---> 同步

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onError(e);
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String result = response.body().string();
                Log.e(TAG, "结果：" + result);
                if(isListenerNotNull(listener)){
                    listener.onStringResult(result);
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Class<T> clazz = GenericsUtils.getSuperClassGenricType(listener.getClass());
                            if(clazz == String.class){
                                if(isListenerNotNull(listener)){
                                    listener.onSuccess((T)result);
                                }
                            }else {
                                if (isListenerNotNull(listener)){
                                    Log.e(TAG, clazz.toString());
                                    Log.e(TAG, mGson.fromJson(result, clazz).toString());
                                    listener.onSuccess(mGson.fromJson(result, clazz));
                                }
                            }
                        } catch (Exception e) {
                            Log.i(TAG, "出错", e);
                            if (isListenerNotNull(listener)){
                                listener.onError(e);
                            }
                        }
                    }
                });
            }
        });
    }

    private Request getBaseRequest(String url) {
        Request request = new Request.Builder().url(url).tag(url).build();
        return request;
    }

    public static String getFileNameByUrl(String strUrl){
        if(TextUtils.isEmpty(strUrl)){
            try {
                return strUrl.substring(strUrl.lastIndexOf("/")+1,strUrl.length());
            }catch (Exception e){
                return strUrl;
            }
        }else {
            return strUrl;
        }
    }

    public Boolean isListenerNotNull(CallbackListener<T> listener){
        if(listener!=null){
            return true;
        }
        return false;
    }

}
