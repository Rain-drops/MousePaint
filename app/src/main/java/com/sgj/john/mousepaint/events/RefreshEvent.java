package com.sgj.john.mousepaint.events;

import com.sgj.john.MaterialRefreshLayout;

/**
 * Created by John on 2016/1/26.
 */
public class RefreshEvent {
    public String category;
    public MaterialRefreshLayout mMaterialRefreshLayout;

    public RefreshEvent(String category) {
        this.category = category;
    }

    public RefreshEvent(MaterialRefreshLayout mMaterialRefreshLayout, String category) {
        this.category = category;
        this.mMaterialRefreshLayout = mMaterialRefreshLayout;
    }
}
