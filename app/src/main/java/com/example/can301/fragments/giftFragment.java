package com.example.can301.fragments;

import static android.content.ContentValues.TAG;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.can301.R;
import com.example.can301.adapter.GiftGridAdapter;
import com.example.can301.entity.GiftItem;
import com.example.can301.net.NetAgent;
import com.example.can301.net.OkHttpUtils;
import com.example.can301.utilities.FastJsonUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class giftFragment extends Fragment {
    private GridView gridView;
    GiftGridAdapter adapter;
    public int cash = 100000;//this need data from database. The cash of users.
    TextView Cash;
    private String id;
    private View root;
    private String backendUrl = "http://47.94.44.163:8080";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_gift,container,false);
        init(root);
        return root;
    }
    public void readID(){
        SharedPreferences mypref = root.getContext().getSharedPreferences("config", root.getContext().MODE_PRIVATE);
        id = mypref.getString("id", "1");
        Cash.setText(id);
    }
    private void init(View root){
        gridView = (GridView) root.findViewById(R.id.gift_grid_layout);
        List<GiftItem> list = GiftItem.getDefaultList();
        Log.d(TAG, "onCreateView: "+list.get(0).name+list.get(1).name);
        adapter = new GiftGridAdapter(getContext(),list);
        gridView.setAdapter(adapter);
        Cash = (TextView) root.findViewById(R.id.cashTV);
        //Cash.setText("Cash : " + cash );
        readID();
        getCash();
}

    private void getCash(){

        HashMap hashMap = new HashMap();
        hashMap.put("id",id);
        //System.out.println(getActivity());
        OkHttpUtils.getSoleInstance().doPostForm(backendUrl + "/user/querycash/", new NetAgent() {
            @Override
            public void onSuccess(String result) {
                Map<String, String> map = FastJsonUtils.stringToCollect(result);
                String cash = String.valueOf(map.get("cash"));
                if (String.valueOf(map.get("status")).equals("200")) {
                    //Toast.makeText(getActivity().getApplicationContext(), "get cash", Toast.LENGTH_SHORT).show();
                    Cash.setText("Cash: " + cash);
                } else {
                    Toast toastCenter = Toast.makeText(getActivity().getApplicationContext(), "no", Toast.LENGTH_SHORT);
                    toastCenter.setGravity(Gravity.CENTER, 0, 0);
                    toastCenter.show();
                }
            }
            @Override
            public void onError(Exception e) {
                if (isAdded()) {
                    e.printStackTrace();
                    Toast center = Toast.makeText(getActivity().getApplicationContext(), "network failure", Toast.LENGTH_SHORT);
                    center.setGravity(Gravity.CENTER, 0, 0);
                    center.show();
                }
            }
        },hashMap,getActivity());
    }



}
