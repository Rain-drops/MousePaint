package com.sgj.john.mousepaint.fragment;

import android.support.v4.app.Fragment;

/**
 * Created by John on 2016/1/26.
 */
public abstract class BaseFragment extends Fragment {
    /** Fragment当前状态是否可见 */
    protected boolean isVisible;

    /**
     * 实现在fragment可见时才进行数据加载操作，即Fragment的懒加载。
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()){
            //可见的
            isVisible = true;
            onVisible();
        }else{
            //不可见的
            isVisible = true;
            onInvisible();
        }
    }
    /**
     * 不可见
     */
    private void onInvisible() {
        lazyLoad();
    }
    /**
     * 延迟加载
     * 子类必须重写此方法
     */
    protected abstract void lazyLoad();

    /**
     * 可见
     */
    private void onVisible() {

    }
}
