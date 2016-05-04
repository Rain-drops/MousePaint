package com.sgj.john.mousepaint;

import android.app.Application;

import com.sgj.john.mousepaint.model.UserModel;

/**
 * Created by John on 2015/12/30.
 */
public class MosuePaintAppication extends Application {

    /**用户信息*/
    public static UserModel UserInfo;
    public static MosuePaintAppication appication;

    @Override
    public void onCreate() {
        super.onCreate();
        appication = this;
    }
}
