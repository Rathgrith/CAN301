package com.example.can301;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.can301.net.NetAgent;
import com.example.can301.net.OkHttpUtils;
import com.example.can301.utilities.FastJsonUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {
    // 先写死椅子数量 16
    private int seatNumber;
    private FloatingActionButton profileBtn;
    private FloatingActionButton flushbtn;
    private ImageView tableBtn;
    private ImageView [] seatList;
    private int[] seatStatus;
    //private WebView mWebview;
    //private String weatherurl = "file:///android_asset/www/weather.html";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 写死椅子数 16
        seatNumber = 16;
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_seatview);
        seatList = new ImageView[seatNumber];
        // mWebview = findViewById(R.id.weather);
        tableBtn = findViewById(R.id.table1);
        flushbtn = findViewById(R.id.floatingActionButton2);

        //loadWeather(weatherurl);
        findSeat();
        getSeatStatus();
        // clearSeat();
        // System.out.println("SeatList" + seatList.toString());

        profileBtn = findViewById(R.id.floatingActionButton);
        profileBtn.setOnClickListener(this::onClick);
        flushbtn.setOnClickListener(this::flush);
        tableBtn.setOnClickListener(this::jumpToTable1);
    }

   /* private void loadWeather(String url){
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.getSettings().setAllowFileAccess(true);
        mWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebview.getSettings().setDomStorageEnabled(true);
        mWebview.getSettings().setDefaultTextEncodingName("utf-8");
        mWebview.loadUrl(url);
    }*/

    private void flush(View view){
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
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
                        seat.setImageDrawable(getResources().getDrawable(R.drawable.redseat));
                    }
                    if(seatStatus[i]==1){
                        seat.setImageDrawable(getResources().getDrawable(R.drawable.greenseat));
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

/*    private void clearSeat(){
        int i=0;
        for (ImageView seat: seatList) {
            if(seatStatus[i]==0){
                seat.setImageDrawable(getResources().getDrawable(R.drawable.grayseat));
            }
            if(seatStatus[i]==1){
                seat.setImageDrawable(getResources().getDrawable(R.drawable.grayseat));
            }
            seat.setImageDrawable(getResources().getDrawable(R.drawable.grayseat));
            i++;
        }
    }*/


    private void onClick(View view){
        jumpToProfile();
    }

    private void jumpToProfile(){
        Intent intent = null;
        intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
    }
    private void jumpToTable1(View view){
        Intent intent = null;
        intent = new Intent(MainActivity.this, TableActivity.class);
        startActivity(intent);
    }


}