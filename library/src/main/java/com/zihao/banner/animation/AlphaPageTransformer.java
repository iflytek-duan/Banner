package com.zihao.banner.animation;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * ClassName：AlphaPageTransformer
 * Description：TODO<一个渐变(淡入淡出)的Page切换动画效果>
 * Author：zihao
 * Date：2017/9/19 11:17
 * Version：v1.0
 */
public class AlphaPageTransformer implements ViewPager.PageTransformer {

    private static final float DEFAULT_MIN_ALPHA = 0.5f;

    @Override
    public void transformPage(View page, float position) {
        float minAlpha = DEFAULT_MIN_ALPHA;

        if (position < -1) {
            page.setAlpha(minAlpha);
        } else if (position <= 1) {// [-1,1]
            if (position < 0) {// [0，-1]
                float factor = minAlpha + (1 - minAlpha) * (1 + position);
                page.setAlpha(factor);
            } else {// [1，0]
                float factor = minAlpha + (1 - minAlpha) * (1 - position);
                page.setAlpha(factor);
            }
        } else {// (1,+Infinity]
            page.setAlpha(minAlpha);
        }

    }

}
