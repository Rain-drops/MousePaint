package com.sgj.john.mousepaint.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sgj.john.mousepaint.WebActivity;
import com.sgj.john.mousepaint.model.AdvModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页商家广告适配器
 * Created by John on 2016/1/27.
 *
 * PagerAdapter比AdapterView的使用更加普通.ViewPager使用回调函数来表示一个更新的步骤，而不是使用一个视图回收机制。
 * 在需要的时候pageradapter也可以实现视图的回收或者使用一种更为巧妙的方法来管理视图，比如采用可以管理自身视图的fragment。
 *
 * viewpager不直接处理每一个视图而是将各个视图与一个键联系起来。这个键用来跟踪且唯一代表一个页面，
 * 不仅如此，该键还独立于这个页面所在adapter的位置。当pageradapter将要改变的时候他会调用startUpdate函数，
 * 接下来会调用一次或多次的instantiateItem或者destroyItem。最后在更新的后期会调用finishUpdate。当finishUpdate返回时,
 * instantiateItem返回的对象应该添加到父ViewGroup destroyItem返回的对象应该被ViewGroup删除。methodisViewFromObject(View, Object)代表了当前的页面是否与给定的键相关联。
 *
 * 对于非常简单的pageradapter或许你可以选择用page本身作为键，在创建并且添加到viewgroup后,instantiateItem方法里返回该page本身即可,
 * destroyItem将会将该page从viewgroup里面移除。isViewFromObject方法里面直接可以返回view == object。
 *
 * pageradapter支持数据集合的改变，数据集合的改变必须要在主线程里面执行，然后还要调用notifyDataSetChanged方法。
 * 和baseadapter非常相似。数据集合的改变包括页面的添加删除和修改位置。viewpager要维持当前页面是活动的，所以你必须提供getItemPosition方法。
 */
public class ViewPagerAdvAdapter extends PagerAdapter {

    private Context mContext;
    private List<AdvModel.ListEntity> mDatas;
    private ImageView mImageView;
    private List<ImageView> mViews;


    public ViewPagerAdvAdapter(Context context, List<AdvModel.ListEntity> mDatas) {
        this.mDatas = mDatas;
        this.mContext = context;
        mViews = new ArrayList<ImageView>();
        int length = mDatas == null ? 0 : mDatas.size();
        for(int i = 0; i < length; i++){
            ImageView imageView = new ImageView(mContext);
            mViews.add(imageView);
        }
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    /**
     * 当前的页面是否与给定的键相关联
     * 可以返回view == object
     * @param view
     * @param object
     * @return
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * 做了两件事，第一：将当前视图添加到container中，第二：返回当前View
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(View container, final int position) {
        mImageView = mViews.get(position);
        //是否保持宽高比
        mImageView.setAdjustViewBounds(true);
        //不按比例缩放图片，目标是把图片塞满整个View。
        mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WebActivity.class);
                intent.putExtra(WebActivity.EXTRA_URL, mDatas.get(position).Link);
                mContext.startActivity(intent);
            }
        });
        Glide.with(mContext).load(mDatas.get(position).Img).centerCrop().into(mImageView);

        ((ViewPager)container).addView(mImageView);

        return mImageView;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        mImageView = mViews.get(position);
        ((ViewPager)container).removeView(mImageView);
//        super.destroyItem(container, position, object);
    }
}
