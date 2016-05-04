package com.sgj.john.mousepaint.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sgj.john.MaterialRefreshLayout;
import com.sgj.john.MaterialRefreshListener;
import com.sgj.john.listener.CallbackListener;
import com.sgj.john.mousepaint.R;
import com.sgj.john.mousepaint.adapter.BookPagerAdapter;
import com.sgj.john.mousepaint.adapter.ViewPagerAdvAdapter;
import com.sgj.john.mousepaint.dao.AppDao;
import com.sgj.john.mousepaint.events.RefreshEvent;
import com.sgj.john.mousepaint.events.ViewPagerHeightEvent;
import com.sgj.john.mousepaint.model.AdvModel;
import com.sgj.john.mousepaint.utils.PlayViewHandler;
import com.sgj.john.view.ViewSelectorLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import me.relex.circleindicator.CircleIndicator;

/**
 * 漫画分类
 * Created by John on 2016/1/6.
 */
public class CategoryFragment extends Fragment {

    private static final String TAG = "CategoryFragment";

    @Bind(R.id.viewpager_catorage)
    ViewPager mViewPager;
    @Bind(R.id.viewpager_book)
    ViewPager mViewPager_book;
    @Bind(R.id.indicator_default)
    CircleIndicator mCircleIndicator;
    @Bind(R.id.tabs)
    TabLayout mTabLayout;
    @Bind(R.id.refreshlayout)
    MaterialRefreshLayout mMaterialRefreshLayout;
    ViewSelectorLayout mViewSelectorLayout;

    private PlayViewHandler mViewHandler = new PlayViewHandler();



//    private

    public static CategoryFragment newInstance() {
        CategoryFragment fragment = new CategoryFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewPager(mViewPager_book);
        mViewSelectorLayout.show_LoadingView();
        getAvdData();

        mViewPager_book.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
        {
            @Override
            public void onPageSelected(int position) {
                Log.e(TAG, "onPageSelected(position)---->"+ position);
                super.onPageSelected(position);
                switch (position)
                {
                    case 0:
                        updateHeight(oldHeith1);
                        break;
                    case 1:
                        updateHeight(oldHeith2);
                        break;
                    case 2:
                        updateHeight(oldHeith3);
                        break;
                }
            }
        });
    }



    private void getAvdData() {
        AppDao.getInstance().getSlideData(new CallbackListener<AdvModel>() {
            @Override
            public void onSuccess(AdvModel result) {
                super.onSuccess(result);
                if (result != null) {
                    final ViewPagerAdvAdapter mAdapter = new ViewPagerAdvAdapter(getActivity(), result.list);
                    mViewPager.setAdapter(mAdapter);
                    mCircleIndicator.setViewPager(mViewPager);
                    mViewHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mViewHandler.slidingPlayView(mViewPager, mAdapter.getCount());
                        }
                    }, 6_000);
                    mViewSelectorLayout.show_ContentView();
                }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_category, null);
        mViewSelectorLayout = new ViewSelectorLayout(getActivity(), v);
        ButterKnife.bind(this, v);
        return mViewSelectorLayout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMaterialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                materialRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mMaterialRefreshLayout.finishRefresh();
                    }
                }, 1_000);
            }

            @Override
            public void onfinish() {
                super.onfinish();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                handRefreshMore(materialRefreshLayout);
            }

        });

    }

    private void handRefreshMore(MaterialRefreshLayout materialRefreshLayout) {
        Log.e(TAG, "handRefreshMore(mViewPager_book.getCurrentItem())---->" + mViewPager_book.getCurrentItem());
        switch (mViewPager_book.getCurrentItem())
        {
            case 0:
                EventBus.getDefault().post(new RefreshEvent(materialRefreshLayout,"refresh_hot"));
                break;
            case 1:
                EventBus.getDefault().post(new RefreshEvent(materialRefreshLayout,"refresh_same"));
                break;
            case 2:
                EventBus.getDefault().post(new RefreshEvent(materialRefreshLayout,"refresh_mouse"));
                break;
            case 3:

                break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    private void setupViewPager(ViewPager viewPager) {
        BookPagerAdapter adapter = new BookPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        mTabLayout.setupWithViewPager(viewPager);
        mTabLayout.setSelectedTabIndicatorColor(Color.parseColor("#ffffff"));
        mTabLayout.setTabTextColors(Color.parseColor("#dfdfdf"), Color.parseColor("#ffffff"));
    }

    private int oldHeith1,oldHeith2,oldHeith3;

    public void onEvent(Object event)
    {
        if(event instanceof ViewPagerHeightEvent)
        {
            ViewPagerHeightEvent hightEvent = (ViewPagerHeightEvent) event;
            int curH = hightEvent.heith;
            if(hightEvent.heith<=0){return;}

            if(hightEvent.vp.equals("hot")&&mViewPager_book.getCurrentItem()==0&&oldHeith1!=curH)
            {
                updateHeight(curH);
                oldHeith1 = curH;
            }else if(hightEvent.vp.equals("same")&&mViewPager_book.getCurrentItem()==1&&oldHeith2!=curH)
            {
                updateHeight(curH);
                oldHeith2 = curH;
            }else if(hightEvent.vp.equals("mouse")&&mViewPager_book.getCurrentItem()==2&&oldHeith3!=curH)
            {
                updateHeight(curH);
                oldHeith3 = curH;
            }

        }else
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(mMaterialRefreshLayout!= null)
                    {
                        mMaterialRefreshLayout.finishRefresh();
                        mMaterialRefreshLayout.finishRefreshLoadMore();
                    }
                }
            },1000);
        }


    }
    public void updateHeight(int heith)
    {
        ViewGroup.LayoutParams layoutParams =  mViewPager_book.getLayoutParams();
        layoutParams.height = heith;
        mViewPager_book.requestLayout();
    }

}
