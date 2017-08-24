package com.zihao.banner.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * ClassName：BannerAdapter
 * Description：TODO<Banner适配器--提供对外的开放接口>
 * Author：zihao
 * Date：2017/8/17 16:08
 * Version：v1.0
 */
public abstract class BannerAdapter<T> {
    private List<T> dataList;// 用于存放展示内容的数据集

    public BannerAdapter(List<T> dataList) {
        this.dataList = dataList;
    }

    public List<T> getDataList() {
        return dataList;
    }

    /**
     * 设置item基本资源(要展示的内容)
     *
     * @param iv       显示图片资源的ImageView
     * @param position item下标
     */
    public void setItemViewSource(ImageView iv, int position) {
        if (dataList != null && dataList.size() > 0) {
            bindImage(iv, dataList.get(position));
        }
    }

    /**
     * 设置选中
     *
     * @param tv       显示提示的TextView
     * @param position item下标
     */
    public void setSelectTips(TextView tv, int position) {
        if (dataList != null && dataList.size() > 0) {
            bindTips(tv, dataList.get(position));
        }
    }

    /**
     * 绑定要展示的提示信息
     *
     * @param tv TextView
     * @param t  data
     */
    protected abstract void bindTips(TextView tv, T t);

    /**
     * 绑定要展示的图片资源
     *
     * @param iv ImageView
     * @param t  data
     */
    public abstract void bindImage(ImageView iv, T t);
}
