package com.zihao.banner.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;

/**
 * ClassName：LoopViewPager
 * Description：TODO<一个支持无限轮播的ViewPager>
 * Author：zihao
 * Date：2017/8/28 10:49
 * Version：v1.0
 */
public class LoopViewPager extends ViewPager {
    private static final String TAG = LoopViewPager.class.getSimpleName();

    private LoopScroller loopScroller;

    public LoopViewPager(Context context) {
        this(context, null);
    }

    public LoopViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);// 关闭硬件加速
        setViewPagerScrollSpeed(getContext());
        // this.setPageTransformer(true, new DepthPageTransformer());
    }

    @Override
    public void setCurrentItem(int item) {
        setCurrentItem(item, false);
    }

    /**
     * 设置切换到当前选择的页面
     *
     * @param item         选定页面的下标
     * @param smoothScroll 是否平滑滚动
     */
    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        int current = getCurrentItem();

        // 如果页面相隔大于1,就设置页面切换时完成滑动的时间为0
        if (Math.abs(current - item) > 1) {
            loopScroller.setNoDuration(true);// 滑动前设置动画时间为0
            super.setCurrentItem(item, smoothScroll);
            loopScroller.setNoDuration(false);// 滑动结束后设置动画时间恢复
        } else {
            loopScroller.setNoDuration(false);
            super.setCurrentItem(item, smoothScroll);
        }
    }

    /**
     * 设置ViewPager的滑动速度
     *
     * @param context 上下文对象
     */
    private void setViewPagerScrollSpeed(Context context) {
        loopScroller = new LoopScroller(context);

        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            field.set(this, loopScroller);// 利用反射设置mScroller域为自己定义的loopScroller
        } catch (Exception e) {
            Log.e(TAG, "setViewPagerScrollSpeed:" + e.toString());
        }
    }
}
