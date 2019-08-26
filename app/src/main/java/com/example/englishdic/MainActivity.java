package com.example.englishdic;

import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.FirebaseDatabase;

import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements ExampleDialog.ExampleDialogListener/*/, MainPageFragment.MainPageFragmentListener */{
    public Toolbar toolbar;
    public  TabLayout tabLayout;
    public  ViewPager viewPager;
    public PagerAdapter pagerAdapter;
    public TabItem tabWords;
    public  TabItem tabSentences;
    private  List<Languages> languagesList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);



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


  /*  @Override
    public void applyList(List<Languages> mlist) {
        languagesList=mlist;
        Log.i("STT","MainActivty applyList size :"+mlist.size());
        ExampleDialog exampleDialog =new ExampleDialog();
        exampleDialog.sendGetRequestLanguage(mlist);
    }*/
}
