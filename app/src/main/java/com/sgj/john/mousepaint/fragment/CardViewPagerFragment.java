package com.sgj.john.mousepaint.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.sgj.john.listener.CallbackListener;
import com.sgj.john.mousepaint.R;
import com.sgj.john.mousepaint.adapter.CardPagerAdapter;
import com.sgj.john.mousepaint.contral.control.IRhythmItemListener;
import com.sgj.john.mousepaint.contral.control.RhythmAdapter;
import com.sgj.john.mousepaint.contral.control.RhythmLayout;
import com.sgj.john.mousepaint.contral.control.ViewPagerScroller;
import com.sgj.john.mousepaint.dao.AppDao;
import com.sgj.john.mousepaint.model.AllBookModels;
import com.sgj.john.mousepaint.model.Card;
import com.sgj.john.mousepaint.utils.AnimatorUtils;
import com.sgj.john.mousepaint.utils.HexUtils;
import com.sgj.john.view.ViewSelectorLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 精选漫画Fragment
 */
public class CardViewPagerFragment extends AbsBaseFragment {

    private static final String TAG = "CardViewPagerFragment";

    private static CardViewPagerFragment mFragment;
    private ViewPager mViewPager;
    private CardPagerAdapter mCardPagerAdapter;
    private List<Card> mCardList;
    private ViewSelectorLayout mViewSelectorLayout;

    private View mMainView;
    private int mPreColor;

    private boolean mHasNext = true;

    private boolean mIsRequesting;//请求

    private boolean isAdapterUpdated;//更新适配器

    private int mCurrentViewPagerPage;//当前视图

    private Button mSideMenuOrBackBtn;

    private RhythmLayout mRhythmLayout;
    private RhythmAdapter mRhythmAdapter;



    public static CardViewPagerFragment getInstance(){
        if(mFragment == null){
            mFragment = new CardViewPagerFragment();
        }
        return mFragment;
    }

    @Override
    protected void initActions(View paramView) {
//        mRhythmLayout.setRhythmListener(rhythmItemListener);
        mViewPager.setOnPageChangeListener(onPageChangeListener);
    }

    @Override
    protected void initData() {
        mCardList = new ArrayList<Card>();
    }

    @Override
    protected View initView(LayoutInflater parpmLayoutInflater) {
        //精选漫画 view
        View view = parpmLayoutInflater.inflate(R.layout.fragment_niceapp, null);
        mViewSelectorLayout = new ViewSelectorLayout(getActivity(), view);
        mMainView = view.findViewById(R.id.main_view);
        mRhythmLayout = (RhythmLayout) view.findViewById(R.id.box_rhythm);
        //精选漫画List
        mViewPager = (ViewPager) view.findViewById(R.id.pager);

        //设置视图滚动速度
        setViewPagerScrollSpeed(mViewPager, 400);

//        mRhythmLayout.setScrollRhythmStartDelayTime(400);
//        int height = (int) mRhythmLayout.getRhythmItemWidth() + (int) TypedValue.applyDimension(1, 10.0F, getResources().getDisplayMetrics());
//        mRhythmLayout.getLayoutParams().height = height;

//        ((RelativeLayout.LayoutParams) mViewPager.getLayoutParams()).bottomMargin = height;

        return mViewSelectorLayout;
    }

