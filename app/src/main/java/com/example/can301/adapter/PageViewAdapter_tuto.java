package com.example.can301.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.can301.fragments.Fragment_3rd;
import com.example.can301.fragments.Fragment_4th;
import com.example.can301.fragments.Fragment_5th;
import com.example.can301.fragments.Fragment_6th;
import com.example.can301.fragments.Fragment_table;

public class PageViewAdapter_tuto extends FragmentStateAdapter {
    public int mCount;

        public PageViewAdapter_tuto(FragmentActivity fa, int count) {
            super(fa);
            mCount = count;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            int index = getRealPosition(position);

            System.out.println("Check Check Fragment create check");

            if(index==0) return new Fragment_3rd();
            else if(index==1) return new Fragment_table();
            else return new Fragment_3rd();

        }

        @Override
        public int getItemCount() {
            return 2000;
        }

        public int getRealPosition(int position) { return position % mCount; }

    }
