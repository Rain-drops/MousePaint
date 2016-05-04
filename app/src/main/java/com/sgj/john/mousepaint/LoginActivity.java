package com.sgj.john.mousepaint;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.sgj.john.listener.CallbackListener;
import com.sgj.john.mousepaint.constants.Constant;
import com.sgj.john.mousepaint.constants.SaveInfo;
import com.sgj.john.mousepaint.dao.AppDao;
import com.sgj.john.mousepaint.events.RegisterEvent;
import com.sgj.john.mousepaint.model.LoginModel;
import com.sgj.john.mousepaint.model.UserModel;
import com.sgj.john.mousepaint.utils.EmptyUtils;
import com.sgj.john.mousepaint.utils.SPUtils;
import com.sgj.john.mousepaint.utils.StatusBarCompat;
import static com.sgj.john.mousepaint.MosuePaintAppication.UserInfo;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by John on 2015/12/30.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = "LoginActivity";
    private Context mContext;

    // compile 'com.jakewharton:butterknife:7.0.1'
    @Bind(R.id.btn_login)
    Button btn_login;
    @Bind(R.id.btn_register)
    Button btn_register;
    @Bind(R.id.et_user_email)
    EditText et_user_email;
    @Bind(R.id.et_user_password)
    EditText et_user_pass;

    @Bind(R.id.tv_find_pass)
    TextView tv_find_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = this;
        // 主要功能是替代Intent,Handler,BroadCast在Fragment，Activity，Service，线程之间传递消息.优点是开销小，代码更优雅.(compile 'de.greenrobot:eventbus:2.4.0')
        //注册EventBus
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);

        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int mHeight = metrics.heightPixels;
        int mWidth = metrics.widthPixels;


        init();
    }

    private void init() {
        StatusBarCompat.compat(this, getResources().getColor(R.color.brownness));//应用版本判断
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);//suppport.v7
        getSupportActionBar().setTitle("登陆");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);// 给左上角图标的左边加上一个返回的图标

        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);

    }

    private void toastMsg(String str){
        Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.btn_login:
                showProDialog("登陆中...");
                login();
                break;
            case R.id.btn_register:
                intent.setClass(mContext, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_find_pass:

                break;

        }
    }

    private void login() {
        final String email = et_user_email.getText().toString().trim();
        final String pass = et_user_pass.getText().toString().trim();
        if(EmptyUtils.emptyOfString(email) || EmptyUtils.emptyOfString(pass)){
            toastMsg("邮箱或者密码不能为空");
            return;
        }

        AppDao.getInstance().userLogin(email, pass, new CallbackListener<LoginModel>(){
            @Override
            public void onError(Exception e) {
                super.onError(e);
                Log.i("sgj", "Exception----->" + e);
                toastMsg(Constant.NET_ERROR);
                closeProDialog();
            }

            @Override
            public void onSuccess(LoginModel result) {
                super.onSuccess(result);
                int status = Integer.valueOf(result.ErrCode);
                if(Constant.SUCCESS == status){
                    UserInfo = new UserModel();
                    UserInfo.email = result.Return.Email;
                    UserInfo.pass = pass;

                    SPUtils.saveObject(SaveInfo.KEY_LOGIN, UserInfo);
                    startActivity(new Intent(mContext, HomeActivity.class));
                    LoginActivity.this.finish();
                }else {
                    toastMsg(result.ErrMsg);
                }
                closeProDialog();
            }

            @Override
            public void onStringResult(String result) {
                super.onStringResult(result);
                Log.i("sgj","-res--->" +result );
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onEvent(RegisterEvent event){
        et_user_email.setText(event.email);

    }
}
