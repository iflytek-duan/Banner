package com.zihao.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.zihao.banner.adapter.BannerAdapter;
import com.zihao.banner.bean.BannerTestModel;
import com.zihao.banner.view.Banner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

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
        banner.setAutoLoop(true);
        banner.setBannerAdapter(new BannerAdapter<BannerTestModel>(dataList) {

            @Override
            protected void bindTips(TextView tv, BannerTestModel bannerTestModel) {

            }

            @Override
            public void bindImage(ImageView iv, BannerTestModel bannerTestModel) {
                iv.setBackgroundResource(bannerTestModel.getImgRes());
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
