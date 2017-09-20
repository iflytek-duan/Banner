package com.zihao.banner.animation;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * ClassName：RotateDownPageTransformer
 * Description：TODO<向下旋转切换动画>
 * Author：zihao
 * Date：2017/9/20 14:17
 * Version：v1.0
 */
public class RotateDownPageTransformer implements ViewPager.PageTransformer {

    private static final float DEFAULT_MAX_ROTATE = 15.0f;

    @Override
    public void transformPage(View page, float position) {
        int pageWidth = page.getWidth();
        int pageHeight = page.getHeight();

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            page.setRotation(DEFAULT_MAX_ROTATE * -1);
            page.setPivotX(pageWidth);
        } else if (position <= 1) { // [-1,1]
            if (position < 0) {//[0，-1]
                page.setPivotX(pageWidth * (0.5f + 0.5f * (-position)));
                page.setRotation(DEFAULT_MAX_ROTATE * position);
            } else {//[1,0]
                page.setPivotX(pageWidth * 0.5f * (1 - position));
                page.setRotation(DEFAULT_MAX_ROTATE * position);
            }
        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            page.setRotation(DEFAULT_MAX_ROTATE);
            page.setPivotX(0);
        }
        page.setPivotY(pageHeight);
    }
}
