package com.example.can301.customizedClass;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.can301.R;

public class ViewHolder extends  RecyclerView.ViewHolder{
    TextView itemNameTV;
    ImageView itemPicIV;
    TextView priceTV;


    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        itemPicIV = itemView.findViewById(R.id.itempicIV);
        itemNameTV = itemView.findViewById(R.id.itemnameTV);
        priceTV = itemView.findViewById(R.id.priceTV);

    }

    public void onBind(DataItem data){
        itemNameTV.setText(data.getTitle());
        itemPicIV.setImageResource(data.getImage());
        priceTV.setText(Integer.toString(data.getPrice()));
    }
}

