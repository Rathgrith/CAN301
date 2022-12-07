package com.example.can301.entity;

import android.media.Image;

import com.example.can301.R;

import java.util.ArrayList;
import java.util.List;

public class GiftItem {
//    this image is int because we currently decide to get the image from drawable instead of backend
    public int imageInDrawable = 0;
    public Image imageFromBack = null;
    public String name;
    public int price;

    public GiftItem(int image, String name, int price) {
        this.imageInDrawable = image;
        this.name = name;
        this.price = price;
    }


    public static int[] imageArray = {R.drawable.xjtlu_bear,R.drawable.xjtlu_bird,R.drawable.xjtlu_cap,R.drawable.xjtlu_t_shirt,R.drawable.xjtlu_dress,R.drawable.xjtlu_bags};
    public static String[] nameArray = {"XJTLU Bear","XJTLU Bird","XJTLU Cap","XJTLU T-shrit","XJTLU dress","XJTLU bags"};
    public static int[] priceArray = {1000,1500,2000,1800,2500,500};


    public static List<GiftItem> getDefaultList(){
        List<GiftItem> list = new ArrayList<>();
        for (int i = 0; i < imageArray.length; i++) {
            list.add(new GiftItem(imageArray[i],nameArray[i],priceArray[i]));
        }
        return list;
    }
}
