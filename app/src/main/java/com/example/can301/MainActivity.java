package com.example.can301;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
// Key package for connection！！！
import com.zhy.http.okhttp.OkHttpUtils;
import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    // Computational Intelligence Vision and Security Lab
    private Button lBtn;
    private EditText inputEmail;
    private EditText inputPassword;
    // notifications
    private String success = "Login success.";
    private String fail = "Login failed";
    private String l2durl = "file:///android_asset/www/l2d.html";
    private TextView rBtn;
    private WebView mWebview;
    private boolean loginFlag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find element
        lBtn = findViewById(R.id.btn_login);
        rBtn = findViewById(R.id.regLink);
        mWebview = findViewById(R.id.l2d);
        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        loginFlag = false;

        //登录flag，需要后端控制user登陆状态，存一个token
        if(loginFlag){
            // 直接跳转
           jumpToMain();
        }
        this.loadL2d(l2durl);


    }


    private void loadL2d(String url){
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.getSettings().setAllowFileAccess(true);
        mWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebview.getSettings().setDomStorageEnabled(true);
        mWebview.getSettings().setDefaultTextEncodingName("utf-8");
        //mWebview.getSettings().setDefaultTextEncodingName("utf-8");
        mWebview.loadUrl(url);
        lBtn.setOnClickListener(this::onClick);
        rBtn.setOnClickListener(this::onClickRegisterLink);
    }


    private void onClick(View view){
        String username = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        // 帐号密码目前明文写死，之后可以另外绑定
       /*邮箱格式校验
        if(!isEmail(username)){
            Toast.makeText(getApplicationContext(), "incorrect email!", Toast.LENGTH_SHORT).show();
        }*/
        if(username.equals("111111") && password.equals("123456")){
            //跳转
            Toast.makeText(getApplicationContext(), success, Toast.LENGTH_SHORT).show();
            jumpToMain();
        }else{
            //fail notification
            Toast toastCenter = Toast.makeText(getApplicationContext(), fail, Toast.LENGTH_SHORT);
            toastCenter.setGravity(Gravity.CENTER,0,0);
            toastCenter.show();
        }



    }


    private void onClickRegisterLink(View view){
        jumpToRegister();
    }

    //跳转方法
    private void jumpToMain(){
        Intent intent = null;
        //setContentView(R.string.login_flag);
        intent = new Intent(MainActivity.this, FunctionalActivity.class);
        startActivity(intent);
        // finish login activity, 这样回退就不会再返回到login
        MainActivity.this.finish();
    }

    private void jumpToRegister(){
        Intent intent = null;
        //setContentView(R.string.login_flag);
        intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
        // finish login activity, 这样回退就不会再返回到login
    }



    private static boolean isEmail(String strEmail) {
        String strPattern = "^[a-zA-Z][w.-]*[a-zA-Z0-9]@[a-zA-Z0-9][w.-]*[a-zA-Z0-9].[a-zA-Z][a-zA-Z.]*[a-zA-Z]$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strEmail);
        return m.matches();
    }
}