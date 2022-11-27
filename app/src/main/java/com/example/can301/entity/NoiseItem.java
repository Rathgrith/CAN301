package com.example.can301.entity;


import com.example.can301.R;

import java.util.ArrayList;
import java.util.List;

public class NoiseItem {
    public int image;
    public String title;
    public String description;
    public int mediaResource;

    public NoiseItem(int image, String title, String description, int mediaRes) {
        this.image = image;
        this.title = title;
        this.description = description;
        this.mediaResource = mediaRes;
    }

    public static int[] mediaResources = {R.raw.bonfire_in_the_forest,R.raw.rain,R.raw.thunderstorm,R.raw.wave,R.raw.wind_and_rain};
    public static int[] imageArray = {R.mipmap.fire_in_forest,R.mipmap.rain,R.mipmap.thunde_rain,R.mipmap.wave,R.mipmap.wind};
    public static String[] titleArray = {"Bonfire in the forest","Rain","Thunder and rain","Wave","wind"};
    public static String[] noiseDescription= {"Listen to the sound of bonfire burning, insects and birds will calm you down.",
                                                "The sound of rain is the developer's favorite white noise, it can make you relax",
                                                "Thunder and rain is helpful to make you asleep and provide you of a sense of safety",
    "It is generally believed that wave can make some people more focused on their assignment","Listening to the wind while coding may be the second happiest thing."};

    public static List<NoiseItem> getDefaultList(){
        List<NoiseItem> list = new ArrayList<>();
        for (int i = 0; i < imageArray.length; i++) {
            list.add(new NoiseItem(imageArray[i],titleArray[i],noiseDescription[i],mediaResources[i]));
        }
        return list;
    }
    public static int[] getMediaResourceArray(){
        return mediaResources;
    }
}
