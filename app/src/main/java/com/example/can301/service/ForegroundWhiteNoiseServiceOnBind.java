package com.example.can301.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.can301.R;
import com.example.can301.adapter.NoiseListAdapter;
import com.example.can301.entity.NoiseItem;
import com.example.can301.receiver.NotificationClickReceiver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ForegroundWhiteNoiseServiceOnBind extends Service implements MediaPlayer.OnCompletionListener {


    private final WhiteNoiseBinder binder = new WhiteNoiseBinder();
//    to save system resource, i will not instantiate all the mediaPlayer.
    private MediaPlayer singleMediaPlayer;
    private static final int NOTIFICATION_DOWNLOAD_PROGRESS_ID = 0x0001;

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

    public class WhiteNoiseBinder extends Binder {
        public ForegroundWhiteNoiseServiceOnBind getService(){
            return ForegroundWhiteNoiseServiceOnBind.this;
        }
    }
    public ForegroundWhiteNoiseServiceOnBind() {
        super();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(ForegroundWhiteNoiseServiceOnBind.this.getClass().getName(), "onCreate: ");
    }
//        int[] mediaResources = NoiseItem.getMediaResourceArray();
//        for (int mediaResource : mediaResources) {
//            MediaPlayer mediaPlayer = MediaPlayer.create(this, mediaResource);
////            mediaPlayer.setOnCompletionListener(this);
//            mediaPlayer.setLooping(true);
//            mediaPlayerList.add(mediaPlayer);
//        }

    public void switchNoise(View view,ImageButton lastOne){
        if(singleMediaPlayer.isPlaying()){
            pauseNoise(view,lastOne);
        }else {
            continueNoise(view,lastOne);
        }
    }

    public void startNewNoise(NoiseItem noiseItem,View view, ImageButton lastOne){
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (lastOne == null){
                        stopForeground(false);
                        singleMediaPlayer = MediaPlayer.create(getApplicationContext(),noiseItem.mediaResource);
                        singleMediaPlayer.setLooping(true);
                        singleMediaPlayer.start();
                        ((ImageButton)view).setBackgroundResource(R.mipmap.icons8_pause_100);
                        NoiseListAdapter.setView((ImageButton) view);
                        createNotification(noiseItem);
                    }else {
                        stopForeground(false);
                        singleMediaPlayer.stop();
                        singleMediaPlayer = MediaPlayer.create(getApplicationContext(),noiseItem.mediaResource);
                        singleMediaPlayer.setLooping(true);
                        singleMediaPlayer.start();
                        ((ImageButton)view).setBackgroundResource(R.mipmap.icons8_pause_100);
                        lastOne.setBackgroundResource(R.mipmap.icons8_play_100);
                        NoiseListAdapter.setView((ImageButton) view);
                        createNotification(noiseItem);
                    }
                }
            }).start();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void pauseNoise(View view,ImageButton lastOne){
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    singleMediaPlayer.pause();
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        public void run() {
                            ((ImageButton)view).setBackgroundResource(R.mipmap.icons8_play_100);
                            NoiseListAdapter.setNull();
                            stopForeground(true);
                        }
                    });
                }
            }).start();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void continueNoise(View view,ImageButton lastOne){
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    singleMediaPlayer.start();
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        public void run() {
                            ((ImageButton)view).setBackgroundResource(R.mipmap.icons8_pause_100);
                        }
                    });
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(singleMediaPlayer!=null){
            if (singleMediaPlayer.isPlaying()) {
                singleMediaPlayer.stop();
            }
            singleMediaPlayer.release();

        }
        stopForeground(true);
        Log.d(ForegroundWhiteNoiseServiceOnBind.this.getClass().getName(), "onDestroy: ");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if(singleMediaPlayer!=null){
            if (singleMediaPlayer.isPlaying()) {
                singleMediaPlayer.stop();
            }
            singleMediaPlayer.release();
        }
        stopForeground(true);
        return super.onUnbind(intent);
    }

    public void createNotification(NoiseItem noiseItem){
        String CHANNEL_ONE_ID = "com.primedu.cn";
        String CHANNEL_ONE_NAME = "Channel One";
        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ONE_ID,
                    CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(notificationChannel);
        }
        //使用兼容版本
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
        //设置状态栏的通知图标
        builder.setSmallIcon(R.mipmap.icons8_pause_50);
        //设置通知栏横条的图标
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),noiseItem.image));
        //禁止用户点击删除按钮删除
        builder.setAutoCancel(true);
        //禁止滑动删除
        builder.setOngoing(true);
        //右上角的时间显示
        builder.setShowWhen(true);
        //设置通知栏的标题内容
        builder.setContentTitle("The music \""+noiseItem.title+"\" is Playing");
        builder.setChannelId(CHANNEL_ONE_ID);
        Intent intent =new Intent (this, NotificationClickReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        //创建通知
        Notification notification = builder.build();
        //设置为前台服务
        startForeground(NOTIFICATION_DOWNLOAD_PROGRESS_ID,notification);
    }

}
