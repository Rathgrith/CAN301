package com.example.can301;

import android.os.Bundle;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class ProfileActivity extends AppCompatActivity {

    private TextView emailTV, nicknameTV, cashTV, timeTV, dayTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        emailTV = (TextView) findViewById(R.id.emailTV);
        nicknameTV = (TextView) findViewById(R.id.nicknameTV);
    }

    public void readEmail(){
        SharedPreferences mypref = getSharedPreferences("login", MODE_PRIVATE);
        String email = mypref.getString("keyemail", "User Email");
        emailTV.setText(email);
    }
}