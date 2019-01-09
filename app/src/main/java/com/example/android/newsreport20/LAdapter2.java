package com.example.android.newsreport20;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class LAdapter2 extends ArrayAdapter<NewsItems> {
    public LAdapter2(Context context, ArrayList<NewsItems> newsitems){
        super(context,0,newsitems);

    }




    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater=(LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.newslayout2,parent,false);

        }
        NewsItems newsitem=getItem(position);
        TextView name=convertView.findViewById(R.id.namev);
        name.setText(newsitem.getTitle());
        TextView author=convertView.findViewById((R.id.authorv));
        author.setText(newsitem.getAuthor());
        TextView date=convertView.findViewById(R.id.datev);
        date.setText(newsitem.getDate());
        ImageView imageView=convertView.findViewById(R.id.imagev);
        Bitmap bitmap=newsitem.getBitmap();
        if (bitmap==null){
            imageView.setImageResource(R.drawable.icon);
            return convertView;
        }
        else {
            imageView.setImageBitmap(bitmap);
            return convertView;

        }



    }
}
