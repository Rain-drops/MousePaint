package com.sgj.john.mousepaint.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sgj.john.mousepaint.DetialActivity;
import com.sgj.john.mousepaint.R;
import com.sgj.john.mousepaint.customview.HtmlTextView;
import com.sgj.john.mousepaint.model.Card;
import com.sgj.john.mousepaint.utils.AppUtils;

/**
 * 精选漫画
 */
public class CardFragment extends AbsBaseFragment implements View.OnClickListener {

    protected Card mCard;
    protected TextView mAuthorText;
    protected ImageView mBottomEdgeImageView;
    protected TextView mBravoNumText;
    protected RelativeLayout mCardLayout;
    protected ImageView mCoverImageView;

    protected HtmlTextView mDigestText;
    protected TextView digestText;

    protected TextView mSubTitleText;
    protected TextView mTitleText;


    /**
     * 当你小心翼翼的创建了一个带有重要参数的Fragment的之后，一旦由于什么原因（横竖屏切换）导致你的Fragment重新创建。
     * ——-很遗憾的告诉你，你之前传递的参数都不见了，因为recreate你的Fragment的时候，调用的是默认构造函数。
     * 而使用系统推荐的 Fragment.setArguments（Bundle）来传递参数。就可以有效的避免这一个问题，当你的Fragment销毁的时候，
     * 其中的Bundle会保存下来，当要重新创建的时候会检查Bundle是否为null，如果不为null，就会使用bundle作为参数来重新创建fragment.
     * @param card
     * @return
     */
    public static CardFragment getInstance(Card card){

        CardFragment localCardFragment = new CardFragment();
        Bundle localBundle = new Bundle();
        localBundle.putSerializable("card", card);
        localCardFragment.setArguments(localBundle);
        return localCardFragment;
    }

    @Override
    protected void initActions(View paramView) {

    }

    @Override
    protected void initData() {
        this.mCard = (Card) getArguments().getSerializable("card");

    }

    @Override
    protected View initView(LayoutInflater parpmLayoutInflater) {

        View view = parpmLayoutInflater.inflate(R.layout.fragment_card, null);
        mAuthorText = (TextView) view.findViewById(R.id.text_author);
        mBottomEdgeImageView = (ImageView) view.findViewById(R.id.image_bottom_edge);
        mBravoNumText = (TextView) view.findViewById(R.id.text_bravos);

        mCardLayout = (RelativeLayout) view.findViewById(R.id.box_card);
        mCardLayout.setOnClickListener(this);

        mCoverImageView = (ImageView) view.findViewById(R.id.image_cover);
//        mDigestText = (HtmlTextView) view.findViewById(R.id.text_digest);
        digestText = (TextView) view.findViewById(R.id.text_digest);
        mSubTitleText = (TextView) view.findViewById(R.id.text_subtitle);
        mTitleText = (TextView) view.findViewById(R.id.text_title);

        mTitleText.setText(this.mCard.getTitle());
        mSubTitleText.setText(this.mCard.getSubTitle());
        this.mBravoNumText.setText("No." + this.mCard.getUpNum());
//        this.mDigestText.setText(this.mCard.getDigest());
        this.digestText.setText(this.mCard.getDigest());
        this.mAuthorText.setText(Html.fromHtml("<B>" + this.mCard.getAuthorName() + "</B>"));
        initAndDisplayCoverImage();

        return view;
    }

    /**
     * 初始化和显示封面图片
     */
    private void initAndDisplayCoverImage() {
        int coverWidth = AppUtils.getScreenDisplayMetrics(getActivity()).widthPixels - 2 * getResources().getDimensionPixelSize(R.dimen.card_margin);
        int coverHeight = (int) (180.0F * (coverWidth / 320.0F));
        //用于child view（子视图） 向 parent view（父视图）传达自己的意愿
        ViewGroup.LayoutParams localLayoutParams = this.mCoverImageView.getLayoutParams();
        localLayoutParams.height = Float.valueOf(coverHeight).intValue();
        Glide.with(getActivity()).load(mCard.getCoverImgerUrl()).centerCrop().into(mCoverImageView);

    }

    @Override
    public void onDestroy() {
        this.mCoverImageView.setImageBitmap(null);
        super.onDestroy();

    }

    @Override
    public void onDestroyView() {
        this.mCoverImageView.setImageBitmap(null);
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.box_card:
                Intent intent = new Intent();
                intent.putExtra("bookId", mCard.getId());
                intent.setClass(getActivity(), DetialActivity.class);
                startActivity(intent);
                break;

        }
    }
}
