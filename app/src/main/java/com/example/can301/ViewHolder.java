package com.example.can301;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends  RecyclerView.ViewHolder{
    TextView itemnameTV;
    ImageView itempicIV;
    TextView priceTV;


    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        itempicIV = itemView.findViewById(R.id.itempicIV);
        itemnameTV = itemView.findViewById(R.id.itemnameTV);
        priceTV = itemView.findViewById(R.id.priceTV);

    }

    public void onBind(DataItem data){
        itemnameTV.setText(data.getTitle());
        itempicIV.setImageResource(data.getImage());
        priceTV.setText(Integer.toString(data.getPrice()));
    }
}

