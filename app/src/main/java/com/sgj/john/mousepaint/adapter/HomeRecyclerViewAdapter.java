package com.sgj.john.mousepaint.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sgj.john.listener.CallbackListener;
import com.sgj.john.mousepaint.MosuePaintAppication;
import com.sgj.john.mousepaint.R;
import com.sgj.john.mousepaint.WebActivity;
import com.sgj.john.mousepaint.constants.Constant;
import com.sgj.john.mousepaint.customview.FavorLayout;
import com.sgj.john.mousepaint.dao.AppDao;
import com.sgj.john.mousepaint.model.AllBookModels;
import com.sgj.john.mousepaint.model.SubscribeModel;
import com.sgj.john.mousepaint.url.HttpUrl;
import com.sgj.john.mousepaint.utils.SPUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *
 * Created by John on 2016/1/8.
 */
public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "HomeRecyclerViewAdapter";

    private Context mContext;
    ArrayList<AllBookModels.ReturnClazz.AllBook> mList;

    public HomeRecyclerViewAdapter(Context mContext, ArrayList<AllBookModels.ReturnClazz.AllBook> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    public void updateData(ArrayList<AllBookModels.ReturnClazz.AllBook> list){
        this.mList = list;
        this.notifyDataSetChanged();
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.grid_item, null);
        return new ViewHolder(view);
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final AllBookModels.ReturnClazz.AllBook allBook = mList.get(position);
        if(allBook.LastChapter == null){
            return;
        }
        holder.tv_explain.setText(allBook.Explain);
        holder.tv_lastChapter.setText(allBook.LastChapter.Title);
        holder.tv_time.setText(allBook.LastChapter.RefreshTimeStr);
        holder.tv_title.setText(allBook.Title + "  " + allBook.LastChapter.Sort + "话");

        //开源库（加载图片）
        Glide.with(mContext).load(allBook.LastChapter.FrontCover).centerCrop().into(holder.iv_cover);
//        Picasso.with(mContext).load(allBook.LastChapter.FrontCover).centerCrop().into(holder.iv_cover);

//        if (SPUtils.getObject(MosuePaintAppication.UserInfo.email + "num" + allBook.Id, String.class, "-1").equals(allBook.LastChapter.ChapterNo)) {
//            holder.iv_cover.setTagEnable(false);
//        } else {
//            holder.iv_cover.setTagEnable(true);
//        }
        holder.ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                handClick(HttpUrl.URL_IMG_CHAPTER + allBook.LastChapter.Id,allBook.LastChapter.Title );
                SPUtils.saveObject(MosuePaintAppication.UserInfo.email + "num" + allBook.Id, allBook.LastChapter.ChapterNo);
            }
        });

        if(SPUtils.getObject(MosuePaintAppication.UserInfo.email + "id" + allBook.Id, String.class, "-1" ).equals(allBook.Id)){
            holder.tv_item_subscribe.setText(allBook.Author);
        }else{
            holder.tv_item_subscribe.setText("+订阅");
        }

        holder.tv_item_subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDao.getInstance().subscribeBook(allBook.LastChapter.Id, true, new CallbackListener<SubscribeModel>(){
                    @Override
                    public void onError(Exception e) {
                        super.onError(e);
                        Toast.makeText(mContext, "订阅失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(SubscribeModel result) {
                        super.onSuccess(result);
                        Toast.makeText(mContext, "您喜欢的已经知道了", Toast.LENGTH_SHORT).show();
                        SPUtils.saveObject(MosuePaintAppication.UserInfo.email + "id" + allBook.Id, allBook.Id);
                        holder.tv_item_subscribe.setText(allBook.Author);
                        //显示小红心
                        holder.favor.addFavor();
                    }

                    @Override
                    public void onStringResult(String result) {
                        super.onStringResult(result);
                        Log.i(TAG, "sgj-->" + result);
                    }
                });
            }
        });

    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return mList.size();
    }

    private void handClick(String url, String title){
        Intent intent = new Intent();
//        Bundle bundle = new Bundle();
        intent.putExtra(WebActivity.EXTRA_URL, url);
        intent.putExtra(WebActivity.EXTRA_TITLE, title);
        intent.setClass(mContext, WebActivity.class);
        mContext.startActivity(intent);

    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_cover)
        ImageView iv_cover;
        @Bind(R.id.title_LastChapter)
        TextView tv_lastChapter;
        @Bind(R.id.tv_item_title)
        TextView tv_title;
        @Bind(R.id.tv_item_time)
        TextView tv_time;
        @Bind(R.id.tv_item_explain)
        TextView tv_explain;
        @Bind(R.id.ll_item)
        LinearLayout ll_item;
        @Bind(R.id.tv_item_subscribe)
        TextView tv_item_subscribe;
        @Bind(R.id.favor)
        FavorLayout favor;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
