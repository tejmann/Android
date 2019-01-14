package com.example.android.newsreport20;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class NewsLoader extends AsyncTaskLoader {
    //Loads the news from the web in the form of JSON expression
    private String site;

    public NewsLoader(@NonNull Context context, String site) {
        super(context);
        this.site = site;


    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Nullable
    @Override
    public Object loadInBackground() {

        HttpURLConnection urlConnection=null;
        URL url=Urlmaker(site);
        InputStream inputStream=null;
        String JsonResponse="";
        ArrayList<NewsItems> newsItems=new ArrayList<NewsItems>();
        ArrayList<String> stringArrayList=new ArrayList<String>();
        try{
            urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();
            int rc=urlConnection.getResponseCode();
            if(rc==200){
                inputStream=urlConnection.getInputStream();
                JsonResponse=insmaker(inputStream);


            }

        }
        catch (IOException e){
            Log.i("NewsLoader Activity","loadinbackgroung",e);
        }
        finally{
            if(urlConnection!=null){
                urlConnection.disconnect();
            }
            if(inputStream!=null){
                try{inputStream.close();}
                catch (IOException e){
                    Log.i("Newsloader inputstream","close");
                }
            }
            try {
                newsItems=JsonReader.newsmaker(JsonResponse);
                stringArrayList=JsonReader.imageUrllist(newsItems);


            } catch (JSONException e) {
                Log.i("NEWSLOADER ACTIVITY","EXCEPTION");
                e.printStackTrace();
            }

            return JsonResponse;}


    }

    public URL Urlmaker(String site){
        URL website=null;
        try{
            website=new URL(site);
        }
        catch (MalformedURLException e){
            Log.i("NewsLoaderActivity","Malformed URL",e);
        }
        finally {
            return website;
        }}

    public String insmaker(InputStream i) throws IOException {
        StringBuilder sb=new StringBuilder();
        if (i!=null){
            InputStreamReader isr=new InputStreamReader(i, Charset.forName("UTF-8"));
            BufferedReader bf=new BufferedReader(isr);
            String line=bf.readLine();
            while (line!=null){
                sb.append(line);
                line=bf.readLine();
            }}
        return sb.toString();}

}
