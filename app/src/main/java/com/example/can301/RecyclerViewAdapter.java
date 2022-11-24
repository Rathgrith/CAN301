package com.example.can301;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.can301.customizedClass.DataItem;
import com.example.can301.customizedClass.ViewHolder;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // list for adapter
    private ArrayList<DataItem> listData = new ArrayList<>();
    int selectedPrice;
    private LocalBroadcastManager localBroadcastManager;
    private XXListener mXXListener;
    private static String b = "0";

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_itemexchange, parent, false);
        return new ViewHolder(view);
    }

    public void setOnXXClickListener (XXListener  XXListener) {
        this.mXXListener = XXListener;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ((ViewHolder)holder).onBind(listData.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Please Confirm");
                builder.setMessage("You want to purchase this item?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //"YES" Button Click
                        Toast.makeText(view.getContext(), "YES Button Click", Toast.LENGTH_LONG).show();
                        selectedPrice = listData.get(position).getPrice();
//                        System.out.println("------------Selected Price------------");
//                        System.out.println(selectedPrice);
                        //Save Selected Price in SharedPreferences Im not so sure:(
                        SharedPreferences mypref = view.getContext().getSharedPreferences("price", view.getContext().MODE_PRIVATE);
                        SharedPreferences.Editor editor = mypref.edit();
                        editor.putString("selectedPrice", String.valueOf(selectedPrice));

                        Bundle bundle = new Bundle();
                        bundle.putString("selectedPrice", mypref.getString("selectedPrice", ""));
                        editor.commit();
                        b = mypref.getString("selectedPrice", "");
                        mXXListener.onXXClick(Integer.parseInt(b));
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //"NO" Button Click
                        Toast.makeText(view.getContext(), "NO Button Click", Toast.LENGTH_LONG).show();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    public interface XXListener {
        public void onXXClick(int b);
    }



    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void addItem(DataItem data) {
        // get item from database
        listData.add(data);
    }
}
