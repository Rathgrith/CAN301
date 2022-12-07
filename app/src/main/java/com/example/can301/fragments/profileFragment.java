package com.example.can301.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class profileFragment extends Fragment {

    private TextView emailTV, cashTV, timeTV, giftTV;
    public ImageView avatar;
    private EditText nicknameTV;
    private Button logOut;
    private View root;
    private String id;
    private ImageButton editName, showGift;
    private String nickname;

    private int[] avatarArray;

    private int NumberOfGift;
    private int[] GiftStatus;
    private ImageButton changeAvatar;
//    beacuse of the id setting in backend, use -1 to occupy the index 0.
    private int[] giftImageArray = {-1,R.drawable.xjtlu_bear,R.drawable.xjtlu_bird,R.drawable.xjtlu_cap, R.drawable.boder_music_text,R.drawable.xjtlu_dress,R.drawable.xjtlu_bags};
    int avatar_index;
    private List<Map<String, Object>> giftList = new ArrayList<>();
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
        avatar = root.findViewById(R.id.profileIV);
        emailTV = (TextView) root.findViewById(R.id.emailTV);
        nicknameTV = (EditText) root.findViewById(R.id.nicknameTV);
        cashTV = (TextView) root.findViewById(R.id.cashTV);
        giftTV = (TextView) root.findViewById(R.id.dayTV);
        logOut = getActivity().findViewById(R.id.btn_log_out);
        editName = root.findViewById(R.id.editname);
        logOut.setOnClickListener(this::onClick);
        editName.setOnClickListener(this::onEditname);
        timeTV = (TextView) root.findViewById(R.id.timeTV);
        showGift = root.findViewById(R.id.showGift);
        avatarArray = new int[]{R.drawable.man1, R.drawable.man2, R.drawable.woman1, R.drawable.woman2};
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
        avatar_index = sharedPref.getInt("avatar", 0);
        avatar.setImageResource(avatarArray[avatar_index]);
        changeAvatar = root.findViewById(R.id.change_avatar);
        changeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                avatar_index+=1;
                if (avatar_index>3){
                    avatar_index = 0;
                }
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("avatar",avatar_index);
                editor.apply();
                avatar.setImageResource(avatarArray[avatar_index]);
            }
        });
        showGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(requireActivity());
                builder.setTitle("Detail Information!");
                builder.setIcon(R.mipmap.icons8_gift_on);
                SimpleAdapter adapter=new SimpleAdapter(
                        requireContext(),
                        giftList,
                        R.layout.profile_gift_item,
                        new String[]{"image","text"},
                new int[]{R.id.profile_gift,R.id.numbersText});
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
            });
                builder.create().show();

        }
        });

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
                    // Toast.makeText(getActivity().getApplicationContext(), "get cash", Toast.LENGTH_SHORT).show();
                    cashTV.setText(" " + cash);
                } else {
                    Toast toastCenter = Toast.makeText(getActivity().getApplicationContext(), "no", Toast.LENGTH_SHORT);
//                    toastCenter.setGravity(Gravity.CENTER, 0, 0);
                    toastCenter.show();
                }
            }
                @Override
                public void onError(Exception e) {
                    if (isAdded()) {
                        e.printStackTrace();
                        Toast center = Toast.makeText(getActivity().getApplicationContext(), "network failure", Toast.LENGTH_SHORT);
//                        center.setGravity(Gravity.CENTER, 0, 0);
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
//                for(Integer x:GiftStatus){
//                    Log.d("profile fragment", "profile: "+x);
//                }

                Map<Integer,Integer> itemAndNumber= new HashMap<>();
                for(Integer x:ss){
                    if(itemAndNumber.containsKey(x)){
                        itemAndNumber.put(x,itemAndNumber.get(x)+1);
                    }else {
                        itemAndNumber.put(x,1);
                    }
                }
//                Log.d("map", "profile: "+itemAndNumber);
                for(Integer x:itemAndNumber.keySet()){
                    Map<String,Object> temMap = new HashMap<>();
                    temMap.put("image",giftImageArray[x]);
                    temMap.put("text","You can change "+itemAndNumber.get(x)+" items!");
                    giftList.add(temMap);
                }
                Log.d("map", "profile: "+giftList);
                NumberOfGift = GiftStatus.length;
                // System.out.println(seatStatus);
                if (status.equals("200")) {
                    if(NumberOfGift == 0){
                        giftTV.setText("You could buy some gift in gift store °˖✧◝(⁰▿⁰)◜✧˖°");
                    }{
                        giftTV.setText(String.format("Gift amount: %d!!", NumberOfGift));
                    }

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
