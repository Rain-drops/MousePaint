package com.sgj.john.mousepaint.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sgj.john.mousepaint.fragment.CardFragment;
import com.sgj.john.mousepaint.fragment.CardViewPagerFragment;
import com.sgj.john.mousepaint.model.Card;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.codetail.animation.arcanimator.A;

/**
 * 精选漫画
 * Created by John on 2016/1/18.
 *
 * 继承自 PagerAdapter。但是，和 FragmentPagerAdapter 不一样的是，正如其类名中的 'State' 所表明的含义一样，
 * 该 PagerAdapter 的实现将只保留当前页面，当页面离开视线后，就会被消除，释放其资源；而在页面需要显示时，
 * 生成新的页面(就像 ListView 的实现一样)。这么实现的好处就是当拥有大量的页面时，不必在内存中占用大量的内存。
 */
public class CardPagerAdapter extends FragmentStatePagerAdapter {

    private List<Card> mPostList;
    private List<Fragment> mFragments = new ArrayList();

    public CardPagerAdapter(FragmentManager paramFragmentManager, List<Card> mPostList) {
        super(paramFragmentManager);
        Iterator localIterator = mPostList.iterator();
        while(localIterator.hasNext()){
            Card localAppModel = (Card) localIterator.next();
            this.mFragments.add(CardFragment.getInstance(localAppModel));
        }
        this.mPostList = mPostList;
    }

    @Override
    public Fragment getItem(int position) {
        return this.mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mPostList.size();
    }

    public List<Card> getCardList() {
        return mPostList;
    }

    public void setCardList(List<Card> cardList) {
        ArrayList localArrayList = new ArrayList();
        Iterator localIterator = localArrayList.iterator();
        while (localIterator.hasNext()){
            localArrayList.add(localIterator.next());
        }
        this.mFragments = localArrayList;
        this.mPostList = cardList;
    }

    public List<Fragment> getFragments() {
        return mFragments;
    }

    public void setFragments(List<Fragment> mFragments) {
        this.mFragments = mFragments;
    }

    public void addCardList(List<Card> cardList){
        ArrayList localArrayList = new ArrayList();
        Iterator localIterator = localArrayList.iterator();
        while (localIterator.hasNext()){
            localArrayList.add(CardFragment.getInstance((Card) localIterator.next()));
        }
        if (this.mFragments == null){
            this.mFragments = new ArrayList();
        }
        this.mFragments.addAll(localArrayList);
        this.mPostList.addAll(cardList);

    }
}
