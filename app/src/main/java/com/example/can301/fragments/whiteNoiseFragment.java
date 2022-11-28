package com.example.can301.fragments;

import static android.content.ContentValues.TAG;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.can301.R;
import com.example.can301.adapter.GiftGridAdapter;
import com.example.can301.adapter.NoiseListAdapter;
import com.example.can301.entity.GiftItem;
import com.example.can301.entity.NoiseItem;
import com.example.can301.service.ForegroundWhiteNoiseServiceOnBind;

import java.util.List;
import java.util.Objects;

public class whiteNoiseFragment extends Fragment {

    private ListView listView;
    private NoiseListAdapter adapter;
    public static ForegroundWhiteNoiseServiceOnBind foregroundWhiteNoiseServiceOnBind;

    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            ForegroundWhiteNoiseServiceOnBind.WhiteNoiseBinder binder = (ForegroundWhiteNoiseServiceOnBind.WhiteNoiseBinder)iBinder;
            Log.d("listAdapter", "onClick: service"+foregroundWhiteNoiseServiceOnBind);
            whiteNoiseFragment.foregroundWhiteNoiseServiceOnBind = binder.getService();
            Log.d("listAdapter", "onClick:service "+whiteNoiseFragment.foregroundWhiteNoiseServiceOnBind);

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d("listAdapter", "onServiceDisconnected:enter ");
            foregroundWhiteNoiseServiceOnBind = null;
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_white_noise, container, false);

        init(root);
        return root;
    }
    private void init(View root){
        listView = (ListView) root.findViewById(R.id.noise_list_layout);
        List<NoiseItem> list = NoiseItem.getDefaultList();
        Intent intent = new Intent(getActivity(), ForegroundWhiteNoiseServiceOnBind.class);
        requireActivity().startService(intent);
        requireActivity().bindService(intent,connection, Service.BIND_AUTO_CREATE);

        adapter = new NoiseListAdapter(getContext(),list);
        listView.setAdapter(adapter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(getActivity(), ForegroundWhiteNoiseServiceOnBind.class);
        requireActivity().stopService(intent);
//        requireActivity().unbindService(connection);
    }
}
