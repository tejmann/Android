package com.example.android.newsreport20;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class All extends Fragment implements LoaderManager.LoaderCallbacks{

    public String baseurl="https://content.guardianapis.com/search?api-key=43ea61e3-5dca-4e36-b36e-c60226c2a238&show-tags=contributor&page-size=50&show-fields=thumbnail";
    public ArrayList<String> imageurllist;
    public ArrayList<NewsItems> newsItemsArrayList;
    public LAdapter2 lAdapter2;
    private int j;
    private int pageNumber=1;
    private LoaderManager.LoaderCallbacks proto=new LoaderManager.LoaderCallbacks() {
        @NonNull
        @Override
        public Loader onCreateLoader(int i, @Nullable Bundle bundle) {
//            String pagenumber=bundle.getString("pageNumber");
            String newUrl=baseurl+"&page="+pageNumber;
            Log.i("MainActivity","mann"+newUrl);
            return new NewsLoader(getContext(),newUrl);
        }

        @Override
        public void onLoadFinished(@NonNull Loader loader, Object o) {
            Log.i("MainActivity","mann//");

            ArrayList<NewsItems> newsItems=new ArrayList<NewsItems>();
            ArrayList<String> imlist=new ArrayList<String>();
            try {
                newsItems=JsonReader.newsmaker((String) o);
                imlist=JsonReader.imageUrllist(newsItems);
            } catch (JSONException e) {
                e.printStackTrace();

        }
        finally {
                updateList(newsItems);
                updateImList(imlist);
                lAdapter2.notifyDataSetChanged();
                int c=imageurllist.size()-50;
                int k=imageurllist.size();
                while(c<k){
                    Bundle bundle=new Bundle();
                    bundle.putString("site",imageurllist.get(c));

                    getLoaderManager().initLoader(c,bundle,sil);
                    c=c+1;


                }
                getLoaderManager().destroyLoader(loader.getId());

            }


        }

        @Override
        public void onLoaderReset(@NonNull Loader loader) {



        }
    };

    private LoaderManager.LoaderCallbacks nl=new LoaderManager.LoaderCallbacks() {
        @NonNull
        @Override
        public Loader onCreateLoader(int i, @Nullable Bundle bundle) {

            return new NewsLoader(getContext(),baseurl);
        }

        @Override
        public void onLoadFinished(@NonNull Loader loader, Object o) {
            ArrayList<NewsItems> newsItems=new ArrayList<NewsItems>();
            ArrayList<String> imlist=new ArrayList<String>();
            Log.i("MAIN ACTIVITY","On load finish");
            try {
                newsItems=JsonReader.newsmaker((String) o);
                imlist=JsonReader.imageUrllist(newsItems);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            finally {

                    setNewsItemsArrayList(newsItems);
                    setImageurllist(imlist);

                lAdapter2=new LAdapter2(getActivity(),newsItemsArrayList);
                j=newsItemsArrayList.size();
                Log.i("Main Activity","jatt"+j);
                int c=0;

                while(c<j){
                    Bundle bundle=new Bundle();
                    bundle.putString("site",imageurllist.get(c));
                    getLoaderManager().initLoader(c,bundle,sil);
                    c=c+1;


                }




                ListView listView=getView().findViewById(R.id.alllist);



                listView.setAdapter(lAdapter2);
                listView.setVisibility(View.VISIBLE);
                ProgressBar progressBar=getView().findViewById(R.id.progress_bar);
                progressBar.setVisibility(View.GONE);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                      NewsItems newsItems1=newsItemsArrayList.get(i);
                      String wurl=newsItems1.getWurl();
                        Intent intent=new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(wurl));
                        startActivity(intent);

                    }
                });
                listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView absListView, int i) {

                    }

                    @Override
                    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

//                        AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount
                        if (i==i2-i1&& i2!=0) {
                            Bundle bundle=new Bundle();
                            pageNumber=pageNumber+1;
                            Log.i("Main Activity","PageNumber="+pageNumber);

//                            bundle.putString("pageNumber",""+pageNumber);

                            getLoaderManager().initLoader(-99,null,proto);


                            }


                    }
                });

            }

        }

        @Override
        public void onLoaderReset(@NonNull Loader loader) {



        }
    };

    private LoaderManager.LoaderCallbacks sil=new LoaderManager.LoaderCallbacks() {
        @NonNull
        @Override
        public Loader onCreateLoader(int i, @Nullable Bundle bundle) {
            return new singleimageloader(getContext(),(String) bundle.get("site"));
        }

        @Override
        public void onLoadFinished(@NonNull Loader loader, Object o) {

            int id=loader.getId();
            Log.i("MAIN ACTIVITY",id+"");
            NewsItems newsItems1=newsItemsArrayList.get(id);
            NewsItems newsItems2=change(newsItems1,(Bitmap) o);
            newsItemsArrayList.set(id,newsItems2);
            lAdapter2.notifyDataSetChanged();
            getLoaderManager().destroyLoader(id);
            //Too many images to load and destroying the loader beomes necessary , otherwise will at some time run Threading error




        }

        @Override
        public void onLoaderReset(@NonNull Loader loader) {



        }
    };

    public All() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getLoaderManager().initLoader(-1,null,nl);



        return inflater.inflate(R.layout.fragment_all, container, false);
    }



    @NonNull
    @Override
    public Loader onCreateLoader(int i, @Nullable Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader loader, Object o) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {

    }

    public void setImageurllist(ArrayList<String> list){
        this.imageurllist=list;
    }
    public void setNewsItemsArrayList(ArrayList<NewsItems> list){
        this.newsItemsArrayList=list;
    }
    public NewsItems change(NewsItems newsItems,Bitmap bitmap){
        String title=newsItems.getTitle();
        String date=newsItems.getDate();
        String wurl=newsItems.getWurl();
        String author=newsItems.getAuthor();
        return new NewsItems(title,date,wurl,author,bitmap);
    }

    public void updateList(ArrayList<NewsItems> newsItems){
        this.newsItemsArrayList.addAll(newsItems);
        //updates the list by adding newsitems
    }
    public void updateImList(ArrayList<String> imageurl){
        this.imageurllist.addAll(imageurl);
        //updates the list by adding newsitems
    }
}
