package com.whf.indicatorfromios;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by WHF on 2017/3/19.
 */

public class IndicatonAdapter extends PagerAdapter {

    private IndicatorView mIndicatorView;
    private List<View> mListView;

    public IndicatonAdapter(IndicatorView indicatorView,List<View> viewList){
        this.mIndicatorView = indicatorView;
        mListView = viewList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mListView.get(position));
        return mListView.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mListView.get(position));
    }

    @Override
    public int getCount() {
        return mListView.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return  mIndicatorView.setCurIndex(position);
    }
}
