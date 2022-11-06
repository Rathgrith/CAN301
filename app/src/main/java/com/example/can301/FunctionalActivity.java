package com.example.can301;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FunctionalActivity extends AppCompatActivity {
    private FloatingActionButton profileBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_functional);
        profileBtn = findViewById(R.id.floatingActionButton);
        profileBtn.setOnClickListener(this::onClick);
    }

    private void onClick(View view){
        jumpToProfile();
    }

    private void jumpToProfile(){
        Intent intent = null;
        intent = new Intent(FunctionalActivity.this, ProfileActivity.class);
        startActivity(intent);
    }


}