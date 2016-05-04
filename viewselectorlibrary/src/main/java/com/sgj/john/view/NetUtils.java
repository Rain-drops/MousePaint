package com.sgj.john.view;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * 网络工具类
 * Created by John on 2016/1/8.
 */
public class NetUtils {

    /**
     * 检验网络连接 并toast提示
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context){

        /*
         * ConnectivityManager主要管理和网络连接相关的操作
         * 相关的TelephonyManager则管理和手机、运营商等的相关信息；WifiManager则管理和wifi相关的信息。
         * 想访问网络状态，首先得添加权限<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
         * NetworkInfo类包含了对wifi和mobile两种网络模式连接的详细描述,通过其getState()方法获取的State对象则代表着
         * 连接成功与否等状态。
         *
         */
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // 获取代表联网状态的NetWorkInfo对象
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        if(info == null || info.isAvailable()){
            //当前网络不可用
            Toast.makeText(context, "没有可用网络", Toast.LENGTH_SHORT).show();
            return false;
        }
        boolean wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        return true;

    }
}
