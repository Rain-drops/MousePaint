package com.sgj.john.mousepaint.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.sgj.john.mousepaint.fragment.HotBookFragment;
import com.sgj.john.mousepaint.fragment.MouseComicFragment;
import com.sgj.john.mousepaint.fragment.SamePeopleFragment;

/**
 * Created by John on 2016/1/26.
 * FragmentPagerAdapter 继承自 PagerAdapter。相比通用的 PagerAdapter，该类更专注于每一页均为 Fragment 的情况.
 * 该类内的每一个生成的 Fragment 都将保存在内存之中，因此适用于那些相对静态的页，数量也比较少的那种；
 * 如果需要处理有很多页，并且数据动态性较大、占用内存较多的情况，应该使用FragmentStatePagerAdapter
 */
public class BookPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = "BookPagerAdapter";

    public BookPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        // 0热血，1国产，2同人，3鼠绘
        Log.e(TAG, "BookPagerAdapter--getItem(position)" + position);
        switch (position){
            case 0:
                return HotBookFragment.newInstance();
            case 1:
                return new SamePeopleFragment().newInstance();
            case 2:
                return new MouseComicFragment().newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0:
                return "热血";
            case 1:
                return "国产";
            case 2:
                return "鼠绘";
        }
        return null;
    }
}
