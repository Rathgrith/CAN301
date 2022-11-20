package com.example.can301;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

public class ProfileActivity extends Activity {

    private TextView emailTV, nicknameTV, cashTV, timeTV, dayTV;
    private Button logOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_profile);
        initVariable();
    }

    public void readEmail(){
        SharedPreferences mypref = getSharedPreferences("login", MODE_PRIVATE);
        String email = mypref.getString("keyemail", "User Email");
        emailTV.setText(email);
    }

    private void initVariable(){
        emailTV = (TextView) findViewById(R.id.emailTV);
        nicknameTV = (TextView) findViewById(R.id.nicknameTV);
        logOut = findViewById(R.id.btn_log_out);
        logOut.setOnClickListener(this::onClick);
    }

    private void onClick(View view) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setIcon(R.mipmap.emblem)//set the image
                .setTitle("Confirmation")//set the title of dialogue
                .setMessage("Master! You really want to leave me T_T?")//set the content
                //set buttons
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                })
                .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(String.valueOf("isLoggedIn"),false);
                        editor.putString(String.valueOf(R.string.checkEmail),null);
                        editor.apply();
                        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        dialog.dismiss();
                        startActivity(intent);
                    }
                }).create();
        dialog.show();
    }

}