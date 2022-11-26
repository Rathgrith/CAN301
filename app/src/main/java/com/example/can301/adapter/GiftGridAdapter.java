package com.example.can301.adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.can301.LoginActivity;
import com.example.can301.R;
import com.example.can301.entity.GiftItem;

import java.util.List;

public class GiftGridAdapter extends BaseAdapter {
    private Context myContext;
    private List<GiftItem> list;

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
                                    SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean("isLoggedIn",false);
                                    editor.putString("email",null);
                                    editor.apply();
                                    Intent intent = new Intent(view.getContext(), LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                    dialog.dismiss();
                                    view.getContext().startActivity(intent);
                                }
                            }).create();
                    dialog.show();
                }

            }
        });
        return convertView;
    }

    public static final class ViewHolder{
        public ImageView giftImage;
        public TextView giftName;
        public TextView giftPrice;
        public Button purchaseButton;
    }

}
