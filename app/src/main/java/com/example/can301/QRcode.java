package com.example.can301;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QRcode extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_qrcode);
        IntentIntegrator intentIntegrator = new IntentIntegrator(QRcode.this);
        // start scanning
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // get result

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Failure", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Successfully" , Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        //identify the id of activity(qr code store the id)
        if(result.getContents().equals("1")){
            jumpToTable1();
        }
        else if(result.getContents().equals("2")){
            jumpToTable2();
        }
        else if(result.getContents().equals("3")){
            jumpToHorTable1();
        }
        else if(result.getContents().equals("4")){
            jumpToHorTable2();
        }
        else if(result.getContents().equals("5")){
            jumpToSqrTable1();
        }
        else if(result.getContents().equals("6")){
            jumpToSqrTable2();
        }

    }

    private void jumpToTable1(){
        if(!ifSwitchToTable()){
            return;
        }
        // int id = view.getId();
        Intent intent = null;
        intent = new Intent(QRcode.this, TableActivity.class);
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

    private void jumpToTable2(){
        if(!ifSwitchToTable()){
            return;
        }
        Intent intent = null;
        intent = new Intent(QRcode.this, TableActivity.class);
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

    private void jumpToHorTable1(){
        if(!ifSwitchToTable()){
            return;
        }
        Intent intent = null;
        intent = new Intent(QRcode.this, TableActivity.class);
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

    private void jumpToHorTable2(){
        if(!ifSwitchToTable()){
            return;
        }
        Intent intent = null;
        intent = new Intent(QRcode.this, TableActivity.class);
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

    private void jumpToSqrTable1(){
        if(!ifSwitchToTable()){
            return;
        }
        Intent intent = null;
        intent = new Intent(QRcode.this, TableActivity.class);
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

    private void jumpToSqrTable2(){
        if(!ifSwitchToTable()){
            return;
        }
        Intent intent = null;
        intent = new Intent(QRcode.this, TableActivity.class);
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
    private Boolean ifSwitchToTable(){
        SharedPreferences sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
        if(!sharedPreferences.getBoolean("isLoggedIn",false)){
            Log.d("qr", "qrcode: hasGone");
            if(!sharedPreferences.getBoolean("hasReadTutorial",false)){
                Log.d("qr", "qrcode: hasGone1");
                Intent intent = new Intent(QRcode.this,TutorialActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return false;
            }else {
                Log.d("qr", "qrcode: hasGone1");
                Intent intent = new Intent(QRcode.this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return false;
            }

        }
        return true;
    }
}
