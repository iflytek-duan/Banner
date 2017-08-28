package com.zihao.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.zihao.banner.adapter.BannerAdapter;
import com.zihao.banner.adapter.XPagerAdapter;
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
    private BannerAdapter bannerAdapter;

    /**
     * 标识是否开启无限轮播，默认为否
     */
    private boolean isAutoLoop = false;

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
        isAutoLoop = typedArray.getBoolean(R.styleable.Banner_autoLoop, false);
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
        XPagerAdapter pagerAdapter = new XPagerAdapter(bannerAdapter, dataList, isAutoLoop);
        loopViewPager.setAdapter(pagerAdapter);
        loopViewPager.setCurrentItem(pagerAdapter.getRealPageStartPos());// 先在这里滚动至非填充区域
        if (pagerAdapter.getCount() > 2) {
            loopViewPager.setOffscreenPageLimit(2);// 缓存2页
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

    public static int dp2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    public static int sp2px(Context context, float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics());
    }
}