    private void setViewPagerScrollSpeed(ViewPager viewPager, int speed) {
        //利用反射控制ViewPager的滑动速度
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            //如果取得的Field是private的，必须调用setAccessible(true)
            field.setAccessible(true);
            ViewPagerScroller viewPagerScroller = new ViewPagerScroller(viewPager.getContext(), new OvershootInterpolator(0.6F));
            field.set(viewPager, viewPagerScroller);
            viewPagerScroller.setDuration(speed);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private IRhythmItemListener rhythmItemListener = new IRhythmItemListener() {
        @Override
        public void onRhythmItemChanged(int paramInt) {

        }

        @Override
        public void onSelected(final int paramInt) {
            CardViewPagerFragment.this.mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mViewPager.setCurrentItem(paramInt);//直接跳转到某一个页面
                }
            }, 100L);
        }

        @Override
        public void onStartSwipe() {

        }
    };

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener(){
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            //当页面在滑动的时候会调用此方法，在滑动被停止之前，此方法回一直得到调用
            //{ arg0 :当前页面，及你点击滑动的页面 ; arg1:当前页面偏移的百分比 ; arg2:当前页面偏移的像素位置}

        }

        @Override
        public void onPageSelected(int position) {
            onAppPagerChange(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            //状态改变的时候调用{有三种状态（0，1，2）。state ==1的时辰默示正在滑动，state==2的时辰默示滑动完毕了，state==0的时辰默示什么都没做。}
        }
    };

    private void onAppPagerChange(int position) {

//        mRhythmLayout.showRhythmAtPosition(position);
        Card post = this.mCardList.get(position);
        Log.e(TAG, "post.getBackgroundColor()---->" + post.getBackgroundColor() );
        int currColor = HexUtils.getHexColor(post.getBackgroundColor());
        Log.e(TAG,"currColor---->" + currColor);
        AnimatorUtils.showBackgroundColorAnimation(this.mMainView, mPreColor, currColor, 400);
        mPreColor = currColor;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewSelectorLayout.show_LoadingView();
        fetchData();

    }

    private void fetchData() {
        AppDao.getInstance().subscribeByUser("0", new CallbackListener<AllBookModels>() {
            @Override
            public void onError(Exception e) {
                super.onError(e);
                Log.e(TAG, "ErrorResult---->" + e);
            }

            @Override
            public void onSuccess(AllBookModels result) {
                super.onSuccess(result);
                mViewSelectorLayout.show_ContentView();
                handData(result.Return.List);
            }

            @Override
            public void onStringResult(String result) {
                super.onStringResult(result);
                Log.i(TAG, "result---->" + result);
            }
        });
    }

    private void handData(ArrayList<AllBookModels.ReturnClazz.AllBook> list) {
        ArrayList<Card> cardList = new ArrayList<>();
        for(int i = 0; i < list.size(); i++){
            Card card = new Card();
            card.setId(list.get(i).Id);
            card.setTitle(list.get(i).Title);
            if(list.get(i).LastChapter != null)
                card.setSubTitle(list.get(i).LastChapter.Title);
            card.setDigest(list.get(i).Explain);
            card.setAuthorName(list.get(i).Author);
            if(list.get(i).LastChapter != null)
                card.setUpNum(Integer.valueOf(list.get(i).LastChapter.ChapterNo));
            card.setBackgroundColor("#795548");
            card.setCoverImgerUrl(list.get(i).FrontCover);
            card.setIconUrl(list.get(i).FrontCover);
            cardList.add(card);
        }

        updateAppAdapter(cardList);

        onAppPagerChange(0);
    }

    private void updateAppAdapter(ArrayList<Card> cardList) {
        if((getActivity() == null) || (getActivity().isFinishing())){
            return;
        }
        if (cardList.isEmpty()){
            this.mMainView.setBackgroundColor(this.mPreColor);
            return;
        }
        int size = mCardList.size();
        if(mCardPagerAdapter == null){
            mCurrentViewPagerPage = 0;
            mCardPagerAdapter = new CardPagerAdapter(getActivity().getSupportFragmentManager(), cardList);
            mViewPager.setAdapter(mCardPagerAdapter);
        }else {
            mCardPagerAdapter.addCardList(cardList);
            mCardPagerAdapter.notifyDataSetChanged();
        }
        //添加“@+id/box_rhythm”
//        addCardIconsToDock(cardList);

        this.mCardList = mCardPagerAdapter.getCardList();
        if(mViewPager.getCurrentItem() == size - 1){
            mViewPager.setCurrentItem(1 + mViewPager.getCurrentItem(), true );
        }
    }

    private void addCardIconsToDock(ArrayList<Card> cardList) {
        if(mRhythmAdapter == null){
            resetRhythmLayout(cardList);
        }
    }

    private void resetRhythmLayout(ArrayList<Card> cardList) {
        if(getActivity() == null){
            return;
        }
        if(cardList == null){
            cardList = new ArrayList<>();
            mRhythmAdapter = new RhythmAdapter(getActivity(), mRhythmLayout, cardList);
            mRhythmAdapter.setAdapter(mRhythmAdapter);
        }
    }
}
