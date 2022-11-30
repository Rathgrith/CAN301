package com.example.can301.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.can301.QRcode;
import com.example.can301.R;
import com.example.can301.TableActivity;
import com.example.can301.net.NetAgent;
import com.example.can301.net.OkHttpUtils;
import com.example.can301.utilities.FastJsonUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.Map;

public class Fragment_3rd extends Fragment {
    private View root;

    public Fragment_3rd(){

    }

    // 先写死椅子数量 60
    private int seatNumber;
    private FloatingActionButton profileBtn;
    private FloatingActionButton flushbtn;

    private ImageView[] seatList;
    private int[] seatStatus;
    //private WebView mWebview;
    //private String weatherurl = "file:///android_asset/www/weather.html";
    private ImageView table1Btn;
    private ImageView table2Btn;
    private ImageView horTable1Btn;
    private ImageView horTable2Btn;
    private ImageView sqrTable1Btn;
    private ImageView sqrTable2Btn;
    private String backendUrl;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_3rd, container,false);
        initVariable();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initVariable(){
        // 写死椅子数 60
        seatNumber = 60;
        seatList = new ImageView[seatNumber];
        // mWebview = findViewById(R.id.weather);
        table1Btn = (ImageView) root.findViewById(R.id.table1);
        table2Btn = (ImageView)root.findViewById(R.id.table2);
        horTable1Btn = (ImageView)root.findViewById(R.id.horizontalTable1);
        horTable2Btn = (ImageView)root.findViewById(R.id.horizontalTable2);
        sqrTable1Btn = (ImageView)root.findViewById(R.id.squareTable1);
        sqrTable2Btn = (ImageView)root.findViewById(R.id.squareTable2);
        flushbtn = (FloatingActionButton) root.findViewById(R.id.floatingActionButton2);
        profileBtn = root.findViewById(R.id.floatingActionButton);
        Resources res = getResources();
        backendUrl = "http://47.94.44.163:8080";
        findSeat();
        getSeatStatus();
        profileBtn.setOnClickListener(this::onClick);
        flushbtn.setOnClickListener(this::flush);
        table1Btn.setOnClickListener(this::jumpToTable1);
        table2Btn.setOnClickListener(this::jumpToTable2);
        horTable1Btn.setOnClickListener(this::jumpToHorTable1);
        horTable2Btn.setOnClickListener(this::jumpToHorTable2);
        sqrTable1Btn.setOnClickListener(this::jumpToSqrTable1);
        sqrTable2Btn.setOnClickListener(this::jumpToSqrTable2);
    }
    private void flush(View view){
        getActivity().finish();
        getActivity().overridePendingTransition(0, 0);
        startActivity(getActivity().getIntent());
        getActivity().overridePendingTransition(0, 0);
    }

    private void getSeatStatus(){
        HashMap hashMap = new HashMap();
        //System.out.println(getActivity());
        OkHttpUtils.getSoleInstance().doPostForm(backendUrl + "/seat/listseat", new NetAgent() {
            @Override
            public void onSuccess(String result) {
                Map<String, String> map = FastJsonUtils.stringToCollect(result);
                String status = map.get("status");
                String message = map.get("status");
                String a = map.get("seatstatus");
                Object[] b = FastJsonUtils.toArray(a);
                int[] ss = new int[b.length];
                for (int i = 0; i < ss.length; i++) {
                    ss[i] = Integer.parseInt(b[i].toString());
                }
                seatStatus = ss;
                System.out.println(seatStatus);
                if (status.equals("200")) {
                    //Toast.makeText(getActivity().getApplicationContext(), "Updated seat info", Toast.LENGTH_SHORT).show();

                } else {
                    Toast toastCenter = Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT);
                    toastCenter.setGravity(Gravity.CENTER, 0, 0);
                    toastCenter.show();
                }
                int i=0;
                for (ImageView seat: seatList) {
                    if(seatStatus[i]==0){
                        seat.setImageDrawable(getResources().getDrawable(R.drawable.redseat));
                    }
                    else if(seatStatus[i]==1){
                        seat.setImageDrawable(root.getResources().getDrawable(R.drawable.greenseat));
                    }
                    else if(seatStatus[i]==2){
                        seat.setImageDrawable(root.getResources().getDrawable(R.drawable.grayseat));
                    }else
                        seat.setImageDrawable(root.getResources().getDrawable(R.drawable.grayseat));
                    i++;
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



    private void findSeat(){
        // 数组越界了就看这里
        for (int i = 1; i < seatNumber+1; i++) {
            String seatID = "seat" + i;
            // System.out.println(seatID);
            int resID = getResources().getIdentifier(seatID, "id", getActivity().getPackageName());
            // System.out.println(resID);
            ImageView seat = (ImageView) root.findViewById(resID);
            // System.out.println(seat.toString());
            seatList[i-1] = seat;
        }
    }



    private void onClick(View view){
        jumpToProfile();
    }

    private void jumpToProfile(){
        Intent intent = null;
        intent = new Intent(requireContext(), QRcode.class);
        startActivity(intent);
        Toast center = Toast.makeText(getActivity().getApplicationContext(), "Please Scan QR code", Toast.LENGTH_SHORT);
        center.setGravity(Gravity.CENTER, 0, 0);
        center.show();
    }
    private void jumpToTable1(View view){
        // int id = view.getId();
        Intent intent = null;
        intent = new Intent(requireContext(), TableActivity.class);
        Bundle tableBundle = new Bundle();
        //请求int[] 前16位
        //数组起始下标为0，对应主页元素seat15
        tableBundle.putString("startIndex", "0");
        //长度为16
        tableBundle.putString("seatNumber", "16");
        tableBundle.putString("type", "0");
        intent.putExtras(tableBundle);
        startActivity(intent);
    }

    private void jumpToTable2(View view){
        Intent intent = null;
        intent = new Intent(requireContext(), TableActivity.class);
        Bundle tableBundle = new Bundle();
        //请求int[] 16-32位
        //数组起始下标为16，对应主页元素seat32
        tableBundle.putString("startIndex", "16");
        //长度为16
        tableBundle.putString("seatNumber", "16");
        tableBundle.putString("type", "0");
        intent.putExtras(tableBundle);
        startActivity(intent);
    }

    private void jumpToHorTable1(View view){
        Intent intent = null;
        intent = new Intent(requireContext(), TableActivity.class);
        Bundle tableBundle = new Bundle();
        //请求int[] 32-42位
        //数组起始下标为32，对应主页元素seat33
        tableBundle.putString("startIndex", "32");
        //长度为10
        tableBundle.putString("seatNumber", "10");
        tableBundle.putString("type", "1");
        intent.putExtras(tableBundle);
        startActivity(intent);
    }

    private void jumpToHorTable2(View view){
        Intent intent = null;
        intent = new Intent(requireContext(), TableActivity.class);
        Bundle tableBundle = new Bundle();
        //请求int[] 42-52位
        //数组起始下标为42，对应主页元素seat43
        tableBundle.putString("startIndex", "42");
        //长度为10
        tableBundle.putString("seatNumber", "10");
        tableBundle.putString("type", "1");
        intent.putExtras(tableBundle);
        startActivity(intent);
    }

    private void jumpToSqrTable1(View view){
        Intent intent = null;
        intent = new Intent(requireContext(), TableActivity.class);
        Bundle tableBundle = new Bundle();
        //请求int[] 52-56位
        //数组起始下标为52，对应主页元素seat53
        tableBundle.putString("startIndex", "52");
        //长度为4
        tableBundle.putString("seatNumber", "4");
        tableBundle.putString("type", "2");
        intent.putExtras(tableBundle);
        startActivity(intent);
    }

    private void jumpToSqrTable2(View view){
        Intent intent = null;
        intent = new Intent(requireContext(), TableActivity.class);
        Bundle tableBundle = new Bundle();
        //请求int[] 56-60位
        //数组起始下标为56，对应主页元素seat55
        tableBundle.putString("startIndex", "56");
        //长度为4
        tableBundle.putString("seatNumber", "4");
        tableBundle.putString("type", "2");
        intent.putExtras(tableBundle);
        startActivity(intent);
    }
    @Override
    public void onResume() {
        super.onResume();
        getSeatStatus();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

}
