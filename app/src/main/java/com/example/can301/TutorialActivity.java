package com.example.can301;

import static com.example.can301.R.id.btn_finish;
import static com.example.can301.R.id.btn_skip;
import static com.example.can301.R.id.viewPage;
import static com.example.can301.R.id.vp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.can301.adapter.PageViewAdapter;
import com.example.can301.adapter.PageViewAdapter_tuto;
import com.example.can301.fragments.Fragment_3rd;
import com.example.can301.fragments.Fragment_4th;
import com.example.can301.fragments.Fragment_5th;
import com.example.can301.fragments.Fragment_6th;
import com.example.can301.fragments.Fragment_table;
import com.example.can301.fragments.Fragment_tutorial;

import me.relex.circleindicator.CircleIndicator3;

public class TutorialActivity extends AppCompatActivity {

    private View root;
    private ViewPager2 mPager;
    private FragmentStateAdapter pagerAdapter;
    private int num_page = 3;
    private CircleIndicator3 mIndicator;

    private Button skip;
    private Button finish;
    Fragment_tutorial fragment_tutorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = getSharedPreferences("config", Context.MODE_PRIVATE);
        if (sharedPref.getBoolean("hasReadTutorial", false)) jumpToLogin();
        setContentView(R.layout.activity_tutorial);
        System.out.println("hmmmmmm");
        setInt();
        skip=findViewById(btn_skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), mainTestActivity.class);
                startActivity(intent);
            }
        });

        finish=findViewById(btn_finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("hasReadTutorial",true);
                editor.apply();
                jumpToLogin();
            }
        });
    }

    private void setInt(){
        mPager =findViewById(R.id.vp);
        //Adapter
        pagerAdapter = new PageViewAdapter_tuto(this, num_page);
        mPager.setAdapter(pagerAdapter);
        //Indicator
        mIndicator = findViewById(R.id.indi);
        mIndicator.setViewPager(mPager);
        mIndicator.createIndicators(num_page,0);
        //ViewPager Setting
        mPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        mPager.setCurrentItem(3);
        mPager.setOffscreenPageLimit(3);

        mPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (positionOffsetPixels == 0) {
                    mPager.setCurrentItem(position);
                }
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mIndicator.animatePageSelected(position%num_page);
            }

        });
    }
    private void jumpToLogin() {
        Intent intent = null;
        //setContentView(R.string.login_flag);
        intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}

