package com.powerbros.yourdictionary;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.FirebaseDatabase;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements ExampleDialog.ExampleDialogListener/*/, MainPageFragment.MainPageFragmentListener */{
    public Toolbar toolbar;
    public  TabLayout tabLayout;
    public static ViewPager viewPager;
    public static PagerAdapter pagerAdapter;
    public TabItem tabWords;
    public  TabItem tabSentences;
    private  List<Languages> languagesList = new ArrayList<>();
    private int RECORD_AUDIO_CODE = 1;

    static {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



       // FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "You have already granted permission ", Toast.LENGTH_SHORT).show();
        }
        else{
            requestRecordAudio();
        }


        toolbar =(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("My Dictionary");
        setSupportActionBar(toolbar);

        tabLayout =(TabLayout)findViewById(R.id.tablayout);
        tabWords =(TabItem) findViewById(R.id.wordPage);
        tabSentences=(TabItem)findViewById(R.id.sentences);
        viewPager =(ViewPager)findViewById(R.id.viewPager);

    }

    private void requestRecordAudio() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.RECORD_AUDIO)){

            new AlertDialog.Builder(this).setTitle("Permisson Needed")
                    .setMessage("This permisson is needed because of microphone usage ")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.RECORD_AUDIO},RECORD_AUDIO_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        }
        else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},RECORD_AUDIO_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode ==RECORD_AUDIO_CODE){
            if(grantResults.length >  0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
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
