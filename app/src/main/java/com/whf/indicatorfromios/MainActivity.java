package com.whf.indicatorfromios;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private IndicatorView mIndicatorView;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIndicatorView = (IndicatorView) findViewById(R.id.indicatorView);
        mIndicatorView.setTitleArray(new String[]{"新闻","娱乐","游戏"});

        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        List<View> list = new ArrayList<>();
        LayoutInflater inflater = LayoutInflater.from(this);
        View view1 = inflater.inflate(R.layout.view1,null);
        list.add(view1);
        View view2 = inflater.inflate(R.layout.view2,null);
        list.add(view2);
        View view3 = inflater.inflate(R.layout.view3,null);
        list.add(view3);

        mIndicatorView.setOnItemClickListener(new IndicatorView.OnIndicatorItemClickListener() {
            @Override
            public void onIndicatorItemClick(int position) {
                mViewPager.setCurrentItem(position);
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mIndicatorView.setCurIndex(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setAdapter(new IndicatonAdapter(mIndicatorView,list));
    }
}
