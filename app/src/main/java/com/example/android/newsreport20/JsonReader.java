package com.example.android.newsreport20;

import android.graphics.Bitmap;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonReader {
    private static String TAG="JSONREADER ACTIVITY";
    public static ArrayList<NewsItems> newsmaker(String json) throws JSONException {
        ArrayList<NewsItems> newsItems=new ArrayList<NewsItems>();
        JSONObject jsonObject=new JSONObject(json);
        JSONObject response=jsonObject.getJSONObject("response");
        JSONArray results=response.getJSONArray("results");

        int i=0;
        while(i<results.length()){
            JSONObject item=(JSONObject) results.get(i);
            String title =item.getString("webTitle");
            String date=item.getString("webPublicationDate");
            String wurl=item.getString("webUrl");
            String imageUrl;
            JSONObject fields=new JSONObject();
            if(item.has("fields")){
              //some newsitems don't have thumbnail in fields and can throw jsonexception,
                //example is a croosword which had an interactive croosword and not an image
                fields=item.getJSONObject("fields");
                imageUrl=fields.getString("thumbnail");


            }
            else
            {
                imageUrl="https://upload.wikimedia.org/wikipedia/en/f/f9/No-image-available.jpg";
            }



            JSONArray tags=item.getJSONArray("tags");
            String author;
            if (tags.length()==0){
                author="Anonymous";

            }
            else if(tags.length()==1){
                String type="contributor";
                JSONObject contributor=(JSONObject) tags.get(0);
                author=contributor.getString("webTitle");

            }
            else{
                JSONObject contributor=(JSONObject) tags.get(0);
                author=contributor.getString("webTitle")+" et al.";

            }



            NewsItems newsItems1=new NewsItems(title,date,wurl,author,imageUrl);
            newsItems.add(newsItems1);
            i=i+1;


        }
        return newsItems;



    }
    public static ArrayList<String> imageUrllist(ArrayList<NewsItems> newsItemsArrayList){
        int i=0;
        ArrayList<String> arrayList=new ArrayList<String>();
        Log.i(TAG,"1"+i);
        while(i<newsItemsArrayList.size()){

            NewsItems newsItems=newsItemsArrayList.get(i);
            String imgUrl=newsItems.getImageUrl();
            Log.i(TAG,""+imgUrl);

            arrayList.add(imgUrl);
            i=i+1;

        }
        return arrayList;

    }
    public static ArrayList<NewsItems> getNewsItems(ArrayList<NewsItems> listwurl,ArrayList<Bitmap> listwbm){

        int i=0;
        ArrayList<NewsItems> newsItemsArrayList=new ArrayList<NewsItems>();
        while (i < listwurl.size()) {
            Log.i(TAG,""+listwbm.size()+listwurl.size());
            NewsItems newsItems=listwurl.get(i);
            String author=newsItems.getAuthor();
            String date=newsItems.getDate();
            String title=newsItems.getTitle();
            String wurl=newsItems.getWurl();
            Bitmap bitmap=listwbm.get(i);
            NewsItems newsItems1=new NewsItems(title,date,wurl,author,bitmap);
            newsItemsArrayList.add(newsItems1);
            i=i+1;


        }
        return newsItemsArrayList;

    }
}
