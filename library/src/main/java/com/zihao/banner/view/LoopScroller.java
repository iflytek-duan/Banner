package com.zihao.banner.view;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * ClassName：LoopScroller
 * Description：TODO<用于支撑完成Banner无限轮播的Scroller视图循环滚动器>
 * Author：zihao
 * Date：2017/8/18 11:21
 * Version：v1.0
 */
class LoopScroller extends Scroller {

    /**
     * 标识是否没有滚动时间(true:没有；false:有)，默认为有
     */
    private boolean noDuration = false;

    LoopScroller(Context context) {
        super(context);
    }

    public LoopScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public LoopScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    /**
     * 开始滚动
     *
     * @param startX   开始X位置
     * @param startY   开始Y位置
     * @param dx       目标X位置
     * @param dy       目标Y位置
     * @param duration 完成滚动的时间
     */
    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        if (noDuration) {
            duration = 0;
        }
        super.startScroll(startX, startY, dx, dy, duration);
    }

    void setNoDuration(boolean noDuration) {
        this.noDuration = noDuration;
    }
}
