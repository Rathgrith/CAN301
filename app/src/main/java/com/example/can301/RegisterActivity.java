package com.example.can301;

import static com.example.can301.utilities.ValidateUtil.validate;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.can301.net.NetAgent;
import com.example.can301.net.OkHttpUtils;
import com.example.can301.utilities.FastJsonUtils;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    // Computational Intelligence Vision and Security Lab
    private Button rBtn;
    private Button cBtn;
    private EditText inputEmail;
    private EditText inputPassword;
    private EditText confirmPassword;
    // notifications
    private String success = "Register success.";
    private String fail = "Register failed";
    private String l2durl = "file:///android_asset/www/l2d.html";
    private WebView mWebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // find element
        rBtn = findViewById(R.id.btn_register);
        cBtn = findViewById(R.id.btn_cancel);
        mWebview = findViewById(R.id.l2d);
        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        this.loadL2d(l2durl);
    }


    private boolean checkPassword(EditText editText0, EditText editTextC){
        if(!checkEqual(editText0,editTextC)){
            return false;
        }
        if(!checkEditText(editText0)){
            return false;
        }
        return true;
    }



    private boolean checkEqual(EditText editTextOrigin, EditText editTextRepeat){
        if(!editTextOrigin.getText().toString().equals(editTextRepeat.getText().toString())){
            Toast.makeText(this, "you input 2 different password",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    public boolean checkEditText(EditText editText){
        String toCheck = editText.getText().toString();
        int length = toCheck.length();
        if(length<6){
            Toast.makeText(getApplicationContext(),"Ensure password longer than 6",Toast.LENGTH_SHORT).show();
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
    private void loadL2d(String url){
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.getSettings().setAllowFileAccess(true);
        mWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebview.getSettings().setDomStorageEnabled(true);
        mWebview.getSettings().setDefaultTextEncodingName("utf-8");
        //mWebview.getSettings().setDefaultTextEncodingName("utf-8");
        mWebview.loadUrl(url);
        rBtn.setOnClickListener(this::onClick);
        cBtn.setOnClickListener(this::onClickToLogin);
    }



    private void onClick(View view){
        if(!checkPassword(inputPassword,confirmPassword)){
            return;
        }
        String email = inputEmail.getText().toString();
        if(!validate(email)){
            Toast.makeText(getApplicationContext(), "incorrect email!", Toast.LENGTH_SHORT).show();
            return;
        }
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("email", inputEmail.getText().toString() );
        hashMap.put("password",inputPassword.getText().toString());
        OkHttpUtils.getSoleInstance().doPostForm("http://10.0.2.2:4523/m1/1900048-0-default/user/register", new NetAgent() {
            @Override
            public void onSuccess(String result) {
                Map<String,String> map =  FastJsonUtils.stringToCollect(result);
                boolean isSuccess = Boolean.parseBoolean(map.get("isSuccess"));
                String message = map.get("message");
                if(isSuccess){
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    jumpToLogin();
                }else{
                    Toast toastCenter = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
                    toastCenter.setGravity(Gravity.CENTER,0,0);
                    toastCenter.show();
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "network failure", Toast.LENGTH_SHORT).show();
            }
        },hashMap,this);
    }

    private void onClickToLogin(View view){
        jumpToLogin();
    }

    //跳转方法

    private void jumpToLogin(){
        Intent intent = null;
        //setContentView(R.string.login_flag);
        intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}