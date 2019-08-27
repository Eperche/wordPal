package com.powerbros.yourdictionary;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.view.ViewGroup;


import java.util.HashMap;
import java.util.Map;

public class PageAdapter extends FragmentPagerAdapter {

    private int numOfTabs;
    private Map<Integer, String> mFragmentTags;
    private FragmentManager mFragmentManager;
    private Context mContext;





    PageAdapter(FragmentManager fm, int numOfTabs){

        super(fm);
        this.mFragmentManager = fm;
        mFragmentTags = new HashMap<Integer, String>();
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

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Object obj = super.instantiateItem(container,position);

        if(obj instanceof Fragment){
            Fragment f = (Fragment) obj;
            String tag = f.getTag();
            mFragmentTags.put(position,tag);
        }
        return  obj;
    }
    public Fragment getFragment(int position) {
        String tag = mFragmentTags.get(position);
        if(tag== null){
            return null;
        }
        return mFragmentManager.findFragmentByTag(tag);
    }
}
