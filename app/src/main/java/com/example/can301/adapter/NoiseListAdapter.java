package com.example.can301.adapter;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.can301.R;
import com.example.can301.entity.NoiseItem;
import com.example.can301.fragments.whiteNoiseFragment;
import com.example.can301.service.ForegroundWhiteNoiseServiceOnBind;

import java.util.List;

public class NoiseListAdapter extends BaseAdapter {
    private Context myContext;
    private List<NoiseItem> list;
    @SuppressLint("StaticFieldLeak")
    private static ImageButton musicPlaying;
//    private ForegroundWhiteNoiseServiceOnBind foregroundWhiteNoiseServiceOnBind;
    private boolean hasBind = false;


    public NoiseListAdapter(Context context, List<NoiseItem> list) {
        myContext = context;
        this.list = list;
//        foregroundWhiteNoiseServiceOnBind = (ForegroundWhiteNoiseServiceOnBind) service;
//        Log.d("NoiseListAdapterccc", "onClick: "+foregroundWhiteNoiseServiceOnBind+"service"+service);
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = new ViewHolder();
        if(convertView==null){
            convertView = LayoutInflater.from(myContext).inflate(R.layout.white_noise_item,null);
            viewHolder.noiseImage = convertView.findViewById(R.id.Noise_image);
            viewHolder.description = convertView.findViewById(R.id.description);
            viewHolder.title = convertView.findViewById(R.id.title);
            viewHolder.play_pause = convertView.findViewById(R.id.play_pause);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (NoiseListAdapter.ViewHolder) convertView.getTag();
        }
        NoiseItem noiseItem = list.get(i);
        viewHolder.noiseImage.setImageResource(noiseItem.image);
        viewHolder.description.setText(noiseItem.description);
        viewHolder.title.setText(noiseItem.title);

        viewHolder.play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(musicPlaying==null){
//                    Log.d("", "onClick:"+ whiteNoiseFragment.foregroundWhiteNoiseServiceOnBind);
                    whiteNoiseFragment.foregroundWhiteNoiseServiceOnBind.startNewNoise(noiseItem,view, (ImageButton) musicPlaying);
                    musicPlaying = (ImageButton) view;

                }else if (musicPlaying==view){
                    whiteNoiseFragment.foregroundWhiteNoiseServiceOnBind.pauseNoise(view,musicPlaying);
                }else {
                    whiteNoiseFragment.foregroundWhiteNoiseServiceOnBind.startNewNoise(noiseItem,view,musicPlaying);
                    musicPlaying = (ImageButton) view;
                }

            }
        });
        return convertView;
    }
    public static void setNull(){
        Log.d("", "setNull1: "+musicPlaying);
        musicPlaying = null;
        Log.d("", "setNull2: "+musicPlaying);
    }
    public static void setView(ImageButton view){
        musicPlaying = view;
    }

    public static final class ViewHolder{
        public ImageView noiseImage;
        public TextView title;
        public TextView description;
        public ImageButton play_pause;
    }



}
