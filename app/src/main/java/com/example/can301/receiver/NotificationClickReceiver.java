package com.example.can301.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.can301.mainTestActivity;

public class NotificationClickReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent newIntent = new Intent(context, mainTestActivity.class);
        //跳转到主界面后，并将栈底的Activity全部都销毁
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //向MainActivity中传入这个值
        Bundle bundle = new Bundle();
        bundle.putBoolean("toNoise",true);
        intent.putExtras(bundle);
        context.startActivity(intent);

    }
}