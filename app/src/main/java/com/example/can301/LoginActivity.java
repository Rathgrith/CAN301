package com.example.can301;

import static com.example.can301.R.string.checkLogged;
import static com.example.can301.utilities.ValidateUtil.validate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.can301.net.NetAgent;
import com.example.can301.net.OkHttpUtils;
import com.example.can301.utilities.FastJsonUtils;

import java.util.HashMap;
import java.util.Map;

// Key package for connection！！！
//import com.zhy.http.okhttp.OkHttpUtils;
//import com.zhy.http.okhttp.callback.StringCallback;

public class LoginActivity extends AppCompatActivity {
    private Button lBtn;
    private EditText inputEmail;
    private EditText inputPassword;
    private String status = String.valueOf('a');
    // notifications
    private String success = "Login success.";
    private String fail = "Login failed";
    private String l2durl = "file:///android_asset/www/l2d.html";
    private TextView rBtn,title;
    private WebView mWebview;
    private boolean loginFlag;
    private Handler handler = new Handler(Looper.getMainLooper());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = getSharedPreferences("config",Context.MODE_PRIVATE);
        if (sharedPref.getBoolean(getString(checkLogged),false)) {
            jumpToMain();
        }
        setContentView(R.layout.activity_login);

        // find element
        lBtn = findViewById(R.id.btn_login);
        rBtn = findViewById(R.id.regLink);
        mWebview = findViewById(R.id.l2d);
        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        title = findViewById(R.id.caption1);
        loginFlag = false;

        //登录flag，需要后端控制user登陆状态，存一个token
        if(loginFlag){
            // 直接跳转
           jumpToMain();
        }
        this.loadL2d(l2durl);

        lBtn.setOnClickListener(this::onClick);
        rBtn.setOnClickListener(this::onClickRegisterLink);
    }


    private void loadL2d(String url){
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.getSettings().setAllowFileAccess(true);
        mWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebview.getSettings().setDomStorageEnabled(true);
        mWebview.getSettings().setDefaultTextEncodingName("utf-8");
        mWebview.loadUrl(url);
    }

    private boolean checkEditText(EditText editText){
        String toCheck = editText.getText().toString();
        int length = toCheck.length();
        if(length<6){
            Toast.makeText(this,"Ensure password longer than 6",Toast.LENGTH_SHORT).show();
            return false;
        }
        boolean containNum = false;
        boolean containLetter = false;
        for (int i = 0; i < length; i++) {
            if(Character.isDigit(toCheck.charAt(i))){
                containNum = true;
            }else{
                containLetter = true;
            }
            if(containNum && containLetter){
                return true;
            }
        }
        if(!(containNum && containLetter)){
            Toast.makeText(this,"Ensure password contains both letter and word",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void onClick(View view){
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        // System.out.println(validate(email));
        if(!validate(email)){
            Toast.makeText(getApplicationContext(), "incorrect email!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!checkEditText(inputPassword)){
            return;
        }
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("email", email );
        hashMap.put("password",password);
        OkHttpUtils.getSoleInstance().doPostForm("http://10.0.2.2:4523/m1/1900048-0-default/user/login?apifoxApiId=48980389", new NetAgent() {
            @Override
            public void onSuccess(String result) {
                Map<String,String> map =  FastJsonUtils.stringToCollect(result);
                boolean isSuccess = Boolean.parseBoolean(map.get("isSuccess"));
                String message = map.get("message");
                if(isSuccess){
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedPref = getSharedPreferences("config",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("isLoggedIn", true);
                    editor.apply();
                    jumpToMain();
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

    private void onClickRegisterLink(View view){
        jumpToRegister();
    }

    //跳转方法
    private void jumpToMain(){
        Intent intent = null;
        //setContentView(R.string.login_flag);
        intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void jumpToRegister(){
        Intent intent = null;
        //setContentView(R.string.login_flag);
        intent = new Intent(this, RegisterActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    // saving info in shared preferences file for displaying it on the profile
    public void onSave(){
        // retrieve info from email edit field
        String email = inputEmail.getText().toString();

        // shared preferences save info
        SharedPreferences mypref = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = mypref.edit();
        editor.putString("keyemail", email);
        editor.commit();
    }
}