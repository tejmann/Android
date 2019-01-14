package com.example.android.newsreport20;


import android.content.Intent;
import android.graphics.Bitmap;
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

import org.json.JSONException;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Business1 extends Fragment implements LoaderManager.LoaderCallbacks{


    public String baseurl="https://content.guardianapis.com/search?api-key=43ea61e3-5dca-4e36-b36e-c60226c2a238&show-tags=contributor&page-size=50&show-fields=thumbnail&section=business";
    //baseurl is the url to access guardian api
    public ArrayList<String> imageurllist; //arraylist to store the image urls
    public ArrayList<NewsItems> newsItemsArrayList; //arraylist of news items
    public LAdapter2 lAdapter2; //Listadapter for the list view
    private int size; //size of the newsitemarraylist
    private int pageNumber=1;

    public Business1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getLoaderManager().initLoader(-1,null,newLoader);
        //initiates the newloader



        return inflater.inflate(R.layout.fragment_all, container, false);

    }

    private LoaderManager.LoaderCallbacks newLoader=new LoaderManager.LoaderCallbacks() {
        @NonNull
        @Override
        public Loader onCreateLoader(int i, @Nullable Bundle bundle) {
            //returns a Newsloader which fetches the data from web

            return new NewsLoader(getContext(),baseurl);
        }

        @Override
        public void onLoadFinished(@NonNull Loader loader, Object o) {
            ArrayList<NewsItems> newsItems=new ArrayList<NewsItems>();
            ArrayList<String> imagelist=new ArrayList<String>();
            Log.i("MAIN ACTIVITY","On load finish");
            try {
                newsItems=JsonReader.newsmaker((String) o);
                imagelist=JsonReader.imageUrllist(newsItems);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            finally {

                setNewsItemsArrayList(newsItems);
                setImageurllist(imagelist);
                //updates the newsItemArrayList and Imageurllist

                lAdapter2=new LAdapter2(getActivity(),newsItemsArrayList);
                //new lAdapter2 object
                size=newsItemsArrayList.size();
                Log.i("Main Activity","jatt"+size);
                int c=0;

                while(c<size){
                    Bundle bundle=new Bundle();
                    bundle.putString("site",imageurllist.get(c));
                    getLoaderManager().initLoader(c,bundle,sil);
                    //sil loads the image assosiated with the news item and then updates the list adapter
                    c=c+1;
                }

                ListView listView=getView().findViewById(R.id.alllist);
                listView.setAdapter(lAdapter2);
                listView.setVisibility(View.VISIBLE);
                //change the visibilty of listview and progressbar when the data loaded.
                ProgressBar progressBar=getView().findViewById(R.id.progress_bar);
                progressBar.setVisibility(View.GONE);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //opens the complete news in web browser
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
                        //loads newsItems from the next page of the gaurdian api after every 50 news
                        //one page of the guardian api can show at max 50 news

//                        AbsListView view, int  i is firstVisibleItem, int i1 is visibleItemCount, int i2 is totalItemCount
                        if (i==i2-i1&& i2!=0) {
                            Bundle bundle=new Bundle();
                            pageNumber=pageNumber+1;
                            Log.i("Main Activity","PageNumber="+pageNumber);
                            getLoaderManager().initLoader(-99,null,updateLoader);


                        }


                    }
                });

            }

        }

        @Override
        public void onLoaderReset(@NonNull Loader loader) {



        }
    };


    private LoaderManager.LoaderCallbacks updateLoader=new LoaderManager.LoaderCallbacks() {
        //Loads the  data from the web when newsitems from the first page of the web api end.
        @NonNull
        @Override
        public Loader onCreateLoader(int i, @Nullable Bundle bundle) {
//            String pagenumber=bundle.getString("pageNumber");
            String newUrl=baseurl+"&page="+pageNumber;
            //updates the baseurl to go to the next page
            Log.i("MainActivity","current url"+newUrl);
            return new NewsLoader(getContext(),newUrl);
        }

        @Override
        public void onLoadFinished(@NonNull Loader loader, Object o) {
            Log.i("MainActivity","check onLoadFinished");

            ArrayList<NewsItems> newsItems=new ArrayList<NewsItems>();
            ArrayList<String> imagelist=new ArrayList<String>();
            try {
                newsItems=JsonReader.newsmaker((String) o);
                imagelist=JsonReader.imageUrllist(newsItems);
            } catch (JSONException e) {
                e.printStackTrace();

            }
            finally {
                updateList(newsItems);
                updateImList(imagelist);
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



    private LoaderManager.LoaderCallbacks sil=new LoaderManager.LoaderCallbacks() {
        //Loads the image associated with the news
        @NonNull
        @Override
        public Loader onCreateLoader(int i, @Nullable Bundle bundle) {
            return new singleimageloader(getContext(),(String) bundle.get("site"));
        }

        @Override
        public void onLoadFinished(@NonNull Loader loader, Object o) {
            //updates the listview
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
        //adds the bitmap image to the news associated with it
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


