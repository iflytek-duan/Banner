package com.zihao.banner.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.zihao.banner.adapter.LoopPagerAdapter;

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

    private LoopPagerAdapter loopPagerAdapter;

    public LoopViewPager(Context context) {
        super(context);
        init();
    }

    public LoopViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);// 关闭硬件加速
        setViewPagerScrollSpeed(getContext());
        // this.setPageTransformer(true, new DepthPageTransformer());
        this.addOnPageChangeListener(onPageChangeListener);
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
            Log.e(TAG, "setViewPagerScrollSpeed error:" + e.toString());
        }
    }

    /**
     * ViewPager页面更改监听器
     */
    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {

        /**
         * 页卡滚动状态回调
         * @param position 页卡位置 position=0，开始边界；position=getCount()-1，结束边界。
         * @param positionOffset 是当前页面滑动比例，如果页面向右翻动，这个值不断变大，
         *                       最后在趋近1的情况后突变为0。如果页面向左翻动，这个值不断变小，最后变为0
         * @param positionOffsetPixels 当前页面偏移的像素位置.
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            if (isAutoLoop && positionOffset == 0) {// 允许无限轮播且滚动完成进入内部判断
//                if (position == 0) {// 如果position为0，则跳转至结束位置
//                    // switch to the last page.
//                    setCurrentItem(pagerAdapter.getRealPageEndPos(), false);
//                }
//
//                if (position == pagerAdapter.getCount() - 1) {// 如果position为pagerAdapter.getCount() - 1，则跳转至开始位置
//                    // switch to the first page.
//                    setCurrentItem(pagerAdapter.getRealPageStartPos(), false);
//                }
//            }
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            // 这里主要是解决在onPageScrolled出现的闪屏问题
            // (positionOffset为0的时候，并不一定是切换完成，所以动画还在执行，强制再次切换，就会闪屏)
            switch (state) {
                case ViewPager.SCROLL_STATE_IDLE:// 空闲状态，没有任何滚动正在进行（表明完成滚动）
                    if (loopPagerAdapter != null && loopPagerAdapter.isAutoLoop()) {
                        if (getCurrentItem() == 0) {
                            setCurrentItem(loopPagerAdapter.getRealPageEndPos(), false);
                        } else if (getCurrentItem() == getAdapter().getCount() - 1) {
                            setCurrentItem(loopPagerAdapter.getRealPageStartPos(), false);
                        }
                    }
                    break;
                case ViewPager.SCROLL_STATE_DRAGGING:// 正在拖动page状态
                    break;
                case ViewPager.SCROLL_STATE_SETTLING:// 手指已离开屏幕，自动完成剩余的动画效果
                    break;
            }
        }

    };

    public void setAdapter(LoopPagerAdapter adapter) {
        super.setAdapter(adapter);
        this.loopPagerAdapter = adapter;
    }
}
