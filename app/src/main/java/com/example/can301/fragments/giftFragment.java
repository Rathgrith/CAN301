package com.example.can301.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.can301.R;
import com.example.can301.RecyclerViewAdapter;
import com.example.can301.customizedClass.DataItem;

public class giftFragment extends Fragment {
    private RecyclerView recyclerView;
    RecyclerViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gift,container,false);
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(root.getContext(), 2)) ;
        init();
        getData();
        return root;
    }

    private void init(){


        adapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void getData(){
        DataItem data = new DataItem(R.drawable.xjtlu_bear, "XJTLU Bear", 1000);
        adapter.addItem(data);
        data = new DataItem(R.drawable.xjtlu_bird, "XJTLU Bird", 1000);
        adapter.addItem(data);
        data = new DataItem(R.drawable.xjtlu_cap, "XJTLU Ball Cap", 2000);
        adapter.addItem(data);
        for (int i = 1; i <= 30; i++) {
            data = new DataItem(R.drawable.item_example, "Example", 1500);
            adapter.addItem(data);
        }

    }
}
