package com.zihao.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.zihao.banner.Banner;
import com.zihao.banner.adapter.BannerAdapter;
import com.zihao.banner.bean.BannerTestModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private List<BannerTestModel> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        initData();
        final Banner banner = (Banner) findViewById(R.id.main_banner);
        banner.setBannerAdapter(new BannerAdapter<BannerTestModel>(dataList) {

            @Override
            protected void bindTips(TextView tv, BannerTestModel bannerTestModel) {

            }

            @Override
            public void bindImage(ImageView iv, BannerTestModel bannerTestModel) {
                iv.setBackgroundResource(bannerTestModel.getImgRes());
            }

        });
        banner.setOnPageClickListener(new Banner.OnPageClickListener() {
            @Override
            public void onPageClick(int position) {
                Log.e(TAG, "onPageClick:" + position);
            }
        });

    }

    private void initData() {
        dataList = new ArrayList<>();
        dataList.add(new BannerTestModel(R.drawable.banner_bg));
        dataList.add(new BannerTestModel(R.drawable.banner_bg1));
        dataList.add(new BannerTestModel(R.drawable.banner_bg));
        dataList.add(new BannerTestModel(R.drawable.banner_bg1));
        dataList.add(new BannerTestModel(R.drawable.banner_bg));
        dataList.add(new BannerTestModel(R.drawable.banner_bg1));
        dataList.add(new BannerTestModel(R.drawable.banner_bg));
        dataList.add(new BannerTestModel(R.drawable.banner_bg1));
        dataList.add(new BannerTestModel(R.drawable.banner_bg));
        dataList.add(new BannerTestModel(R.drawable.banner_bg1));
        dataList.add(new BannerTestModel(R.drawable.banner_bg));
        dataList.add(new BannerTestModel(R.drawable.banner_bg1));
        dataList.add(new BannerTestModel(R.drawable.banner_bg));
        dataList.add(new BannerTestModel(R.drawable.banner_bg1));
        dataList.add(new BannerTestModel(R.drawable.banner_bg));
        dataList.add(new BannerTestModel(R.drawable.banner_bg1));
        dataList.add(new BannerTestModel(R.drawable.banner_bg2));
    }

}
