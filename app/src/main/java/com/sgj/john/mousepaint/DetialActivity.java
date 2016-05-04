package com.sgj.john.mousepaint;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sgj.john.listener.CallbackListener;
import com.sgj.john.mousepaint.adapter.DetialBookAdapter;
import com.sgj.john.mousepaint.dao.AppDao;
import com.sgj.john.mousepaint.model.DetialComicBookModel;
import com.sgj.john.mousepaint.url.HttpUrl;
import com.sgj.john.mousepaint.utils.StatusBarCompat;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 漫画章节页
 */
public class DetialActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final String TAG = "DetialActivity";
    private Context mContext;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    //提供了一个可以折叠的Toolbar
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @Bind(R.id.backdrop)
    ImageView mImageView;
    @Bind(R.id.lv_detial)
    GridView mListView;
    @Bind(R.id.ns_view)
    NestedScrollView mNestedScrollView;
    @Bind(R.id.fab_sub)
    FloatingActionButton fab_sub;

    private boolean isHaveBottom = false;//底部
    private String id;
    private DetialBookAdapter mDetialBookAdapter;
    private boolean isLoadMore = false; //加载更多
    private List<DetialComicBookModel.ReturnEntity.ListEntity> list;
    private int PageIndex = 0;
    private boolean isSub = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_detial);
        ButterKnife.bind(this);
        mContext = this;
        StatusBarCompat.compat(this, getResources().getColor(R.color.brownness));
        id = getIntent().getStringExtra("bookId");

        //嵌套的滚动视图
        mNestedScrollView.setOnTouchListener(new TouchListenerImpl());

        setSupportActionBar(mToolbar);
        fab_sub.setOnClickListener(this);
        mListView.setOnItemClickListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getBookComicData();


    }

    private void getBookComicData() {
        AppDao.getInstance().getBookComicData(id, String.valueOf(PageIndex), new CallbackListener<DetialComicBookModel>() {
            @Override
            public void onError(Exception e) {
                super.onError(e);
                Log.e(TAG, "getBookComicData---->" + e);
            }

            @Override
            public void onSuccess(DetialComicBookModel result) {
                super.onSuccess(result);
                showView(result);
            }

            @Override
            public void onStringResult(String result) {
                super.onStringResult(result);
                Log.i(TAG, result + "");
            }
        });
    }

    private void showView(DetialComicBookModel result) {
        if (!isLoadMore) {//首次加载
            list = result.Return.List;
            mCollapsingToolbarLayout.setTitle(result.Return.ParentItem.Title);
            Glide.with(this).load(result.Return.ParentItem.FrontCover).into(mImageView);
            mDetialBookAdapter = new DetialBookAdapter(DetialActivity.this, list);
            mListView.setAdapter(mDetialBookAdapter);
        } else {
            list.addAll(result.Return.List);
            mDetialBookAdapter.updateData(list);
            isLoadMore = false;
            isHaveBottom = false;
        }

        //订阅
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_sub:
                //订阅

                break;

        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        handClick(HttpUrl.URL_IMG_CHAPTER + mDetialBookAdapter.getItem(position).Id, "123");
    }

    private void handClick(String url, String title) {

        Intent intent = new Intent(DetialActivity.this, WebActivity.class);
        intent.putExtra(WebActivity.EXTRA_URL, url);
        intent.putExtra(WebActivity.EXTRA_TITLE, title);
        startActivity(intent);
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

    private class TouchListenerImpl implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    int scrollyY = v.getScrollY();
                    int height = v.getHeight();
                    int scrollViewMeasureHeight = mNestedScrollView.getChildAt(0).getMeasuredHeight();
                    if(scrollyY == 0){
                        Log.i(TAG, "滑动到了顶端 view.getScrollY() = " + scrollyY);
                    }
                    if ((scrollyY + height) ==scrollViewMeasureHeight){
                        if(!isHaveBottom){
                            isLoadMore = true;
                            PageIndex +=1;
                            getBookComicData();
                        }
                        isHaveBottom = true;
                    }
                    break;
            }
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
