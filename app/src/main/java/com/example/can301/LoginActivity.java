package com.example.can301;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
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

public class LoginActivity extends AppCompatActivity implements View.OnFocusChangeListener {
    private Button lBtn;
    private EditText inputEmail;
    private EditText inputPassword;
    private String status = String.valueOf('a');
    // notifications
    private String success = "Login success.";
    private String fail = "Login failed";
    private String l2durl = "file:///android_asset/www/l2d.html";
    private TextView rBtn, title;
    private WebView mWebview;
    private boolean loginFlag;
    private ScrollView scrollView;
    private View finalShow;
    private int noKeyBoardHeight;
    private Rect rect = new Rect();
    private int[] location = new int[2];
    private Handler handler = new Handler(Looper.getMainLooper());
    private String backendUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = getSharedPreferences("config", Context.MODE_PRIVATE);
        if (sharedPref.getBoolean("isLoggedIn", false)) {
            jumpToMain();
        }
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_login);
        Resources res = getResources();
        backendUrl = (String) res.getText(R.string.remoteBaseUrl);
        // find element
        lBtn = findViewById(R.id.btn_login);
        rBtn = findViewById(R.id.regLink);
        mWebview = findViewById(R.id.l2d);
        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        title = findViewById(R.id.caption1);
        scrollView = findViewById(R.id.out_est);
        finalShow = lBtn;
        this.loadL2d(l2durl);


        lBtn.setOnClickListener(this::onClick);
        rBtn.setOnClickListener(this::onClickRegisterLink);


        // try to prevent softkeyboard from hide button
        inputEmail.setOnFocusChangeListener(this);
        inputPassword.setOnFocusChangeListener(this);
        scrollView.getWindowVisibleDisplayFrame(rect);
        noKeyBoardHeight = rect.bottom;
    }

    @Override

    protected void onStart() {

        super.onStart();
        final int[] times = {1};
        ViewTreeObserver observer = scrollView.getViewTreeObserver();

        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override

            public void onGlobalLayout() {
                if (times[0] == 1) {
//                    Log.d(TAG, "test if once only");
                    rBtn.getLocationOnScreen(location);
                    times[0]++;
                }

            }

        });


    }

    private void loadL2d(String url) {
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.getSettings().setAllowFileAccess(true);
        mWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebview.getSettings().setDomStorageEnabled(true);
        mWebview.getSettings().setDefaultTextEncodingName("utf-8");
        mWebview.loadUrl(url);
    }

    private boolean checkEditText(EditText editText) {
        String toCheck = editText.getText().toString();
        int length = toCheck.length();
        if (length < 6) {
            Toast.makeText(this, "Ensure password longer than 6", Toast.LENGTH_SHORT).show();
            return false;
        }
        boolean containNum = false;
        boolean containLetter = false;
        for (int i = 0; i < length; i++) {
            if (Character.isDigit(toCheck.charAt(i))) {
                containNum = true;
            } else {
                containLetter = true;
            }
            if (containNum && containLetter) {
                return true;
            }
        }
        if (!(containNum && containLetter)) {
            Toast.makeText(this, "Ensure password contains both letter and word", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void onClick(View view) {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        // System.out.println(validate(email));
       /* if(!validate(email)){
            Toast.makeText(getApplicationContext(), "incorrect email!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!checkEditText(inputPassword)){
            return;
        }*/
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("email", "zihan.lyu18@studet.xjtlu.edu.cn");
        hashMap.put("password", "123456a");
        //Log.d(TAG, "Before: " + hashMap);
        OkHttpUtils.getSoleInstance().doPostForm(backendUrl + "/user/login", new NetAgent() {
            //OkHttpUtils.getSoleInstance().doPostForm("http://10.0.2.2:8080/user/login", new NetAgent() {
            @Override
            public void onSuccess(String result) {
                Map<String, String> map = FastJsonUtils.stringToCollect(result);
                String isSuccess = map.get("isSuccess");
                String message = map.get("message");
                //System.out.println();
                try {
                    if (isSuccess.equals("200")) {

                        // save email for profile
                        onSave();

                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        SharedPreferences sharedPref = getSharedPreferences("config", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putBoolean(String.valueOf("isLoggedIn"), true);
                        editor.putString(String.valueOf(R.string.checkEmail), email);
                        editor.apply();
                        onSave();
                        jumpToMain();
                    } else {
                        Toast toastCenter = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
                        toastCenter.setGravity(Gravity.CENTER, 0, 0);
                        toastCenter.show();
                    }
                } catch (Exception e) {
                    Toast toastCenter = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
                    toastCenter.setGravity(Gravity.CENTER, 0, 0);
                    toastCenter.show();
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                Toast center = Toast.makeText(getApplicationContext(), "network failure", Toast.LENGTH_SHORT);
                center.setGravity(Gravity.CENTER, 0, 0);
                Log.d(TAG, "Beforeafter: ");
                center.show();
            }
        }, hashMap, this);

    }

    private void onClickRegisterLink(View view) {
        jumpToRegister();
    }

    //跳转方法
    private void jumpToMain() {
        Intent intent = null;
        //setContentView(R.string.login_flag);

        intent = new Intent(this, mainTestActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void jumpToRegister() {
        Intent intent = null;
        //setContentView(R.string.login_flag);
        intent = new Intent(this, RegisterActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        Log.d(TAG, "onFocusChange: " + hasFocus);
        if (hasFocus) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    scrollView.getWindowVisibleDisplayFrame(rect);
                    int current = rect.bottom;
                    while (current == noKeyBoardHeight) {
                        scrollView.getWindowVisibleDisplayFrame(rect);
                        current = rect.bottom;
                    }
//                        in case hide half
                    int safeMargin = 40;
                    int scrollHeight = (location[1] + safeMargin + rBtn.getHeight()) - current;
//                        Log.d(TAG, "run: "+location[1]);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.scrollTo(0, (int) scrollHeight);
                        }
                    });

                }
            }).start();

//                Log.d(TAG, "onFocusrb1"+b1);


        }

    }


    // saving info in shared preferences file for displaying it on the profile

    public void onSave() {
        // retrieve info from email edit field
        String email = inputEmail.getText().toString();

        // shared preferences save info
        SharedPreferences mypref = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = mypref.edit();
        editor.putString("keyemail", email);
        editor.commit();

    }
}
