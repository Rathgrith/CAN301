package com.example.can301.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.can301.fragments.Fragment_3rd;
import com.example.can301.fragments.Fragment_4th;
import com.example.can301.fragments.Fragment_5th;
import com.example.can301.fragments.Fragment_6th;

public class PageViewAdapter extends FragmentStateAdapter {
    public int mCount;

    public PageViewAdapter(FragmentActivity fa, int count) {
        super(fa);
        mCount = count;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);

        if(index==0) return new Fragment_3rd();
        else if(index==1) return new Fragment_4th();
        else if(index==2) return new Fragment_5th();
        else if(index==3) return new Fragment_6th();
        else return new Fragment_3rd();

    }

    @Override
    public int getItemCount() {
        return 2000;
    }

    public int getRealPosition(int position) { return position % mCount; }

}
