package com.zihao.banner.animation;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * ClassName：ScaleInTransformer
 * Description：TODO<缩放切换动画>
 * Author：zihao
 * Date：2017/9/20 15:16
 * Version：v1.0
 */
public class ScaleInTransformer implements ViewPager.PageTransformer {

    private static final float DEFAULT_CENTER = 0.5f;// 默认中心点
    private static final float DEFAULT_MIN_SCALE = 0.85f;// 默认最小缩放比例

    @Override
    public void transformPage(View page, float position) {
        int pageWidth = page.getWidth();
        int pageHeight = page.getHeight();

        page.setPivotY(pageHeight / 2);
        page.setPivotX(pageWidth / 2);
        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            page.setScaleX(DEFAULT_MIN_SCALE);
            page.setScaleY(DEFAULT_MIN_SCALE);
            page.setPivotX(pageWidth);
        } else if (position <= 1) { // [-1,1]
            // Modify the default slide transition to shrink the page as well
            if (position < 0) {// 1-2:1[0,-1] ;2-1:1[-1,0]
                float scaleFactor = (1 + position) * (1 - DEFAULT_MIN_SCALE) + DEFAULT_MIN_SCALE;
                page.setScaleX(scaleFactor);
                page.setScaleY(scaleFactor);
                page.setPivotX(pageWidth * (DEFAULT_CENTER + (DEFAULT_CENTER * -position)));

            } else {// 1-2:2[1,0] ;2-1:2[0,1]
                float scaleFactor = (1 - position) * (1 - DEFAULT_MIN_SCALE) + DEFAULT_MIN_SCALE;
                page.setScaleX(scaleFactor);
                page.setScaleY(scaleFactor);
                page.setPivotX(pageWidth * ((1 - position) * DEFAULT_CENTER));
            }
        } else { // (1,+Infinity]
            page.setPivotX(0);
            page.setScaleX(DEFAULT_MIN_SCALE);
            page.setScaleY(DEFAULT_MIN_SCALE);
        }
    }
}
