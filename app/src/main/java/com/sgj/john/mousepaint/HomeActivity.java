package com.sgj.john.mousepaint;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.sgj.john.mingle.entity.MenuEntity;
import com.sgj.john.mingle.sweetpick.BlurEffect;
import com.sgj.john.mingle.sweetpick.Delegate;
import com.sgj.john.mingle.sweetpick.RecyclerViewDelegate;
import com.sgj.john.mingle.sweetpick.SweetSheet;
import com.sgj.john.mousepaint.constants.Constant;
import com.sgj.john.mousepaint.constants.SaveInfo;
import com.sgj.john.mousepaint.fragment.CardViewPagerFragment;
import com.sgj.john.mousepaint.fragment.CategoryFragment;
import com.sgj.john.mousepaint.fragment.HomeFragment;
import com.sgj.john.mousepaint.fragment.SubscribeFragment;
import com.sgj.john.mousepaint.utils.SPUtils;
import com.sgj.john.mousepaint.utils.StatusBarCompat;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.codetail.animation.SupportAnimator;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    private Context mContext;
    public static final String TAG = HomeActivity.class.getSimpleName();
    public static final String TAG_HOME = "Home";
    public static final String TAG_CATEGORY = "Category";
    public static final String TAG_SUBSCRIBE = "Subscribe";

    private HomeFragment mHomeFragment;
    private CategoryFragment mCategoryFragment;
    private SubscribeFragment mSubscribeFragment;

    private SupportAnimator mAnimator;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private String hideTag;

    private SweetSheet mSweetSheet;

    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.fl_content_home)
    FrameLayout fl_home;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.card_search)
    CardView mCardView;
    @Bind(R.id.iv_bottom_search)
    ImageView iv_bottom_search;
    @Bind(R.id.edit_text_search)
    EditText edit_text_search;
    @Bind(R.id.view_hide)
    View view_hide;
    @Bind(R.id.nav_view)
    NavigationView mNavigationView;

    private Intent mIntent;

    private boolean isFabHide = false;

    private boolean isCategory = false;

    private boolean isHome = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mContext = this;
        ButterKnife.bind(this);

        StatusBarCompat.compat(this, getResources().getColor(R.color.brownness));

        init();

    }

    private void init() {
        view_hide.setOnClickListener(this);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //返回按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Navigation Icon 要設定在 setSupoortActionBar 后才有作用
        // 否則會出現 back button
        toolbar.setNavigationIcon(R.drawable.ic_menu);

        //是Support Library包中实现了侧滑菜单效果的控件
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //滑动菜单栏视图
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        if(mNavigationView != null){
            setupDrawerContent(mNavigationView);
            mNavigationView.setCheckedItem(R.id.nav_home);
        }

        //主页List
        mHomeFragment = HomeFragment.newInstance();
        switchFragment(TAG_HOME, mHomeFragment);

        setupRecyclerView();

        mIntent = new Intent();

        handFabPathAndSearch();


    }

    /**
     * 搜索框
     */
    private void handFabPathAndSearch() {

    }

    /**
     * 设置下方菜单Item[R,id.fab]
     */
    private void setupRecyclerView() {
        List<MenuEntity> list = new ArrayList<>();
        MenuEntity menuEntity1 = new MenuEntity();
        menuEntity1.iconId = R.drawable.recommend;
        menuEntity1.title = "最新推荐";
        MenuEntity menuEntity2 = new MenuEntity();
        menuEntity2.iconId = R.drawable.seven;
        menuEntity2.title = "本周更新";
        MenuEntity menuEntity3 = new MenuEntity();
        menuEntity3.iconId = R.drawable.mouse;
        menuEntity3.title = "所有漫画";
        list.add(menuEntity1);
        list.add(menuEntity2);
        list.add(menuEntity3);

        // SweetSheet 控件,根据 rl 确认位置
        mSweetSheet = new SweetSheet(fl_home);
        //设置数据源 (数据源支持设置 list 数组,也支持从菜单中获取)
        mSweetSheet.setMenuList(list);

        //根据设置不同的Delegate 来显示不同的风格
        mSweetSheet.setDelegate(new RecyclerViewDelegate(false));

        //根据设置不同的Effect 来显示背景效果BlueEffect：模糊效果，DimEffect，变暗效果
        mSweetSheet.setBackgroundEffect(new BlurEffect(8));

        //设置点击事件
        mSweetSheet.setOnMenuItemClickListener(new SweetSheet.OnMenuItemClickListener(){
            @Override
            public boolean onItemClick(int position, MenuEntity menuEntity) {
                showFab();
                //根据返回值, true 会关闭 SweetSheet ,false 则不会.
//                Toast.makeText(HomeActivity.this, menuEntity.title + "  " + position, Toast.LENGTH_SHORT).show();
                handClick(position);
                return true;
            }
        });
        mSweetSheet.setBgListener(new Delegate.BgListener(){
            @Override
            public void onClick() {
                closeMenu();
            }
        });
        fab.setOnClickListener(this);
    }

    private void handClick(int position) {
        switch (position){
            case Constant.RECOMMENT : //最新推荐
                mIntent.setClass(HomeActivity.this, RecommendActivity.class);
                break;
            case Constant.WEEK://本周更新
                mIntent.setClass(this, WeekActivity.class);
                break;
            case Constant.ALLCOMMIC://所有漫画
                mNavigationView.setCheckedItem(R.id.nav_category);
                setCategoryFragment();
                return;
        }
        startActivity(mIntent);

    }

    /**
     * 转换不同的Fragment
     * @param tag
     * @param mFragment
     */
    private void switchFragment(String tag, Fragment mFragment) {

        if(hideTag == tag){
            return;
        }

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();

        Fragment tagFragment = mFragmentManager.findFragmentByTag(tag);
        if(tagFragment == null){
            mFragmentTransaction.add(R.id.fl_content_home, mFragment, tag);
        }else {
            mFragmentTransaction.show(tagFragment);
        }

        tagFragment = mFragmentManager.findFragmentByTag(hideTag);

        if(tagFragment !=null){
            mFragmentTransaction.hide(tagFragment);
        }
        hideTag = tag;
        mFragmentTransaction.commit();
    }

    /**
     * 设置滑动菜单Item
     * @param mNavigationView
     */
    private void setupDrawerContent(NavigationView mNavigationView) {

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                //关闭搜索框、选择器
                if (view_hide.isShown()) {
                    view_hide.performClick();
                }
                closeMenu();
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:

                        setTitle("最新漫画");
                        handFab(false);
                        fab.setImageResource(R.drawable.ic_add_white_24dp);
                        isHome = true;
                        isCategory = false;

                        if (mHomeFragment == null) {
                            mHomeFragment = HomeFragment.newInstance();
                        }
                        switchFragment(TAG_HOME, mHomeFragment);
                        break;
                    case R.id.nav_subscribe:
//                                if (mSubscribeFragment == null) {
//                                    mSubscribeFragment = SubscribeFragment.newInstance();
//                                }
                        setTitle("精选漫画");
                        handFab(true);
                        //侧滑 List
                        switchFragment(TAG_SUBSCRIBE, new CardViewPagerFragment());
                        break;
                    case R.id.nav_category:
                        setCategoryFragment();
                        break;

                }
                return true;
            }
        });
    }

    private void setCategoryFragment() {
        setTitle("漫画分类");
        isHome = false;
        fab.setImageResource(R.drawable.ic_action_search);
        isCategory = true;
        handFab(false);
        if(mCategoryFragment == null){
            mCategoryFragment = CategoryFragment.newInstance();

        }
        switchFragment(TAG_CATEGORY, mCategoryFragment);
    }

    private void handFab(boolean isFabHide) {
        this.isFabHide = isFabHide;
        animFab();
    }

    private void animFab() {
        if(isFabHide)
        {
            hideFab();
        }else
        {
            showFab();
        }
    }

    private void showFab() {
        if (fab != null) {
//            ObjectAnimator animatorTX = ObjectAnimator.ofFloat(fab,"translationX",fab.getTranslationX(),0);
////          ObjectAnimator animatorSX = ObjectAnimator.ofFloat(fab, "scaleX", 0, 1);
////          ObjectAnimator animatorSY = ObjectAnimator.ofFloat(fab, "scaleY", 0, 1);
//            ObjectAnimator animatorA = ObjectAnimator.ofFloat(fab,"alpha",0,1);
//            AnimatorSet animSet = new AnimatorSet();
//            animSet.play(animatorA).with(animatorTX);
//            animSet.setDuration(500);
//            animSet.start();
            fab.animate().scaleX(1.0f);
            fab.animate().scaleY(1.0f);
            fab.animate().translationX(0);
        }
    }

    private void hideFab() {
        if (fab != null) {
//            ObjectAnimator animatorTX = ObjectAnimator.ofFloat(fab,"translationX",fab.getTranslationX(),200);
////          ObjectAnimator animatorSX = ObjectAnimator.ofFloat(fab, "scaleX", 1, 0);
////          ObjectAnimator animatorSY = ObjectAnimator.ofFloat(fab, "scaleY", 1,0);
//            ObjectAnimator animatorA = ObjectAnimator.ofFloat(fab,"alpha",1,0);
//            AnimatorSet animSet = new AnimatorSet();
//            animSet.play(animatorA).with(animatorTX);
//            animSet.setDuration(500);
//            animSet.start();
            fab.animate().scaleX(0.1f);
            fab.animate().scaleY(0.1f);
            fab.animate().translationX(200);
        }
    }

    private void closeMenu() {

        if (mSweetSheet.isShow()) {
            showFab();
            mSweetSheet.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.fab:
                if(isHome ){
                    //如果是主页，显示菜单
                    menuAndBtnAnim();
                }else {
                    //如果不是主页，显示搜索框
                    view_hide.setVisibility(View.VISIBLE);
//                    ArcAnimtor
                }

                break;
            case R.id.view_hide:

                break;

        }

    }

    private void menuAndBtnAnim() {
        mSweetSheet.toggle();
        if(mSweetSheet.isShow()){
            hideFab();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                closeMenu();
                //开启抽屉菜单
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK){
            //先隐藏搜索框
            if(view_hide.isShown()){
                view_hide.performClick();
            }else {
                showCloseDialog();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void showCloseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage("小伙伴，你确定要离开我吗？");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SPUtils.delObject(SaveInfo.KEY_LOGIN);
                System.exit(0);
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }
}
