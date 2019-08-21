package com.example.englishdic;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    TabItem tabWords;
    TabItem tabSentences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar =(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Tablayout");
        setSupportActionBar(toolbar);

        tabLayout =(TabLayout)findViewById(R.id.tablayout);
        tabWords =(TabItem) findViewById(R.id.wordPage);
        tabSentences=(TabItem)findViewById(R.id.sentences);
        viewPager =(ViewPager)findViewById(R.id.viewPager);


        pagerAdapter =new PageAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition()==0){

                    tabLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.colorAccent));
                    if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
                        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.colorAccent));
                    }

                    else if (tab.getPosition()==2){
                        tabLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.colorPrimary));
                        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
                            getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.colorPrimary));
                        }

                    }
                    else {

                        tabLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.colorPrimaryDark));
                        if (Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
                            getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.colorPrimaryDark));
                        }
                    }
                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));







    }
}
