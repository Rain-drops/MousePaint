package com.sgj.john.mousepaint;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.sgj.john.MaterialRefreshLayout;
import com.sgj.john.MaterialRefreshListener;
import com.sgj.john.http.Http;
import com.sgj.john.listener.CallbackListener;
import com.sgj.john.mousepaint.adapter.HomeRecyclerViewAdapter;
import com.sgj.john.mousepaint.constants.Constant;
import com.sgj.john.mousepaint.dao.AppDao;
import com.sgj.john.mousepaint.model.AllBookModels;
import com.sgj.john.mousepaint.url.HttpUrl;
import com.sgj.john.mousepaint.utils.FastOnClickUtils;
import com.sgj.john.mousepaint.utils.StatusBarCompat;
import com.sgj.john.view.ReLoadCallbackListener;
import com.sgj.john.view.ViewSelectorLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecommendActivity extends BaseActivity implements View.OnClickListener, ReLoadCallbackListener{

    private static final String TAG = "RecommendActivity";

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.refreshlayout)
    MaterialRefreshLayout refreshLayout;

    private ArrayList<AllBookModels.ReturnClazz.AllBook> mDataSet;
    private HomeRecyclerViewAdapter mAdapter;
    private ViewSelectorLayout viewSelectorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
        ButterKnife.bind(this);
        StatusBarCompat.compat(this, getResources().getColor(R.color.brownness));
        init();
        Data();
    }

    private void setContentView() {
        View v = getLayoutInflater().inflate(R.layout.activity_recommend, null);
        viewSelectorLayout = new ViewSelectorLayout(this, v);
        viewSelectorLayout.setReLoadCallbackListener(this);
        setContentView(viewSelectorLayout);
    }

    private void Data() {
        viewSelectorLayout.show_LoadingView();
        getRecommendBookData();

        refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                getRecommendBookData();
            }
        });
    }

    private void getRecommendBookData() {
        AppDao.getInstance().getRecommendBook("0", "0", new CallbackListener<AllBookModels>() {
            @Override
            public void onError(Exception e) {
                super.onError(e);
                Log.e(TAG, "" + e);
                toastMsg(Constant.NET_ERROR);
                viewSelectorLayout.show_FailView();
                refreshLayout.finishRefresh();
            }

            @Override
            public void onSuccess(AllBookModels result) {
                super.onSuccess(result);
                mDataSet = (ArrayList)result.Return.List;
                if(mAdapter == null){
                    mAdapter = new HomeRecyclerViewAdapter(RecommendActivity.this, mDataSet);
                    recyclerView.setAdapter(mAdapter);
                }else {
                    mAdapter.updateData(mDataSet);
                }

                viewSelectorLayout.show_ContentView();
                refreshLayout.finishRefresh();

            }

            @Override
            public void onStringResult(String result) {
                super.onStringResult(result);
                Log.i(TAG, result);
            }
        });
    }

    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("最新推荐");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void toastMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        Http.cancel(HttpUrl.URL_RECOMMEND);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onReLoadCallback() {
        if(!FastOnClickUtils.isFastClick800())
        {
            viewSelectorLayout.show_LoadingView();
            getRecommendBookData();
        }
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
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
