package com.sgj.john.mousepaint.contral.control;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.sgj.john.mousepaint.model.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by John on 2016/1/18.
 * 精选漫画中与横滑List对应的下标List适配器
 */
public class RhythmAdapter extends BaseAdapter {

    private static final String TAG = "RhythmAdapter";

    private Context mContext;
    private RhythmAdapter mAdapter;

    private float itemWidth;
    private List<Card> mCardList;
    private RhythmLayout mRhythmLayout;
    private LayoutInflater mInflater;

    public RhythmAdapter(Context context, List<Card> cardList, RhythmLayout mRhythmLayout) {
        this.mContext = mContext;
        this.mRhythmLayout = mRhythmLayout;
        this.mCardList = new ArrayList();
        this.mCardList.addAll(cardList);
        if(context != null){
            this.mInflater = LayoutInflater.from(context);
        }
    }

    public RhythmAdapter(FragmentActivity activity, RhythmLayout mRhythmLayout, ArrayList<Card> cardList) {
        
    }

    public List<Card> getCardList(){
        return this.mCardList;
    }

    public void addCardList(List<Card> cardList){
        mCardList.addAll(cardList);
    }

    public void setCardList(List<Card> mCardList) {
        this.mCardList = mCardList;
    }

    public void setItemWidth(float itemWidth) {
        this.itemWidth = itemWidth;
    }

    @Override
    public int getCount() {
        return this.mCardList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mCardList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.mCardList.get(position).getUid();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        return null;
    }

    public void setAdapter(RhythmAdapter adapter) {
        this.mAdapter = adapter;
    }
}
