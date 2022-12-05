package com.example.can301.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.can301.LoginActivity;
import com.example.can301.R;
import com.example.can301.net.NetAgent;
import com.example.can301.net.OkHttpUtils;
import com.example.can301.utilities.FastJsonUtils;

import java.util.HashMap;
import java.util.Map;

public class profileFragment extends Fragment {

    private TextView emailTV, cashTV, timeTV, giftTV;
    private EditText nicknameTV;
    private Button logOut;
    private View root;
    private String id;
    private ImageButton editName;
    private String nickname;

    private int NumberOfGift;
    private int[] GiftStatus;
    private String backendUrl = "http://47.94.44.163:8080";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_profile,container,false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVariable();
        readEmail();
        getCash();
    }

    //    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        initVariable();
//    }

    private void initVariable(){
        emailTV = (TextView) root.findViewById(R.id.emailTV);
        nicknameTV = (EditText) root.findViewById(R.id.nicknameTV);
        cashTV = (TextView) root.findViewById(R.id.cashTV);
        giftTV = (TextView) root.findViewById(R.id.dayTV);
        logOut = getActivity().findViewById(R.id.btn_log_out);
        editName = root.findViewById(R.id.editname);
        logOut.setOnClickListener(this::onClick);
        editName.setOnClickListener(this::onEditname);
        timeTV = (TextView) root.findViewById(R.id.timeTV);
        readID();
        GetGiftNumber();
        bindNickname();
    }

    private void bindNickname(){
        SharedPreferences mypref = root.getContext().getSharedPreferences("info", root.getContext().MODE_PRIVATE);
        nickname = mypref.getString("nickname", "DefaultName");
        nicknameTV.setText(nickname);

    }

    private void onEditname(View view){
        nickname = nicknameTV.getText().toString();
        SharedPreferences mypref = root.getContext().getSharedPreferences("info", root.getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = mypref.edit();
        editor.putString("nickname", nickname);
        editor.apply();
        nicknameTV.setText(nickname);
    }

    private void readID(){
        SharedPreferences mypref = root.getContext().getSharedPreferences("config", root.getContext().MODE_PRIVATE);
        id = mypref.getString("id", "1");
    }

    private void readEmail(){
        SharedPreferences mypref = root.getContext().getSharedPreferences("login", root.getContext().MODE_PRIVATE);
        String email = mypref.getString("email", "User Email");
        emailTV.setText(email);
        SharedPreferences countpref = root.getContext().getSharedPreferences("tab", root.getContext().MODE_PRIVATE);
        int checkCount = countpref.getInt("checkCount", 0);
        timeTV.setText(" " + String.valueOf(checkCount));
    }

    private void getCash(){
        HashMap hashMap = new HashMap();
        hashMap.put("id",id);
        //System.out.println(getActivity());
        OkHttpUtils.getSoleInstance().doPostForm(backendUrl + "/user/querycash/", new NetAgent() {
            @Override
            public void onSuccess(String result) {
                Map<String, String> map = FastJsonUtils.stringToCollect(result);
                String cash = String.valueOf(map.get("cash"));
                if (String.valueOf(map.get("status")).equals("200")) {
                    Toast.makeText(getActivity().getApplicationContext(), "get cash", Toast.LENGTH_SHORT).show();
                    cashTV.setText(" " + cash);
                } else {
                    Toast toastCenter = Toast.makeText(getActivity().getApplicationContext(), "no", Toast.LENGTH_SHORT);
                    toastCenter.setGravity(Gravity.CENTER, 0, 0);
                    toastCenter.show();
                }
            }
                @Override
                public void onError(Exception e) {
                    if (isAdded()) {
                        e.printStackTrace();
                        Toast center = Toast.makeText(getActivity().getApplicationContext(), "network failure", Toast.LENGTH_SHORT);
                        center.setGravity(Gravity.CENTER, 0, 0);
                        center.show();
                    }
                }
            },hashMap,getActivity());
        }

    private void GetGiftNumber(){

        HashMap hashMap = new HashMap();
        hashMap.put("id",id);
        OkHttpUtils.getSoleInstance().doPostForm(backendUrl + "/user/getGift", new NetAgent() {
            @Override
            public void onSuccess(String result) {
                Map<String, String> map = FastJsonUtils.stringToCollect(result);
                String status = String.valueOf(map.get("status"));
                String a = String.valueOf(map.get("giftList"));
                if (status.equals("0")){
                    giftTV.setText(" " + 0);
                    return;
                }
                Object[] b = FastJsonUtils.toArray(a);
                int[] ss = new int[b.length];
                for (int i = 0; i < ss.length; i++) {
                    ss[i] = Integer.parseInt(b[i].toString());
                }
                GiftStatus = ss;
                NumberOfGift = GiftStatus.length;
                // System.out.println(seatStatus);
                if (status.equals("200")) {
                    giftTV.setText(" " + NumberOfGift);
                    //Toast.makeText(getApplicationContext(), "Updated seat info", Toast.LENGTH_SHORT).show();
                } else {
                }
                int bias=0;
            }
            @Override
            public void onError(Exception e) {
                if (isAdded()) {
                    e.printStackTrace();
                    Toast center = Toast.makeText(getActivity().getApplicationContext(), "network failure", Toast.LENGTH_SHORT);
                    center.setGravity(Gravity.CENTER, 0, 0);
                    center.show();
                }
            }

        },hashMap,getActivity());

    }


    private void onClick(View view) {
        AlertDialog dialog = new AlertDialog.Builder(view.getContext())
                .setIcon(R.mipmap.emblem)//set the image
                .setTitle("Confirmation")//set the title of dialogue
                .setMessage("Master! You really want to leave me T_T?")//set the content
                //set buttons
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isLoggedIn",false);
                        editor.putString("email",null);
                        editor.apply();
                        Intent intent = new Intent(requireContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        dialog.dismiss();
                        startActivity(intent);
                    }
                }).create();
        dialog.show();
    }
}
