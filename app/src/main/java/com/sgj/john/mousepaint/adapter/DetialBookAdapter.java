package com.sgj.john.mousepaint.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sgj.john.mousepaint.R;

import com.sgj.john.mousepaint.model.DetialComicBookModel;
import com.sgj.john.mousepaint.utils.ViewHolderUtils;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by John on 2016/1/21.
 */
public class DetialBookAdapter extends BaseAdapter {

    private static final String TAG = "DetialBookAdapter";

    private Activity context;
    private List<DetialComicBookModel.ReturnEntity.ListEntity> list;
    private ViewHolder mHolder;
    private int selectIndex = 0;

    public DetialBookAdapter(Activity context, List<DetialComicBookModel.ReturnEntity.ListEntity> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public DetialComicBookModel.ReturnEntity.ListEntity getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        mHolder = new ViewHolder();
        convertView = ViewHolderUtils.loadingConvertView(parent.getContext(), convertView, R.layout.item_detial_book, ViewHolder.class);
        mHolder = (ViewHolder) convertView.getTag();
        Log.e(TAG, "list---->" + list.get(position).RefreshTimeStr);
        mHolder.tv_comic_intro.setText(list.get(position).RefreshTimeStr);
        mHolder.tv_comic_title.setText(list.get(position).Title);
        String text = null;

        switch (list.get(position).ChapterType){
            case 0:
                text = list.get(position).Sort + "话";
                break;
            case 1:
                text = list.get(position).Sort + "卷";
                break;
            case 2:
                text = list.get(position).Sort + "SP";
                break;
        }
        mHolder.tv_comic_status.setText(text);//ChapterType：话、卷、SP，参数为0、1和2
        Glide.with(context).load(list.get(position).FrontCover).centerCrop().into(mHolder.iv_zone_item);


        return convertView;
    }

    public void updateData(List<DetialComicBookModel.ReturnEntity.ListEntity> list) {

        this.list = list;
        this.notifyDataSetChanged();
    }

    public static class ViewHolder{
        public ImageView iv_zone_item;
        public TextView tv_comic_title;
        public TextView tv_comic_intro;
        public TextView tv_num_last;
        public TextView tv_comic_status;


    }
}
