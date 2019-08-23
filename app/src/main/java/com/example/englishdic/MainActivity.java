package com.example.englishdic;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SentencesDialog.ExampleDialogListener {
    public Toolbar toolbar;
    public  TabLayout tabLayout;
    public  ViewPager viewPager;
    public PagerAdapter pagerAdapter;
    public TabItem tabWords;
    public  TabItem tabSentences;

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

                    tabLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.colorPrimary));
                    if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
                        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.colorAccent));
                    }
                }
                    else if (tab.getPosition()==2){
                        tabLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.colorPrimary));
                        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
                            getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.colorAccent));
                        }

                    }
                    else {

                        tabLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.colorPrimary));
                        if (Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
                            getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.colorAccent));
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

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));







    }

    @Override
    public void applyTexts(String post_key, String word) {

        Fragment frg = null;
        frg = ((PageAdapter)viewPager.getAdapter()).getFragment(1);


        if (frg != null && frg instanceof Fragment2) {
            ((Fragment2)frg).sendGetRequest(post_key,word,frg);
        }
      /*  Bundle bundle = new Bundle();
        bundle.putString("params", post_key);
        bundle.putString("params", word);
        // set MyFragment Arguments
        Fragment2 myObj = new Fragment2();
        myObj.setArguments(bundle);*/
        viewPager.setCurrentItem(1);

    }
}
