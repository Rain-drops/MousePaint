package com.sgj.john.mousepaint;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sgj.john.listener.CallbackListener;
import com.sgj.john.mousepaint.constants.Code;
import com.sgj.john.mousepaint.constants.Constant;
import com.sgj.john.mousepaint.dao.AppDao;
import com.sgj.john.mousepaint.events.RegisterEvent;
import com.sgj.john.mousepaint.model.RegisterModel;
import com.sgj.john.mousepaint.utils.EmptyUtils;
import com.sgj.john.mousepaint.utils.InputCheckUtils;
import com.sgj.john.mousepaint.utils.StatusBarCompat;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by John on 2015/12/30.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener{

    public static final String TAG = RegisterActivity.class.getSimpleName();

    private Context mContext;
    @Bind(R.id.re_btn_sure)
    Button re_btn_sure;
    @Bind(R.id.re_et_user_email)
    EditText re_et_user_email;
    @Bind(R.id.re_et_user_password)
    EditText re_et_user_password;
    @Bind(R.id.re_et_user_re_password)
    EditText re_et_user_re_password;

    private String email = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mContext = this;

        ButterKnife.bind(this);

        init();

    }

    private void init() {
        StatusBarCompat.compat(this, getResources().getColor(R.color.brownness));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("注册");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        re_btn_sure.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.re_btn_sure:
                checkInputFindPassword(re_et_user_email.getText().toString().trim(),
                        re_et_user_password.getText().toString().trim(),
                        re_et_user_re_password.getText().toString().trim());
                break;
        }
    }

    /**
     *
     * @param account
     * @param password
     * @param comparePassword
     */
    private void checkInputFindPassword(String account, String password, String comparePassword) {
        if(EmptyUtils.emptyOfString(account))
        {
            toastMsg("邮箱不能为空");
            return;
        }
        else if(!InputCheckUtils.isValidEmail(account))
        {
            toastMsg("请输入正确的邮箱");
            return;
        }
        else if(InputCheckUtils.isEmpty(password))
        {
            toastMsg("新密码不能为空");
            return;
        }
        else if(InputCheckUtils.isEmpty(comparePassword))
        {
            toastMsg("重复密码不能为空");
            return;
        }
        else if(!InputCheckUtils.compareIsEqual(password, comparePassword))
        {
            toastMsg("两次密码输入不一致");
            return;
        }
        else if(!InputCheckUtils.checkInputRangeIsConform(password,3,15) || !InputCheckUtils.checkInputRangeIsConform(comparePassword,3,15))
        {
            toastMsg("请填写3-15位密码");
            return;
        }
        email = account;

        AppDao.getInstance().userRegister(email, password, new CallbackListener<RegisterModel>(){
            @Override
            public void onError(Exception e) {
                super.onError(e);
                Log.i(TAG, "" + e);
            }

            @Override
            public void onSuccess(RegisterModel result) {
                super.onSuccess(result);
                Log.i(TAG, result.ErrCode);
                int status = Integer.valueOf(result.ErrCode);
                toastMsg(Code.getRegisterMsg(status));
                if(status == Constant.SUCCESS){
                    EventBus.getDefault().post(new RegisterEvent(email));
                    RegisterActivity.this.finish();
                }
            }

            @Override
            public void onStringResult(String result) {
                super.onStringResult(result);
            }
        });

    }

    private void toastMsg(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {

        ButterKnife.unbind(this);
        super.onDestroy();

    }
}
