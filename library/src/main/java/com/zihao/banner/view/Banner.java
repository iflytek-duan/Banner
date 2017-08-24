package com.zihao.banner.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.zihao.banner.R;
import com.zihao.banner.adapter.BannerAdapter;
import com.zihao.banner.adapter.XPagerAdapter;

import java.lang.reflect.Field;
import java.util.List;

/**
 * ClassName：Banner
 * Description：TODO<实现无限轮播的Banner>
 * Author：zihao
 * Date：2017/8/17 16:07
 * Version：v1.0
 */
public class Banner extends ViewPager {

    private static final String TAG = Banner.class.getSimpleName();

    private BannerAdapter bannerAdapter;
    private XPagerAdapter pagerAdapter;

    /**
     * 标识是否开启无限轮播，默认为否
     */
    private boolean isAutoLoop = false;

    public Banner(Context context) {
        super(context);
        init();
    }

    public Banner(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Banner);
        isAutoLoop = typedArray.getBoolean(R.styleable.Banner_autoLoop, false);
        typedArray.recycle();
        init();
    }

    public void setBannerAdapter(BannerAdapter bannerAdapter) {
        this.bannerAdapter = bannerAdapter;
        setVPDataSource();
    }

    private void init() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);// 关闭硬件加速
        setViewPagerScrollSpeed(getContext());
//        this.setPageTransformer(true, new DepthPageTransformer());
    }

    /**
     * 设置ViewPager数据源
     */
    private void setVPDataSource() {
        if (bannerAdapter != null) {// bannerAdapter is not null
            List list = bannerAdapter.getDataList();
            if (list == null) {
                Log.e(TAG, "bannerAdapter.getDataList() is null.");
            } else {
                if (list.size() > 0) {
                    setVPAdapter(list);
                }
            }
        } else {// bannerAdapter is null
            Log.e(TAG, "bannerAdapter is null.");
        }
    }

    /**
     * 设置ViewPager适配器
     *
     * @param dataList 数据集
     */
    private void setVPAdapter(List dataList) {
        pagerAdapter = new XPagerAdapter(bannerAdapter, dataList, isAutoLoop);
        this.setAdapter(pagerAdapter);
        this.setCurrentItem(pagerAdapter.getRealPageStartPos());// 先在这里滚动至非填充区域
        this.addOnPageChangeListener(onPageChangeListener);
        if (pagerAdapter.getCount() > 2) {
            this.setOffscreenPageLimit(2);// 缓存2页
        }
    }

    /**
     * ViewPager页面更改监听器
     */
    private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {

        /**
         * 页卡滚动状态回调
         * @param position 页卡位置 position=0，开始边界；position=getCount()-1，结束边界。
         * @param positionOffset 位置间距，positionOffset=0时标识完成滚动。
         * @param positionOffsetPixels Value in pixels indicating the offset from position.
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (isAutoLoop && positionOffset == 0) {// 允许无限轮播且滚动完成进入内部判断
                if (position == 0) {// 如果position为0，则跳转至结束位置
                    // switch to the last page.
                    setCurrentItem(pagerAdapter.getRealPageEndPos(), false);
                }

                if (position == pagerAdapter.getCount() - 1) {// 如果position为pagerAdapter.getCount() - 1，则跳转至开始位置
                    // switch to the first page.
                    setCurrentItem(pagerAdapter.getRealPageStartPos(), false);
                }
            }
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    };

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

        //如果页面相隔大于1,就设置页面切换的动画的时间为0
        if (Math.abs(current - item) > 1) {
            loopScroller.setNoDuration(true);// 滑动前设置动画时间为0
            super.setCurrentItem(item, smoothScroll);
            loopScroller.setNoDuration(false);// 滑动结束后设置动画时间恢复
        } else {
            loopScroller.setNoDuration(false);
            super.setCurrentItem(item, smoothScroll);
        }
    }


    private LoopScroller loopScroller;

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
            field.set(this, loopScroller);// 利用反射设置mScroller域为自己定义的MScroller
        } catch (Exception e) {
            Log.e(TAG, "setViewPagerScrollSpeed error:" + e.toString());
        }
    }

    /**
     * 设置是否开启无限轮播
     *
     * @param autoLoop true:开启；false:关闭.默认false.
     */
    public void setAutoLoop(boolean autoLoop) {
        isAutoLoop = autoLoop;
    }
}