package com.sgj.john.mousepaint.constants;

/**
 * Created by John on 2016/1/5.
 */
public class Code {
    public static String getRegisterMsg(int statusCode){
        String msg = "系统内部错误";
        switch (statusCode) {
            case 0:
                msg = "注册成功";
                break;
            case -1:
                msg = "注册失败";
                break;
            case -2:
                msg = "邮箱已被注册";
                break;
        }
        return msg;


    }
}
