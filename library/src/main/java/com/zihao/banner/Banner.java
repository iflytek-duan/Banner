package com.zihao.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zihao.banner.adapter.BannerAdapter;
import com.zihao.banner.adapter.LoopPagerAdapter;
import com.zihao.banner.util.ScreenUtils;
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

    /**
     * 布局参数
     */
    private static final int LT_WC = LinearLayout.LayoutParams.WRAP_CONTENT;
    private static final int LT_MP = LinearLayout.LayoutParams.MATCH_PARENT;
    private static final int RT_MP = RelativeLayout.LayoutParams.MATCH_PARENT;
    private static final int RT_WC = RelativeLayout.LayoutParams.WRAP_CONTENT;
    /**
     * 指示器容器属性
     */
    private LinearLayout indicatorContainerLt;// 指示器容器
    private int indicatorContainerLRPadding;// 指示器容器左/右填充
    private int indicatorContainerTBPadding;// 指示器容器上/下填充
    /**
     * 点的layout的属性
     */
    private int pointLRMargin;// point点左/右margin
    private int pointTBMargin;// point点上/下margin

    private LoopViewPager loopViewPager;
    private LoopPagerAdapter loopPagerAdapter;
    private BannerAdapter bannerAdapter;

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
        // 默认指示器容器的上下左右padding为6dp
        indicatorContainerLRPadding = ScreenUtils.dp2px(context, 6);
        indicatorContainerTBPadding = ScreenUtils.dp2px(context, 6);

        // 默认点的上下左右Margin为4dp
        pointLRMargin = ScreenUtils.dp2px(context, 4);
        pointTBMargin = ScreenUtils.dp2px(context, 4);
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
        initIndicatorContainerView(context);
    }

    private void initViewPager(Context context) {
        loopViewPager = new LoopViewPager(context);
        loopViewPager.addOnPageChangeListener(onPageChangeListener);
        LayoutParams params = new LayoutParams(RT_MP, RT_MP);
        addView(loopViewPager, params);
    }

    /**
     * 初始化指示器容器内容
     *
     * @param context context
     */
    private void initIndicatorContainerView(Context context) {
        indicatorContainerLt = new LinearLayout(context);// 初始化指示器容器布局
        indicatorContainerLt.setOrientation(LinearLayout.HORIZONTAL);// 设置为水平布局方式
        indicatorContainerLt.setGravity(Gravity.CENTER);// 设置内部内容居中展示
        indicatorContainerLt.setPadding(indicatorContainerLRPadding, 0, indicatorContainerLRPadding,
                indicatorContainerTBPadding);// 设置填充
        LayoutParams pointsLayoutParams = new LayoutParams(RT_MP, RT_WC);
        pointsLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        pointsLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        this.addView(indicatorContainerLt, pointsLayoutParams);
    }

    /**
     * 设置Banner的适配器
     *
     * @param bannerAdapter bannerAdapter
     */
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
        LinearLayout.LayoutParams pointParams = new LinearLayout.LayoutParams(LT_WC, LT_WC);
        pointParams.setMargins(pointLRMargin, pointTBMargin, pointLRMargin, pointTBMargin);
        ImageView pointImg;
        for (int i = 0; i < dataList.size(); i++) {
            pointImg = new ImageView(context);
            pointImg.setBackgroundResource(R.drawable.point_default_selector);
            pointImg.setEnabled(false);
            indicatorContainerLt.addView(pointImg, pointParams);
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

    /**
     * 切换至指定的指示点(即将对应位置的点设置为Enable=true)
     * 在切换点时，我们只需要将它的上一个/下一个点设置为Enable=false即可达到效果(在生成点时，默认都为false)。
     *
     * @param newPosition 新选中位置
     */
    private void switchToPoint(int newPosition) {
        int lastPos;
        int nextPos;

        if (newPosition == 0) {
            lastPos = loopPagerAdapter.getRealPageEndPos() - 1;
            nextPos = newPosition + 1;
        } else if (newPosition == loopPagerAdapter.getRealPageEndPos() - 1) {
            lastPos = newPosition - 1;
            nextPos = 0;
        } else {
            lastPos = newPosition - 1;
            nextPos = newPosition + 1;
        }

        indicatorContainerLt.getChildAt(lastPos).setEnabled(false);
        indicatorContainerLt.getChildAt(nextPos).setEnabled(false);
        indicatorContainerLt.getChildAt(newPosition).setEnabled(true);
    }
}
