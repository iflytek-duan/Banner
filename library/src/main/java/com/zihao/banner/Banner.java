package com.zihao.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zihao.banner.adapter.BannerAdapter;
import com.zihao.banner.adapter.LoopPagerAdapter;
import com.zihao.banner.view.LoopViewPager;

import java.util.List;

/**
 * ClassName：Banner
 * Description：TODO<实现无限轮播的Banner>
 * Author：zihao
 * Date：2017/8/17 16:07
 * Version：v1.0
 */
public class Banner extends RelativeLayout {

    private static final String TAG = Banner.class.getSimpleName();

    private LoopViewPager loopViewPager;
    private LoopPagerAdapter loopPagerAdapter;
    private BannerAdapter bannerAdapter;

    private LinearLayout pointsLayout;

    /**
     * 标识是否开启无限轮播，默认为开启状态
     */
    private boolean isAutoLoop = true;

    public Banner(Context context) {
        this(context, null);
    }

    public Banner(Context context, AttributeSet attrs) {
        super(context, attrs);
        initDefaultAttrs(context);
        initCustomAttrs(context, attrs);
        initView(context);
    }

    /**
     * 初始化默认属性
     *
     * @param context context
     */
    private void initDefaultAttrs(Context context) {

    }

    /**
     * 初始化自定义属性
     *
     * @param context context
     * @param attrs   attrs
     */
    private void initCustomAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Banner);
        isAutoLoop = typedArray.getBoolean(R.styleable.Banner_autoLoop, true);
        typedArray.recycle();
    }

    /**
     * 初始化控件
     *
     * @param context context
     */
    private void initView(Context context) {
        initViewPager(context);
    }

    private void initViewPager(Context context) {
        loopViewPager = new LoopViewPager(context);
        loopViewPager.addOnPageChangeListener(onPageChangeListener);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(loopViewPager, params);
    }

    public void setBannerAdapter(BannerAdapter bannerAdapter) {
        this.bannerAdapter = bannerAdapter;
        setVPDataSource();
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
        loopPagerAdapter = new LoopPagerAdapter(bannerAdapter, dataList, isAutoLoop);
        loopViewPager.setAdapter(loopPagerAdapter);
        loopViewPager.setCurrentItem(loopPagerAdapter.getRealPageStartPos());// 先在这里滚动至非填充区域
        if (loopPagerAdapter.getCount() > 2) {
            initPoints(getContext(), dataList);
            loopViewPager.setOffscreenPageLimit(2);// 缓存2页
            switchToPoint(loopPagerAdapter.getRealPageStartPos() - 1);
        }
    }

    /**
     * 初始化dot点
     *
     * @param context  context
     * @param dataList 数据集
     */
    private void initPoints(Context context, List dataList) {
//        pointsLayout = new LinearLayout(context);
//        pointsLayout.setOrientation(LinearLayout.HORIZONTAL);// 设置水平方向
//        pointsLayout.setGravity(Gravity.NO_GRAVITY);
//        LayoutParams pointsLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);
//
//        LayoutParams pointParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        pointParams.setMargins(dp2px(context, 12), 0, dp2px(context, 12), 0);
//        for (int i = 0; i < dataList.size(); i++) {
//            ImageView pointImg = new ImageView(context);
//            pointImg.setBackgroundResource(R.drawable.point_default_selector);
//            pointsLayout.addView(pointImg, pointParams);
//        }
//
//        pointsLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//        pointsLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
//        this.addView(pointsLayout, pointsLayoutParams);
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
            Log.e(TAG, "page selected:" + position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            // 这里主要是解决在onPageScrolled出现的闪屏问题
            // (positionOffset为0的时候，并不一定是切换完成，所以动画还在执行，强制再次切换，就会闪屏)
            switch (state) {
                case ViewPager.SCROLL_STATE_IDLE:// 空闲状态，没有任何滚动正在进行（表明完成滚动）
                    int currentItem = loopViewPager.getCurrentItem();
                    Log.e(TAG, "onPageScrollStateChanged currentItem:" + currentItem);

                    if (loopPagerAdapter.isAutoLoop()) {// 开启无限轮播情况下item的切换
                        if (currentItem == 0) {// 尾部item页映射，跳转至尾部对接
                            switchToPoint(loopPagerAdapter.getRealPageEndPos() - 1);
                            loopViewPager.setCurrentItem(loopPagerAdapter.getRealPageEndPos(), false);
                        } else if (currentItem == loopPagerAdapter.getCount() - 1) {// 头部item页映射，跳转至头部对接
                            switchToPoint(0);
                            loopViewPager.setCurrentItem(loopPagerAdapter.getRealPageStartPos(), false);
                        } else {// 正常item的切换
                            switchToPoint(currentItem - 1);
                        }
                    } else {// 未开启无限轮播情况下item的切换
                        switchToPoint(currentItem - 1);
                    }
                    break;
                case ViewPager.SCROLL_STATE_DRAGGING:// 正在拖动page状态
                    break;
                case ViewPager.SCROLL_STATE_SETTLING:// 手指已离开屏幕，自动完成剩余的动画效果
                    break;
            }
        }

    };

    /**
     * 设置是否开启无限轮播
     *
     * @param autoLoop true:开启；false:关闭.默认false.
     */
    public void setAutoLoop(boolean autoLoop) {
        isAutoLoop = autoLoop;
    }

    public static int dp2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    public static int sp2px(Context context, float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics());
    }

    /**
     * 将点切换到指定的位置
     * 就是将指定位置的点设置成Enable
     *
     * @param newCurrentPoint 新位置
     */
    private void switchToPoint(int newCurrentPoint) {
//        for (int i = 0; i < pointsLayout.getChildCount(); i++) {
//            pointsLayout.getChildAt(i).setEnabled(false);
//        }
//        pointsLayout.getChildAt(newCurrentPoint).setEnabled(true);
    }
}
