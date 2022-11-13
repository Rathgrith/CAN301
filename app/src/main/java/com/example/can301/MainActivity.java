package com.example.can301;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton profileBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seatview);
        profileBtn = findViewById(R.id.floatingActionButton);
        profileBtn.setOnClickListener(this::onClick);
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