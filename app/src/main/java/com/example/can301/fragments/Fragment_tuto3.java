package com.example.can301.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.can301.R;

public class Fragment_tuto3 extends Fragment {
    private View root;
    public Fragment_tuto3(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        root =  inflater.inflate(R.layout.fragment_tutorial3, container, false);
        return root;
    }
}
