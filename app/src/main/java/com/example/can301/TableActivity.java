package com.example.can301;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.can301.net.NetAgent;
import com.example.can301.net.OkHttpUtils;
import com.example.can301.utilities.FastJsonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TableActivity extends Activity {
    private int startIndex;
    private int seatNumber;
    private String type;
    private TextView testTV;
    private ImageView[] seatList;
    private int[] seatStatus;
    private ArrayList<Integer> seatIDs;
    private String backendUrl;
    private static int taken;
    private static int ava;
    private static int unk;
    private TextView tableStats;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        seatIDs = new ArrayList<Integer>();
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setContentView(R.layout.activity_table_v);
        backendUrl = "http://47.94.44.163:8080";
        Intent intent = getIntent();
        Bundle infoBundle = intent.getExtras();
        // 在res中获取
            Resources res =getResources();
            String[] typeArray= res.getStringArray(R.array.tableType);
        startIndex = Integer.parseInt(
                infoBundle.getString("startIndex"));
        seatNumber = Integer.parseInt(
                infoBundle.getString("seatNumber"));;
        type = infoBundle.getString("type");

        if(type.equals("0"))
            setContentView(R.layout.activity_table_v);
        else if(type.equals("1"))
            setContentView(R.layout.activity_table_h);
        else
            setContentView(R.layout.activity_table_sqr);
        testTV = findViewById(R.id.testView);
        testTV.setText(type + "," + startIndex + ", " + seatNumber);
        tableStats = findViewById(R.id.tableStats);
        seatList = new ImageView[seatNumber];
        findSeat();
        //
        try{
            getSeatStatus();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        assignSeatBtn(seatList);
        readID();
    }

    private void findSeat(){
        // 数组越界了就看这里
        int j = 0;
        for (int i = 1; i < seatNumber+1; i++) {
            String seatID = "seat" + i;;
            //System.out.println(seatID);
            int resID = getResources().getIdentifier(seatID, "id", getPackageName());
            // System.out.println(resID);
            seatIDs.add(resID);
            ImageView seat = (ImageView) findViewById(resID);
            //System.out.println(seat.toString());
            seatList[j] = seat;
            j++;
        }
    }

    private void assignSeatBtn(@NonNull ImageView[] seatList){
        for (ImageView seat: seatList) {
            seat.setOnClickListener(this::onSeatClick);
        }
    }

    private void onSeatClick(View view) {
        int id = view.getId();
        int specSeatIdx = startIndex;
        String staticSeatId = "";
        int status = 0;
        int localID = 0;
        for (int i = 0; i<seatIDs.size(); i++) {
            if(id == seatIDs.get(i)){
                specSeatIdx += i;
                staticSeatId = "seat" + (i+1);
                status = seatStatus[startIndex+i];
                localID = i;
            }
        }
        changeSeatStatus(specSeatIdx+1,999);
        getSeatStatus();
//        if(status == 0){
//            seatList[localID].setImageResource(R.drawable.grayseat);
//            //当前status设为2，转灰色
//            status = 2;
//
//            // 这里写上，连后端设置status
//            // 解除下面注释可以随时刷新
//            getSeatStatus();
//        }
//        else if(status == 1) {
//            seatList[localID].setImageResource(R.drawable.redseat);
//            //当前status设为2，转红色
//            status = 0;
//            changeSeatStatus(localID,999);
//            // 这里写上，连后端设置status
//            // 解除下面注释可以随时刷新
//            getSeatStatus();
//        }
//        else if(status == 2){
//            seatList[localID].setImageResource(R.drawable.greenseat);
//            //当前status设为2，转绿色
//            status = 1;
//            changeSeatStatus(localID,999);
//            // 这里写上，连后端设置status
//            // 解除下面注释可以随时刷新
//            getSeatStatus();
//        }
        //System.out.println("color = " + greenID);
        //Toast toastCenter = Toast.makeText(getApplicationContext(), "seat in list index = " + specSeatIdx, Toast.LENGTH_SHORT);
        //toastCenter.setGravity(Gravity.CENTER, 0, 0);
        //toastCenter.show();
    }


    private void getSeatStatus(){
        taken = 0;
        ava = 0;
        unk = 0;
        HashMap hashMap = new HashMap();
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
                // System.out.println(seatStatus);
                if (status.equals("200")) {
                    Toast.makeText(getApplicationContext(), "Updated seat info", Toast.LENGTH_SHORT).show();

                } else {
                    Toast toastCenter = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
                    toastCenter.setGravity(Gravity.CENTER, 0, 0);
                    toastCenter.show();
                }
                int bias=0;
                for (ImageView seat: seatList) {
                    if(seatStatus[startIndex+bias]==0){
                        taken++;
                        seat.setImageDrawable(getDrawable(R.drawable.redseat));
                    }
                    else if(seatStatus[startIndex+bias]==1){
                        ava++;
                        seat.setImageDrawable(getDrawable(R.drawable.greenseat));
                    }
                    else if(seatStatus[startIndex+bias]==2){
                        unk++;
                        seat.setImageDrawable(getDrawable(R.drawable.grayseat));
                    }else{
                        seat.setImageDrawable(getDrawable(R.drawable.grayseat));
                        unk++;
                    }
                    bias++;
                }
                tableStats.setText("// "+ taken + " taken, " + ava + " available, "+ unk +" unknown. //");
            }
            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                Toast center = Toast.makeText(getApplicationContext(), "network failure", Toast.LENGTH_SHORT);
                center.setGravity(Gravity.CENTER, 0, 0);
                center.show();
            }
        },hashMap,this);

    }
    private void changeSeatStatus(int id,int qrresult){

        HashMap hashMap = new HashMap();
        hashMap.put("seatIndex",String.valueOf(id));
        hashMap.put("qrresult",String.valueOf(qrresult));
        OkHttpUtils.getSoleInstance().doPostForm(backendUrl + "/seat/changeSteatStatus", new NetAgent() {
            @Override
            public void onSuccess(String result) {
                Map<String, String> map = FastJsonUtils.stringToCollect(result);
                String status = map.get("status");
                if (status.equals("200")) {
                    Toast.makeText(getApplicationContext(), "Seat status changed", Toast.LENGTH_SHORT).show();

                } else {
                    Toast toastCenter = Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT);
                    toastCenter.setGravity(Gravity.CENTER, 0, 0);
                    toastCenter.show();
                }
                getSeatStatus();
            }
            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                Toast center = Toast.makeText(getApplicationContext(), "network failure", Toast.LENGTH_SHORT);
                center.setGravity(Gravity.CENTER, 0, 0);
                center.show();
            }
        },hashMap,this);

    }

    public void readID(){
        SharedPreferences mypref = getSharedPreferences("config", MODE_PRIVATE);
        String id = mypref.getString("id", "id = x");
        testTV.setText("id = " + id);
    }

}