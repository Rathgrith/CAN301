package com.example.can301;

import static com.example.can301.utilities.ValidateUtil.validate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.can301.net.NetAgent;
import com.example.can301.net.OkHttpUtils;
import com.example.can301.utilities.FastJsonUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton profileBtn;
    private FloatingActionButton flushbtn;
    private ImageView [] seatList;
    private int[] seatStatus;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_seatview);
        seatList = new ImageView[16];

        flushbtn = findViewById(R.id.floatingActionButton2);

        findSeat();
        getSeatStatus();
        //clearSeat();
        // System.out.println("SeatList" + seatList.toString());

        profileBtn = findViewById(R.id.floatingActionButton);
        profileBtn.setOnClickListener(this::onClick);
        flushbtn.setOnClickListener(this::flush);

    }
    public void flush(View view){
        Intent intent = null;
        intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
        clearSeat();
    }

    private void getSeatStatus1(){
        HashMap hashMap = new HashMap();
        try {
            OkHttpUtils.getSoleInstance().doPostForm("http://10.0.2.2:8081/seat/listseat", new NetAgent() {
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
                    if (status.equals("200")) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast toastCenter = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
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
            }, hashMap, this);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
    private void getSeatStatus(){
        HashMap hashMap = new HashMap();
        OkHttpUtils.getSoleInstance().doPostForm("http://10.0.2.2:8081/seat/listseat", new NetAgent() {
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
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else {
                    Toast toastCenter = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
                    toastCenter.setGravity(Gravity.CENTER, 0, 0);
                    toastCenter.show();
                }
                int i=0;
                for (ImageView seat: seatList) {
                    if(seatStatus[i]==0){
                        seat.setImageDrawable(getResources().getDrawable(R.drawable.greenseat));
                    }
                    if(seatStatus[i]==1){
                        seat.setImageDrawable(getResources().getDrawable(R.drawable.redseat));
                    }
                    i++;

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



    private void findSeat(){
        for (int i = 1; i < 17; i++) {
            String seatID = "seat" + i;
            // System.out.println(seatID);
            int resID = getResources().getIdentifier(seatID, "id", getPackageName());
            // System.out.println(resID);
            ImageView seat = (ImageView) findViewById(resID);
            // System.out.println(seat.toString());
            seatList[i-1] = seat;
        }
    }

    private void clearSeat(){
        int i=0;
        for (ImageView seat: seatList) {
            if(seatStatus[i]==0){
                seat.setImageDrawable(getResources().getDrawable(R.drawable.greenseat));
            }
            if(seatStatus[i]==1){
                seat.setImageDrawable(getResources().getDrawable(R.drawable.redseat));
            }
            seat.setImageDrawable(getResources().getDrawable(R.drawable.redseat));
            i++;


        }
    }


    private void onClick(View view){
        jumpToProfile();
    }

    private void jumpToProfile(){
        Intent intent = null;
        intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
    }


}