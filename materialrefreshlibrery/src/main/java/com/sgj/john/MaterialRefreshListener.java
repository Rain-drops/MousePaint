package com.sgj.john;

/**
 * 材料刷新侦听器
 */
public abstract class MaterialRefreshListener {
    public void onfinish(){};
    public abstract void onRefresh(MaterialRefreshLayout materialRefreshLayout);
    public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout){};
}
