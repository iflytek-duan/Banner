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

    private boolean noDuration = false;// 标识是否包含切换动画时间

    LoopScroller(Context context) {
        super(context);
    }

    public LoopScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public LoopScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }

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
