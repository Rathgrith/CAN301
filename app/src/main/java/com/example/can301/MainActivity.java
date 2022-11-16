package com.example.can301;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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