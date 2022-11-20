package com.example.can301;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class TableActivity extends Activity {
    private int startIndex;
    private int seatNumber;
    private String type;
    private TextView testTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_table);
        testTV = findViewById(R.id.testView);
        Intent intent = getIntent();
        Bundle infoBundle = intent.getExtras();
        // 在res中获取
            Resources res =getResources();
            String[] typeArray= res.getStringArray(R.array.tableType);
        startIndex = Integer.parseInt(
                infoBundle.getString("startIndex"));
        seatNumber = Integer.parseInt(
                infoBundle.getString("seatNumber"));;
        type = typeArray[Integer.parseInt(infoBundle.getString("type"))];
        testTV.setText(type + "," + startIndex + ", " + seatNumber);


    }
}