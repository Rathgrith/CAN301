package com.example.can301.fragments;

import static com.example.can301.R.id.viewPage;
import static com.example.can301.R.id.vp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.can301.R;
import com.example.can301.adapter.PageViewAdapter_tuto;

import me.relex.circleindicator.CircleIndicator3;

public class Fragment_tutorial extends Fragment {
    private View root;
    private ViewPager2 mPager;
    private FragmentStateAdapter pagerAdapter;
    private int num_page = 4;
    private CircleIndicator3 mIndicator;

    public Fragment_tutorial(){
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        root =  inflater.inflate(R.layout.activity_tutorial, container, false);
        return root;
    }


}