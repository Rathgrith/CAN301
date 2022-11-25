package com.example.can301;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QRcode extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        IntentIntegrator intentIntegrator = new IntentIntegrator(QRcode.this);
        // start scanning
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // get result

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Failure", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Successfully" , Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        //identify the id of activity(qr code store the id)
        if(result.getContents().equals("abcd")){
            Intent intent = new Intent(QRcode.this,TableActivity.class);
            startActivity(intent);
        }
    }
}