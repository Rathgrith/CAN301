package com.example.can301;

import android.content.Intent;
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

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton profileBtn;
    private ImageView [] seatList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seatview);
        seatList = new ImageView[16];
        findSeat();
        clearSeat();
        // System.out.println("SeatList" + seatList.toString());
        profileBtn = findViewById(R.id.floatingActionButton);
        profileBtn.setOnClickListener(this::onClick);
    }


    private void getSeatStatus(){
        HashMap hashMap = new HashMap();
        OkHttpUtils.getSoleInstance().doPostForm("http://127.0.0.1:4523/m1/1900048-0-default/seat/listseat", new NetAgent() {
            @Override
            public void onSuccess(String result) {
                Map<String,String> map =  FastJsonUtils.stringToCollect(result);
                boolean isSuccess = Boolean.parseBoolean(map.get("isSuccess"));
                String message = map.get("message");
                if(isSuccess){
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }else{
                    Toast toastCenter = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
                    toastCenter.setGravity(Gravity.CENTER,0,0);
                    toastCenter.show();
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                Toast center = Toast.makeText(getApplicationContext(), "network failure", Toast.LENGTH_SHORT);
                center.setGravity(Gravity.CENTER,0,0);
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
        for (ImageView seat:
             seatList) {
            seat.setImageDrawable(getResources().getDrawable(R.drawable.greenseat));
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