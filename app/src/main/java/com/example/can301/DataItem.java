package com.example.can301;

public class DataItem {
    int image;
    String title;
    int price;

    public DataItem(int image, String title, int price){
        this.image = image;
        this.title = title;
        this.price = price;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {return price; }

    public void setPrice(int price) {this.price = price; }
}
