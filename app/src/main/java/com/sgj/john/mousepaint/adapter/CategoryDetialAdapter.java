package com.sgj.john.mousepaint.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sgj.john.mousepaint.R;
import com.sgj.john.mousepaint.model.CategoryModel;
import com.sgj.john.mousepaint.utils.ViewHolderUtils;

import java.util.List;

/**
 * Created by John on 2016/1/27.
 */
public class CategoryDetialAdapter extends BaseAdapter {

    private Activity context;
    List<CategoryModel.ReturnEntity.ListEntity> list;
    private int selectIndex = 0;
    private ViewHolder mHolder;

    public CategoryDetialAdapter(Activity context, List<CategoryModel.ReturnEntity.ListEntity> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CategoryModel.ReturnEntity.ListEntity getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mHolder = new ViewHolder();
        convertView = ViewHolderUtils.loadingConvertView(parent.getContext(), convertView, R.layout.item_category_detial, ViewHolder.class);
        mHolder = (ViewHolder) convertView.getTag();
        mHolder.tv_comic_title.setText(list.get(position).Title);
        mHolder.tv_comic_intro.setText(list.get(position).Explain);
        mHolder.tv_num_last.setText(list.get(position).Author);
        if(null != list.get(position).LastChapter&& !"null".equals(list.get(position).LastChapter.ChapterNo))
            mHolder.tv_comic_status.setText(list.get(position).LastChapter.ChapterNo+" 话");
        Glide.with(context).load(list.get(position).FrontCover).centerCrop().into(mHolder.iv_zone_item);

        return convertView;
    }

    public void updateData(List<CategoryModel.ReturnEntity.ListEntity> list){
        this.list = list;
        this.notifyDataSetChanged();

    }

    public static class ViewHolder{
        public ImageView iv_zone_item;
        public TextView tv_comic_title,tv_comic_intro,tv_num_last,tv_comic_status;
    }
}
