package com.example.can301.adapter;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.can301.LoginActivity;
import com.example.can301.R;
import com.example.can301.entity.GiftItem;
import com.example.can301.net.NetAgent;
import com.example.can301.net.OkHttpUtils;
import com.example.can301.utilities.FastJsonUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GiftGridAdapter extends BaseAdapter {
    private Context myContext;
    private String backendUrl = "http://47.94.44.163:8080";
    private String id;
    private List<GiftItem> list;
    private TextView Cash;
    private View root;

    public GiftGridAdapter(Context myContext, List<GiftItem> list) {
        this.myContext = myContext;
        this.list = list;
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
        ViewHolder holder = new ViewHolder();
        if(convertView == null){
            Log.d(TAG, "getView: ifde");
            convertView = LayoutInflater.from(myContext).inflate(R.layout.gift_item,null);
            SharedPreferences mypref = myContext.getSharedPreferences("config", myContext.MODE_PRIVATE);
            id = mypref.getString("id", "1");
            TextView Cash = convertView.findViewById(R.id.cashTV);
            holder.giftImage = convertView.findViewById(R.id.giftImage);
            holder.giftName = convertView.findViewById(R.id.giftName);
            holder.giftPrice= convertView.findViewById(R.id.giftPrice);
            holder.purchaseButton = convertView.findViewById(R.id.btn_purchase);
            convertView.setTag(holder);
        }else {
            Log.d(TAG, "getView:elsede ");
            holder = (ViewHolder) convertView.getTag();

        }
        GiftItem giftItem= list.get(i);
        int giftID = i+1;
        holder.giftImage.setImageResource(giftItem.imageInDrawable);
        holder.giftName.setText(giftItem.name);
        holder.giftPrice.setText(String.valueOf(giftItem.price));
        holder.purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(false){
                    // may be we can use sharedpreference to compare the price of the item and the money user hold
                }else {
                    AlertDialog dialog = new AlertDialog.Builder(view.getContext())
                            .setIcon(giftItem.imageInDrawable)//set the image
                            .setTitle("Confirmation")//set the title of dialogue
                            .setMessage("Master, it will cost you "+giftItem.price+"!\n\nYou really wanna buy it?")//set the content
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
//                              //because that the mechanism of credits has not been set， so currently just use log out to show it can work
                                    int price = (giftItem.price);
                                    //int cash = Integer.parseInt(String.valueOf(Cash.getText()));
                                    //System.out.println(cash);
                                    try{
                                        purchase(String.valueOf(price));
                                        buy(giftID);
                                    }
                                    catch (Exception e){

                                    }
                                }
                            }).create();
                    dialog.show();
                }

            }
        });
        return convertView;
    }
    private void purchase(String price){
        HashMap hashMap = new HashMap();
        hashMap.put("id",id);
        hashMap.put("price",price);
        //System.out.println(getActivity());
        OkHttpUtils.getSoleInstance().doPostForm(backendUrl + "/user/purchase/", new NetAgent() {
            @Override
            public void onSuccess(String result) {
                Map<String, String> map = FastJsonUtils.stringToCollect(result);
                if (map.get("status").equals("200")) {
                    Toast.makeText(myContext.getApplicationContext(), "老板大气", Toast.LENGTH_SHORT).show();
                    // 暂时没想到怎么刷新
                    Intent intent = new Intent(myContext, LoginActivity.class);
                    myContext.startActivity(intent);
                } else if (map.get("status").equals("400")) {
                    Toast.makeText(myContext.getApplicationContext(), "你买不起", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(myContext.getApplicationContext(), map.get("status"), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                Toast center = Toast.makeText(myContext.getApplicationContext(), "network failure", Toast.LENGTH_SHORT);
                center.setGravity(Gravity.CENTER, 0, 0);
                center.show();
            }
        },hashMap, (Activity) myContext);
    }

    private void buy(int giftID){
        HashMap hashMap = new HashMap();
        hashMap.put("id",id);
        hashMap.put("giftid", String.valueOf(giftID));
        //System.out.println(getActivity());
        OkHttpUtils.getSoleInstance().doPostForm(backendUrl + "/user/buygift/", new NetAgent() {
            @Override
            public void onSuccess(String result) {
                Map<String, String> map = FastJsonUtils.stringToCollect(result);
                if (map.get("status").equals("200")) {
                    // Toast.makeText(myContext.getApplicationContext(), map.get("status"), Toast.LENGTH_SHORT).show();
                } else if (map.get("status").equals("400")) {
                    // Toast.makeText(myContext.getApplicationContext(), "succ", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(myContext.getApplicationContext(), map.get("status"), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                Toast center = Toast.makeText(myContext.getApplicationContext(), "network failure", Toast.LENGTH_SHORT);
                center.setGravity(Gravity.CENTER, 0, 0);
                center.show();
            }
        },hashMap, (Activity) myContext);
    }


    public static final class ViewHolder{
        public ImageView giftImage;
        public TextView giftName;
        public TextView giftPrice;
        public Button purchaseButton;
    }

}
