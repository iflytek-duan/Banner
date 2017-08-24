package com.zihao.banner.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * ClassName：XPagerAdapter
 * Description：TODO<ViewPager页卡适配器>
 * Author：zihao
 * Date：2017/8/17 16:47
 * Version：v1.0
 */
public class XPagerAdapter extends PagerAdapter {
    private static final String TAG = XPagerAdapter.class.getSimpleName();

    private BannerAdapter bannerAdapter;
    private List dataList;
    private int realPageStartPos = 0;// 页卡真实开始位置，默认为0
    private int realPageEndPos = 0;// 页卡真实结束位置，默认为0
    private boolean isAutoLoop = false;// 标识是否开启无限轮播，默认为否

    public XPagerAdapter(BannerAdapter bannerAdapter, List dataList, boolean isAutoLoop) {
        this.bannerAdapter = bannerAdapter;
        this.dataList = dataList;
        this.isAutoLoop = isAutoLoop;
    }

    /**
     * 获取页卡视图总数
     *
     * @return 页卡视图总数量
     */
    @Override
    public int getCount() {
        int count = dataList == null ? 0 : dataList.size();

        if (isAutoLoop) {// 只有在开启无限自动轮播时，才会设置左右侧填充页卡
            count = count > 1 ? count + 2 : count;
            if (count > 1) {// 只有创建1个以上的真实页卡时才会在边界(左右侧)填充页卡用于支持无限循环效果
                realPageStartPos = 1;// 真实的页卡起始位置为1,0为最左侧填充页卡
                realPageEndPos = count - 2;// 真实的页卡结束位置为count - 2,count - 1为最右侧填充页卡
            }
        } else {
            if (count > 0) {
                realPageStartPos = 0;
                realPageEndPos = count - 1;
            }
        }
        return count;// 只有创建1个以上的真实页卡时才会在边界添加View用于支持无限循环效果
    }

    /**
     * TODO<判断要添加的视图是否与pager的一个view相关联>
     * <p>
     * <p>
     * Determines whether a page View is associated with a specific key object<br>
     * 确定一个页面视图是否与一个特定的键对象相关联<br>
     * as returned by {@link #instantiateItem(ViewGroup, int)}. This method is<br>
     * 由{@link #instantiateItem(ViewGroup, int)}返回。这个方法是<br>
     * required for a PagerAdapter to function properly.<br>
     * 需要一个PagerAdapter来正常工作。<br>
     *
     * @param view   Page View to check for association with <code>object</code><br>
     *               用于检查与对象关联的页面视图<br>
     * @param object Object to check for association with <code>view</code><br>
     *               用于检查与视图关联的对象(要添加的视图)<br>
     * @return true if <code>view</code> is associated with the key object <code>object</code><br>
     * 如果要添加的视图与page的View相关联，则返回true.
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void startUpdate(ViewGroup container) {
        super.startUpdate(container);
    }

    /**
     * 实例化一个页卡
     *
     * @param container 视图容器
     * @param position  子视图下标
     * @return itemVIew
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView iv;// 新建itemView
        if (container.getTag() == null) {// 没有保存tag
            iv = createItemView(container.getContext());// 新建itemView
        } else {// 获取缓存的tag
            iv = (ImageView) container.getTag();
        }

        container.addView(iv);// 添加itemView到ViewGroup

        // 设置itemView资源
        if (bannerAdapter != null) {
            if (isAutoLoop) {// 支持无限轮播
                if (position == 0) {// 最左侧视图，与realLastPage共用一张图片
                    bannerAdapter.setItemViewSource(iv, dataList.size() - 1);
                } else if (position == getCount() - 1) {// 最右侧的视图，与realFirstPage共用一张图片
                    bannerAdapter.setItemViewSource(iv, 0);
                } else {// 因为最左侧存在填充视图，所以所有页面为了保证显示正常，需要使用position - 1达到效果即可
                    bannerAdapter.setItemViewSource(iv, position - 1);
                }
            } else {// 禁止无限轮播
                bannerAdapter.setItemViewSource(iv, position);
            }
        }
        return iv;
    }

    /**
     * 从当前视图容器中删除指定的页卡
     *
     * @param container 视图容器
     * @param position  子视图下标
     * @param object    要销毁的视图对象
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        removeItemView(container, object);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);
    }

//    /**
//     * 返回给定的页面所占ViewPager 测量宽度的比例，范围（0,1]
//     *
//     * @param position 页面下标位置
//     * @return 宽度比
//     */
//    @Override
//    public float getPageWidth(int position) {
//        return 0.8f;
//    }

    /**
     * 创建一个子视图
     *
     * @param context 上下文对象
     */
    private ImageView createItemView(Context context) {
        ImageView iv = new ImageView(context);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return iv;
    }

    /**
     * 移除一个子视图
     *
     * @param viewGroup 视图容器
     * @param object    要销毁的视图对象
     */
    private void removeItemView(ViewGroup viewGroup, Object object) {
        ImageView view = (ImageView) object;
        viewGroup.removeView(view);
    }

    public int getRealPageStartPos() {
        return realPageStartPos;
    }

    public int getRealPageEndPos() {
        return realPageEndPos;
    }
}
