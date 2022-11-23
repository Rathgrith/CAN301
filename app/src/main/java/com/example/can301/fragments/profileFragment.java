package com.example.can301.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.can301.LoginActivity;
import com.example.can301.R;

public class profileFragment extends Fragment {

    private TextView emailTV, nicknameTV, cashTV, timeTV, dayTV;
    private Button logOut;
    private View root;
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
    }

    //    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        initVariable();
//    }

    private void initVariable(){
        emailTV = (TextView) getActivity().findViewById(R.id.emailTV);
        nicknameTV = (TextView) getActivity().findViewById(R.id.nicknameTV);
        logOut = getActivity().findViewById(R.id.btn_log_out);
        logOut.setOnClickListener(this::onClick);

    }

    public void readEmail(){
        SharedPreferences mypref = root.getContext().getSharedPreferences("login", root.getContext().MODE_PRIVATE);
        String email = mypref.getString("keyemail", "User Email");
        emailTV.setText(email);
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
                        // 这两行有bug，问题sharedpreference没有被改变，于是回到login之后，还会跳转到main
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
