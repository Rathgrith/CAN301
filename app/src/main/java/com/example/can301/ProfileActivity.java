package com.example.can301;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class ProfileActivity extends Activity {

    private TextView emailTV, nicknameTV, cashTV, timeTV, dayTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
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