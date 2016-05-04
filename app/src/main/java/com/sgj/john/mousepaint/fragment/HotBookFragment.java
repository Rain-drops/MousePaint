package com.sgj.john.mousepaint.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sgj.john.MaterialRefreshLayout;
import com.sgj.john.listener.CallbackListener;
import com.sgj.john.mousepaint.DetialActivity;
import com.sgj.john.mousepaint.R;
import com.sgj.john.mousepaint.adapter.CategoryDetialAdapter;
import com.sgj.john.mousepaint.customview.NormalListView;
import com.sgj.john.mousepaint.dao.AppDao;
import com.sgj.john.mousepaint.events.RefreshEvent;
import com.sgj.john.mousepaint.events.RefreshFinishEvent;
import com.sgj.john.mousepaint.events.RegisterEvent;
import com.sgj.john.mousepaint.events.ViewPagerHeightEvent;
import com.sgj.john.mousepaint.model.CategoryModel;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by John on 2016/1/26.
 */
public class HotBookFragment extends BaseFragment implements AdapterView.OnItemClickListener{

    private static final String TAG = "HotBookFragment";

    private boolean isInit = false;
    private boolean isHaveLoadData = false;
    private int PageIndex = 0;
    private CategoryDetialAdapter mAdapter;
    private List<CategoryModel.ReturnEntity.ListEntity> mList;
    @Bind(R.id.lv_hot)
    NormalListView lv_hot;
    @Bind(R.id.pb)
    ProgressBar pb;
    private  MaterialRefreshLayout mMaterialRefreshLayout;
    private boolean isLoadMore = false;

    public static HotBookFragment newInstance() {
        HotBookFragment fragment = new HotBookFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_hot_book, null);
        ButterKnife.bind(this, v);
        isInit = true;
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(TAG, "onActivityCreated");
        lazyLoad();
    }

    @Override
    protected void lazyLoad() {
        if (!isVisible||isHaveLoadData || !isInit) {
            return;
        }

        getCategoryData();

        lv_hot.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                EventBus.getDefault().post(new ViewPagerHeightEvent("hot", lv_hot.getMeasuredHeight()));
            }
        });

        lv_hot.setOnItemClickListener(this);
    }

    private void getCategoryData() {
        AppDao.getInstance().getCategoryData("0", "30", String.valueOf(PageIndex), new CallbackListener<CategoryModel>() {
            @Override
            public void onStringResult(String result) {
                super.onStringResult(result);
                Log.i(TAG, result + "");
            }

            @Override
            public void onSuccess(CategoryModel result) {
                super.onSuccess(result);

                if (result != null) {
                    List<CategoryModel.ReturnEntity.ListEntity> list = result.Return.List;
                    if (!isLoadMore) {
                        mList = list;
                        mAdapter = new CategoryDetialAdapter(getActivity(), mList);
                        lv_hot.setAdapter(mAdapter);

                        lv_hot.setFocusable(false);
                        isHaveLoadData = true;

                    } else {
                        if (list.size() <= 0) {
                            Toast.makeText(getActivity(), "没有更多数据了", Toast.LENGTH_SHORT).show();
                        }
                        mList.addAll(list);
                        if (mAdapter != null && list != null) {
                            mAdapter.updateData(mList);
                        }
                    }

                }
                reSet();
            }


            @Override
            public void onError(Exception e) {
                super.onError(e);
                Log.e(TAG, e + "");
                reSet();
            }


        });
    }

    public void reSet()
    {
        EventBus.getDefault().post(new RefreshFinishEvent());
        isLoadMore = false;
        pb.setVisibility(View.GONE);
    }


    public void onEvent(RefreshEvent event)
    {

        if(event.category.equals("refresh_hot"))
        {
            PageIndex += 1;
            isLoadMore = true;
            getCategoryData();
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), DetialActivity.class);
        intent.putExtra("bookId",String.valueOf(mAdapter.getItem(position).Id));
        startActivity(intent);
    }
}