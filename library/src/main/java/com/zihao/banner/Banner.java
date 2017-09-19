package com.zihao.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zihao.banner.adapter.BannerAdapter;
import com.zihao.banner.adapter.LoopPagerAdapter;
import com.zihao.banner.util.ScreenUtils;
import com.zihao.banner.view.LoopViewPager;

import java.util.ArrayList;
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
     * 最小移动距离，用于判断是否为滑动操作
     */
    private static final int MIN_MOVING_DISTANCE = 10;
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
    private int indicatorContainerPaddingL;
    private int indicatorContainerPaddingR;
    private int indicatorContainerPaddingT;
    private int indicatorContainerPaddingB;
    /**
     * 点的layout的属性
     */
    private int pointDrawableResId;// point资源图,默认为point_default_selector
    private int pointMarginL;
    private int pointMarginR;
    private int pointMarginT;
    private int pointMarginB;

    private LoopViewPager loopViewPager;
    private LoopPagerAdapter loopPagerAdapter;
    private BannerAdapter bannerAdapter;

    private OnPageClickListener onPageClickListener;// 页卡点击事件监听器

    /**
     * 标识是否开启无限轮播，默认为开启状态
     */
    private boolean isAutoLoop = true;
    /**
     * 标识是否启用指示器，默认为启用
     */
    private boolean isEnableIndicator = true;
    /**
     * 标识是否开启画廊效果，默认为否
     */
    private boolean isEnableGallery = false;
    /**
     * 标识页卡是否正在改变状态(防止与onTouch事件引发冲突--滚动过程中点击后造成页卡停留(return true导致))
     */
    private boolean onPageChange = false;

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
        // 默认指示器容器的上下左右padding为4dp
        indicatorContainerPaddingL = ScreenUtils.dp2px(context, 4);
        indicatorContainerPaddingR = ScreenUtils.dp2px(context, 4);
        indicatorContainerPaddingT = ScreenUtils.dp2px(context, 4);
        indicatorContainerPaddingB = ScreenUtils.dp2px(context, 4);

        // 默认点的背景为R.drawable.point_default_selector
        pointDrawableResId = R.drawable.point_default_selector;
        // 默认点的左右Margin为2dp，上下Margin为0dp
        pointMarginL = ScreenUtils.dp2px(context, 2);
        pointMarginR = ScreenUtils.dp2px(context, 2);
        pointMarginT = ScreenUtils.dp2px(context, 0);
        pointMarginB = ScreenUtils.dp2px(context, 0);
    }

    /**
     * 初始化自定义属性
     *
     * @param context context
     * @param attrs   attrs
     */
    private void initCustomAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Banner);

        isAutoLoop = typedArray.getBoolean(R.styleable.Banner_autoLoop, isAutoLoop);
        isEnableIndicator = typedArray.getBoolean(R.styleable.Banner_enable_indicator,
                isEnableIndicator);
        isEnableGallery = typedArray.getBoolean(R.styleable.Banner_enable_gallery, isEnableGallery);

        indicatorContainerPaddingL = typedArray.getInteger(R.styleable.Banner_indicator_paddingLeft,
                indicatorContainerPaddingL);
        indicatorContainerPaddingR = typedArray.getInteger(R.styleable.Banner_indicator_paddingRight,
                indicatorContainerPaddingR);
        indicatorContainerPaddingT = typedArray.getInteger(R.styleable.Banner_indicator_paddingTop,
                indicatorContainerPaddingT);
        indicatorContainerPaddingB = typedArray.getInteger(R.styleable.Banner_indicator_paddingBottom,
                indicatorContainerPaddingB);

        pointDrawableResId = typedArray.getResourceId(R.styleable.Banner_pointDrawable, pointDrawableResId);
        pointMarginL = typedArray.getInteger(R.styleable.Banner_point_marginLeft, pointMarginL);
        pointMarginR = typedArray.getInteger(R.styleable.Banner_point_marginRight, pointMarginR);
        pointMarginT = typedArray.getInteger(R.styleable.Banner_point_marginTop, pointMarginT);
        pointMarginB = typedArray.getInteger(R.styleable.Banner_point_marginBottom, pointMarginB);

        typedArray.recycle();
    }

    /**
     * 初始化控件
     *
     * @param context context
     */
    private void initView(Context context) {
        initViewPager(context);
        if (isEnableIndicator) {// 只有启用指示器时才添加进来
            initIndicatorContainerView(context);
        }
    }

    /**
     * 初始化ViewPager
     *
     * @param context context
     */
    private void initViewPager(Context context) {
        loopViewPager = new LoopViewPager(context);
        loopViewPager.addOnPageChangeListener(onPageChangeListener);

        loopViewPager.setOnTouchListener(new OnTouchListener() {
            float downX, downY;// 按下时X、Y坐标
            float dx, dy;//松开时的X、Y坐标

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:// 按下
                        downX = motionEvent.getX();
                        downY = motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_UP:// 松开
                        dx = motionEvent.getX() - downX;// X轴的距离差
                        dy = motionEvent.getY() - downY;// Y轴的距离差

                        if (Math.abs(dx) > Math.abs(dy) && Math.abs(dx) < MIN_MOVING_DISTANCE // 在X轴移动，且移动距离小于最小限制，视为点击事件
                                || Math.abs(dx) < Math.abs(dy) && Math.abs(dy) < MIN_MOVING_DISTANCE) {// 在Y轴移动，且移动距离小于最小限制，视为点击事件
                            if (!onPageChange && onPageClickListener != null) {// 点击操作
                                onPageClickListener.onPageClick(loopViewPager.getCurrentItem());
                                return true;// 拦截touch事件
                            }
                        }
                        break;
                }
                return false;// 默认不拦截touch事件
            }
        });

        LayoutParams params = new LayoutParams(RT_MP, RT_MP);
        this.addView(loopViewPager, params);
    }

    /**
     * 初始化画廊效果
     *
     * @param enableGallery true：启用画廊效果；false：不启用。
     */
    private void initGallery(boolean enableGallery) {
        if (enableGallery) {// 启用画廊模式的情况下，进行对应配置
            // 设置Viewpager左右边距，便于展示其它内容页
            LayoutParams params = (LayoutParams) loopViewPager.getLayoutParams();
            params.leftMargin = ScreenUtils.dp2px(getContext(), 60);
            params.rightMargin = ScreenUtils.dp2px(getContext(), 60);

            loopViewPager.setClipChildren(false);
            loopViewPager.setPageMargin(20);
            loopViewPager.setLayoutParams(params);
            this.setClipChildren(false);// 设置在子View进行绘制时不裁切它们的显示范围
        } else {// 不启用画廊模式的情况下，关闭硬件加速效果同时重置ViewPager的LayoutParams
            LayoutParams params = (LayoutParams) loopViewPager.getLayoutParams();
            params.leftMargin = 0;
            params.rightMargin = 0;

            loopViewPager.setPageMargin(0);
            loopViewPager.setLayoutParams(params);
            loopViewPager.setLayerType(View.LAYER_TYPE_SOFTWARE, null);// 关闭硬件加速
        }
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
        indicatorContainerLt.setPadding(indicatorContainerPaddingL, indicatorContainerPaddingT,
                indicatorContainerPaddingR, indicatorContainerPaddingB);// 设置填充
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

            if (list != null && list.size() > 0) {
                setVPAdapter(list);
            } else {
                Log.e(TAG, "setVPDataSource: dataList is empty.");
            }
        } else {// bannerAdapter is null
            Log.e(TAG, "setVPDataSource: bannerAdapter is null.");
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
        initGallery(isEnableGallery);// 初始化画廊效果
        loopViewPager.setCurrentItem(loopPagerAdapter.getRealPageStartPos());// 先在这里滚动至非填充区域

        int pageCount = loopPagerAdapter.getCount();
        if (pageCount > 2) {// 当page数量超过2个时，加入指示点
            initIndicatorPoints(getContext(), dataList);
            loopViewPager.setOffscreenPageLimit(pageCount > 3 ? 3 : pageCount);// 默认缓存3页
            switchToPoint(loopPagerAdapter.getRealPageStartPos() - 1);
        }
    }

    /**
     * 初始化指示点
     *
     * @param context  context
     * @param dataList 数据集
     */
    private void initIndicatorPoints(Context context, List dataList) {
        if (indicatorContainerLt != null) {
            LinearLayout.LayoutParams pointParams = new LinearLayout.LayoutParams(LT_WC, LT_WC);
            pointParams.setMargins(pointMarginL, pointMarginT, pointMarginR, pointMarginB);
            ImageView pointImg;
            for (int i = 0; i < dataList.size(); i++) {
                pointImg = new ImageView(context);
                pointImg.setBackgroundResource(pointDrawableResId);
                pointImg.setEnabled(false);
                indicatorContainerLt.addView(pointImg, pointParams);
            }
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
        }

        @Override
        public void onPageSelected(int position) {
            Log.d(TAG, "onPageSelected:" + position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            // 这里主要是解决在onPageScrolled出现的闪屏问题
            // (positionOffset为0的时候，并不一定是切换完成，所以动画还在执行，强制再次切换，就会闪屏)
            switch (state) {
                case ViewPager.SCROLL_STATE_IDLE:// 空闲状态，没有任何滚动正在进行（表明完成滚动）
                    int currentItem = loopViewPager.getCurrentItem();
                    Log.d(TAG, "onPageScrollStateChanged, currentItem:" + currentItem);

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

                    onPageChange = false;// 在这边标记为默认正常状态
                    break;
                case ViewPager.SCROLL_STATE_DRAGGING:// 正在拖动page状态
                    onPageChange = true;// 在这里标记为页卡改变状态
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

    // 用于记录已设置为Enable状态的指示点,解决快速滑动切换item导致onPageScrollStateChanged方法内
    // 未完全触发每个item的空闲状态
    private ArrayList<View> enablePointList = new ArrayList<>();

    /**
     * 切换至指定的指示点(即将对应位置的点设置为Enable=true)
     * 在切换点时，我们只需要将它的上一个/下一个点设置为Enable=false即可达到效果(在生成点时，默认都为false)。
     *
     * @param newPosition 新选中位置
     */
    private void switchToPoint(int newPosition) {
        // 重置已选中内容的状态
        if (enablePointList != null && enablePointList.size() > 0) {
            for (View childView : enablePointList) {
                childView.setEnabled(false);
                enablePointList.remove(childView);
            }
        }
        enablePointList.add(indicatorContainerLt.getChildAt(newPosition));
        indicatorContainerLt.getChildAt(newPosition).setEnabled(true);
    }

    /**
     * 页卡点击事件
     */
    public interface OnPageClickListener {
        void onPageClick(int position);

    }

    public void setOnPageClickListener(OnPageClickListener onPageClickListener) {
        this.onPageClickListener = onPageClickListener;
    }
}
