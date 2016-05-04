package com.sgj.john.mousepaint.model;

import android.os.UserManager;

/**
 * {"ErrCode":"0","ErrMsg":"0","Return":{"Id":481,"NickName":null,"Email":"929178@qq.com","Phone":null,"RegFromType":0,"Avatar":"/Content/images/touxi.jpg"},"Costtime":"4","IsError":false,"Message":null}
 * Created by John on 2015/12/30.
 */
public class LoginModel {
    public String ErrCode;
    public String ErrMsg;
    public UserMessage Return;

    public class UserMessage{
        public String Email;
    }
}
