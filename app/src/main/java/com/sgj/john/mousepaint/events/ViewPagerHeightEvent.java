package com.sgj.john.mousepaint.events;

/**
 * Created by John on 2016/1/27.
 */
public class ViewPagerHeightEvent {
    public int heith;
    public String vp;

    public ViewPagerHeightEvent(String vp,int heith) {
        this.vp = vp;
        this.heith = heith;
    }
}
