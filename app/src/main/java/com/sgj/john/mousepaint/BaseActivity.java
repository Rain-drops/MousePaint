package com.sgj.john.mousepaint;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by John on 2015/12/30.
 */
public class BaseActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void closeProDialog(){
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }

    public void showProDialog(){
        if(progressDialog != null){

            progressDialog.show();
        }
    }

    public void showProDialog(String str){
        if(progressDialog != null){
            progressDialog.setMessage(str);
            progressDialog.show();

        }
    }
}
