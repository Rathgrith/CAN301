package com.example.can301;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.can301.net.NetAgent;
import com.example.can301.net.OkHttpUtils;
import com.example.can301.utilities.FastJsonUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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
    private int taken = 0;
    private int ava = 0;
    private int unk = 0;
    private Button checkinBtn;
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
        checkinBtn = findViewById(R.id.checkinBtn);
        testTV = findViewById(R.id.testView);
        testTV.setText(type + "," + startIndex + ", " + seatNumber);
        tableStats = findViewById(R.id.tableStats);
        seatList = new ImageView[seatNumber];
        findSeat();
        //
        try{
            initSeatStatus();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        checkinBtn.setOnClickListener(this::onCheckInClick);
        assignSeatBtn(seatList);
        readID();
    }

    private void gainCoin(String id, int credit){
            HashMap hashMap = new HashMap();
            hashMap.put("id", id);
            hashMap.put("salary", String.valueOf(credit));
            //System.out.println(getActivity());
            OkHttpUtils.getSoleInstance().doPostForm(backendUrl + "/user/getcash/", new NetAgent() {
                @Override
                public void onSuccess(String result) {
                    Map<String, String> map = FastJsonUtils.stringToCollect(result);
                    String cash = String.valueOf(map.get("cash"));
                    if (map.get("status").equals("200")) {
                        //Toast.makeText(getActivity().getApplicationContext(), "get cash", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast toastCenter = Toast.makeText(getApplicationContext(), "no", Toast.LENGTH_SHORT);
                        toastCenter.setGravity(Gravity.CENTER, 0, 0);
                        toastCenter.show();
                    }
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean isSameDay(Date date1, Date date2) {
        LocalDate localDate1 = date1.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate localDate2 = date2.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        return localDate1.isEqual(localDate2);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void onCheckInClick(View view) {
        int credit = 0;
        String userid = "1";
        SharedPreferences userpref = getSharedPreferences("config", MODE_PRIVATE);
        userid = userpref.getString("id", "1");
        SharedPreferences mypref = getSharedPreferences("tab", MODE_PRIVATE);
        Date currdate = new Date(); //or simply new Date();
        Date archivedDate = new Date(mypref.getLong("date", 0));

        //!
        // one seat 5 credits by checkin Commit (max 25), global time recorded, uncomment condition to activate date detection
        if(!isSameDay(currdate,archivedDate)){
            String stringStatus =  mypref.getString("statuslist", "null");
            for (int i = startIndex; i < startIndex+seatNumber; i++) {
                if(seatStatus[i] != Integer.parseInt(stringStatus.substring(i-startIndex,i-startIndex+1))){
                    credit += 5;
                }
            }
            if (credit > 25) credit = 25;
            int old_ava = mypref.getInt("available", 0);
            int old_unk = mypref.getInt("unknown", 0);
            int old_tak = mypref.getInt("taken", 0);
            long millis = currdate.getTime();
            SharedPreferences.Editor editor = mypref.edit();
            editor.putLong("date", millis);
            gainCoin(userid, credit);
            Toast.makeText(getApplicationContext(), "you earned " + credit, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(TableActivity.this, mainTestActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(getApplicationContext(), "You have taken todays credit!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(TableActivity.this, mainTestActivity.class);
            startActivity(intent);
        }

        //System.out.println(old_tak + ", " + old_ava + ", "+ old_unk + ", " + stringStatus + ", " + myDate);
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
    }

    private void initSeatStatus(){
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
                String statusKey = "";
                Object[] b = FastJsonUtils.toArray(a);
                int[] ss = new int[b.length];
                for (int i = 0; i < ss.length; i++) {
                    ss[i] = Integer.parseInt(b[i].toString());
                    statusKey += b[i].toString();
                }
                seatStatus = ss;
                // System.out.println(seatStatus);
                if (status.equals("200")) {
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
                SharedPreferences mypref = getSharedPreferences("tab", MODE_PRIVATE);
                SharedPreferences.Editor editor = mypref.edit();
                editor.putString("statuslist", statusKey.substring(startIndex, startIndex + seatNumber));
                editor.putInt("available", ava);
                editor.putInt("unknown", unk);
                editor.putInt("taken", taken);
                editor.apply();
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
                    //Toast.makeText(getApplicationContext(), "Updated seat info", Toast.LENGTH_SHORT).show();
                } else {
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
                } else {
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