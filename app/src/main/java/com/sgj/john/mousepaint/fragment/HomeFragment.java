package com.sgj.john.mousepaint.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sgj.john.MaterialRefreshLayout;
import com.sgj.john.MaterialRefreshListener;
import com.sgj.john.listener.CallbackListener;
import com.sgj.john.mousepaint.R;
import com.sgj.john.mousepaint.adapter.HomeRecyclerViewAdapter;
import com.sgj.john.mousepaint.dao.AppDao;
import com.sgj.john.mousepaint.events.UpdateHomeDataEvent;
import com.sgj.john.mousepaint.model.AllBookModels;
import com.sgj.john.view.ReLoadCallbackListener;
import com.sgj.john.view.ViewSelectorLayout;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by John on 2016/1/6.
 */
public class HomeFragment extends Fragment implements ReLoadCallbackListener{

    private static final String TAG= "HomeFragment";

    @Bind(R.id.recycler_view) RecyclerView recyclerView;
    @Bind(R.id.refreshlayout)
    MaterialRefreshLayout refreshLayout;
    private HomeRecyclerViewAdapter mAdapter;
    private ArrayList<AllBookModels.ReturnClazz.AllBook> mDataSet;
    private ViewSelectorLayout viewSelectorLayout;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * 此回调获取传入的参数包;
     * 应该尽量避免耗时操作（避免阻塞UI线程）
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(mAdapter != null){
            mAdapter.updateData(mDataSet);
        }
        if(refreshLayout != null){
            refreshLayout.finishRefresh();
        }

    }

    /**
     * 在该回调方法中应该返回该Fragment的一个视图层次结构
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, null);
        ButterKnife.bind(this, v);
        viewSelectorLayout = new ViewSelectorLayout(getActivity(), v);
        viewSelectorLayout.setReLoadCallbackListener(this);//自定义监听
        return viewSelectorLayout;

    }

    /**
     * 在onCreateView执行完后立即执行。
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //实现瀑布流 （设置列数）
        /*
        LayoutManager：用来确定每一个item如何进行排列摆放，何时展示和隐藏。回收或重用一个View的时候，LayoutManager会向适配器请求新的数据来替换旧的数据，
        这种机制避免了创建过多的View和频繁的调用findViewById方法（与ListView原理类似）。
        目前SDK中提供了三种自带的LayoutManager:
            LinearLayoutManager
            GidLayoutManager
            StaggeredGridLayoutManager
         */
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    /**
     * 会在Activity完成其onCreate()回调之后调用;
     * 这是在用户看到用户界面之前你可对用户界面执行的最后调整的地方
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewSelectorLayout.show_LoadingView();
        getBookData();
        refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                getBookData();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    private void getBookData() {
        AppDao.getInstance().getAllBook(new CallbackListener<AllBookModels>(){

            @Override
            public void onError(Exception e) {
                super.onError(e);
                Log.e(TAG, e.toString());
                toastMsg(e + "");
                viewSelectorLayout.show_FailView();
                refreshLayout.finishRefresh();

            }

            @Override
            public void onSuccess(AllBookModels result) {
                super.onSuccess(result);

                mDataSet = (ArrayList) result.Return.List;
                if(mAdapter == null){
                    mAdapter = new HomeRecyclerViewAdapter(getActivity(), mDataSet);
                    recyclerView.setAdapter(mAdapter);
                }else{
                    mAdapter.updateData(mDataSet);
                }
                viewSelectorLayout.show_ContentView();
                refreshLayout.finishRefresh();


            }

            @Override
            public void onStringResult(String result) {
                super.onStringResult(result);
                Log.i("sgj", "result ----> " + result.toString());

            }

        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onReLoadCallback() {
        viewSelectorLayout.show_LoadingView();
        getBookData();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private void toastMsg(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    public void onEvent(UpdateHomeDataEvent event){

    }

    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener(){
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            Log.e("ListView", "onScrollStateChanged");
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };

}
