package com.example.android.newsreport20;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class ImageLoader extends AsyncTaskLoader {
    private ArrayList<String> list;

    public ImageLoader(@NonNull Context context, ArrayList<String> list) {
        super(context);
        this.list = list;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Nullable
    @Override
    public Object loadInBackground() {
        int c=0;
        ArrayList<Bitmap> bitmaps=new ArrayList<Bitmap>();
        while(c<list.size()){
            Bitmap bitmap;
            String imageurl=list.get(c);
            InputStream inputStream2= null;
            try {
                inputStream2 = new URL(imageurl).openStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bitmap= BitmapFactory.decodeStream(inputStream2);

            bitmaps.add(bitmap);
            c = c + 1;
        }
        return bitmaps;
    }
}
