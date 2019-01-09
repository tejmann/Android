package com.example.android.newsreport20;

import android.graphics.Bitmap;

public class NewsItems {
    private String title;
    private String date;
    private String wurl;
    private String author;
    private String imageUrl;
    private Bitmap bitmap=null;


    public NewsItems(String title, String date, String wurl, String author, String imageUrl){
        this.title=title;
        this.date=date;
        this.wurl=wurl;
        this.author=author;
        this.imageUrl=imageUrl;

    }



    public NewsItems(String title, String date, String wurl, String author, Bitmap bitmap){
        this.title=title;
        this.date=date;
        this.wurl=wurl;
        this.author=author;
        this.bitmap=bitmap;

    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getWurl() {
        return wurl;
    }

    public String getAuthor() {
        return author;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public Bitmap getBitmap() {
        if (bitmap==null){
            return null;
        }
        return bitmap;
    }
}
