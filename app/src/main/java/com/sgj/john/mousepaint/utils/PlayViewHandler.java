package com.sgj.john.mousepaint.utils;

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;

import java.util.logging.LogRecord;

/**
 * 图片联播
 * Created by John on 2016/1/25.
 */
public class PlayViewHandler extends Handler {

    //播放标志
    public boolean play = true;
    //播放标志
    public int playingDirection;
    //播放的时间
    private static final int DEFAULT_TIME = 6_000;

    public PlayViewHandler() {
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case 0x123:
                final int count = msg.arg1;
                playingDirection = msg.arg2;
                final ViewPager viewPager = (ViewPager) msg.obj;
                //viewPager 从0开始
                int i = viewPager.getCurrentItem();
                if(playingDirection == 0){
                    if(i == count - 1){//正向--最后一条
                        //反向
                        playingDirection = -1;
                        i--;
                    }else{
                        //正向
                        i++;
                    }
                }else{
                    if(i == count - 1){
                        playingDirection = 1;
                        i=0;
                    }else {
                        i++;
                    }
                }

                viewPager.setCurrentItem(i, false);
                if(play){
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            slidingPlayView(viewPager, count, playingDirection);
                        }
                    }, DEFAULT_TIME);
                }
        }

        super.handleMessage(msg);

    }

    /**
     * 图片联播的方法
     * @param viewPager
     * @param count
     *              页数
     * @param dir
     *          方向  1正；0反
     */
    public void slidingPlayView(ViewPager viewPager, int count, int dir){
        Message message = new Message();
        message.what = 0x123;
        message.obj = viewPager;
        message.arg1 = count;
        message.arg2 = dir;
        sendMessage(message);
    }

    public void slidingPlayView(ViewPager viewPager, int count){
        Message message = new Message();
        message.what = 0x123;
        message.obj = viewPager;
        message.arg1 = count;
        message.arg2 = 1;
        sendMessage(message);
    }


}
