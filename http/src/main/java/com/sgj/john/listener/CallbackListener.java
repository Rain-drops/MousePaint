package com.sgj.john.listener;

/**
 * Created by John on 2015/12/30.
 */
public class CallbackListener<T> extends BaseListener<T>{
    @Override
    public void onError(Exception e) {

    }

    @Override
    public void onSuccess(T result) {
        
    }

    @Override
    public void onStringResult(String result) {

    }

    @Override
    public void onDownloadFinish(String path) {

    }

    @Override
    public void onDownloadProgress(int progress) {

    }
}
