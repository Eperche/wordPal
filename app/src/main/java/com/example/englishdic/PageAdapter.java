package com.example.englishdic;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

public class PageAdapter extends FragmentPagerAdapter {

    private int numOfTabs;

    PageAdapter(FragmentManager fm,int numOfTabs){

        super(fm);
        this.numOfTabs=numOfTabs;
    }

    @Override
    public Fragment getItem(int i) {

        switch (i){
            case 0:
                return new MainPageFragment();
            case 1:
                return new Fragment2();
                default:
                    return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
