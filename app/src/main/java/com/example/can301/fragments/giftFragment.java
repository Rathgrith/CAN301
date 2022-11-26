package com.example.can301.fragments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.can301.R;
import com.example.can301.adapter.GiftGridAdapter;
import com.example.can301.entity.GiftItem;

import java.util.List;

public class giftFragment extends Fragment {
    private GridView gridView;
    GiftGridAdapter adapter;
    public int cash = 100000;//this need data from database. The cash of users.
    TextView Cash;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gift,container,false);

        init(root);
        return root;
    }

    private void init(View root){
        gridView = (GridView) root.findViewById(R.id.gift_grid_layout);
        List<GiftItem> list = GiftItem.getDefaultList();
        Log.d(TAG, "onCreateView: "+list.get(0).name+list.get(1).name);
        adapter = new GiftGridAdapter(getContext(),list);
        gridView.setAdapter(adapter);
        Cash = (TextView) root.findViewById(R.id.cashTV);
        Cash.setText("Cash : " + cash );
    }


}
