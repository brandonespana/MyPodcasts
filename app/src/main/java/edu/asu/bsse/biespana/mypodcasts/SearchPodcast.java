package edu.asu.bsse.biespana.mypodcasts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

//  Copyright (c) 2015 Brandon Espana,
//  The professor and TA have the right to build and evaluate this software package
//
//  @author: Brandon Espana mailto:biespana@asu.edu
//  @Version: May 1, 2015

public class SearchPodcast extends Activity {
    private Activity self = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_for_podcast);
        setTitle("Search For Podcast");
    }


    public void performSearch(View view){
        EditText searchField = (EditText) findViewById(R.id.searchBox);
        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchField.getWindowToken(), 0);

        String searchTerm = searchField.getText().toString();
        String urlEncodedTerm = searchTerm.replace(" ","+");
        System.out.println("Url encoded Search Term: "+urlEncodedTerm);
        findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        new AsyncSearch().execute(urlEncodedTerm);



    }

    private class AsyncSearch extends AsyncTask<String, Void, ArrayList<Podcast>> {

        protected ArrayList<Podcast> doInBackground (String...searchTerms){
            ArrayList<String> results = new ArrayList<String>();
            ArrayList<Podcast> podcasts = new ArrayList<Podcast>();
            HttpsURLConnection connection = null;

            String queryString = "term="+searchTerms[0]+"&media=podcast";
            String urlString = "https://itunes.apple.com/search?"+queryString;
            System.out.println("#################: "+urlString);
            try {
                URL url = new URL(urlString);
                connection = (HttpsURLConnection) url.openConnection();
                System.out.println("Connection established?");
                InputStream inputStream = new BufferedInputStream(connection.getInputStream());

                StringBuffer builder = new StringBuffer("");
                int read;
                while((read = inputStream.read())>-1){
                    char c = (char) read;
                    builder.append(c);
                }
                String receivedJsonString = builder.toString();
                results.add(receivedJsonString);

                //Extract information (podcast titles, authors, artwork) from the json response:
                    JSONObject responseObj = new JSONObject(receivedJsonString);//used to be JSONObject(item)
                    JSONArray list = (JSONArray) responseObj.get("results");
                    for (int i = 0; i < list.length();i++){
                        JSONObject oneItem = list.getJSONObject(i);
                        String title = (String) oneItem.get("collectionName");
                        String author = (String) oneItem.get("artistName");
                        String feedUrl = (String) oneItem.get("feedUrl");
                        String artworkUrl = (String) oneItem.get("artworkUrl100");

                        //get the artwork:
                        System.out.println("Getting the image...");
                        InputStream in = new URL(artworkUrl).openStream();
                        Bitmap artworkImage = BitmapFactory.decodeStream(in);
                        System.out.println("Done getting the image");

                        Podcast onePodcast = new Podcast(title, author, feedUrl, artworkImage);
                        podcasts.add(onePodcast);
                    }
            }
            catch(Exception  e){
                e.printStackTrace();
            }
            finally{
                connection.disconnect();
            }
            return podcasts;
        }

        protected void onPostExecute(final ArrayList<Podcast> podcasts){
            TextView noResultsView = (TextView)findViewById(R.id.noResultsView);

            if(podcasts.size()<1){
                System.out.println("No results were found");
                noResultsView.setVisibility(View.VISIBLE);
            }
            else{
                noResultsView.setVisibility(View.GONE);
            }

            ListView resultsListView = (ListView) findViewById(R.id.searchResults);
            SearchResultsAdapter myAdapter = new SearchResultsAdapter(self,R.layout.individual_search_result,podcasts);
            resultsListView.setAdapter(myAdapter);
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);

            resultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Podcast selectedPodcast = podcasts.get(position);
                    //System.out.println("Clicked on: "+ podcasts.get(position).getTitle());
                    System.out.println("Clicked on: "+ selectedPodcast.getTitle());
                    //Create Intent to start PodcastActivity
                    Intent intent = new Intent(self, PodcastActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("podcastTitle", selectedPodcast.getTitle());
                    bundle.putString("podcastAuthor", selectedPodcast.getAuthor());
                    bundle.putString("podcastFeedUrl", selectedPodcast.getFeedUrl());
                    bundle.putParcelable("podcastImage",selectedPodcast.getArtworkImage());
                    intent.putExtra("podcastBundle",bundle);

                    startActivity(intent);

                }
            });
        }
    }
}
