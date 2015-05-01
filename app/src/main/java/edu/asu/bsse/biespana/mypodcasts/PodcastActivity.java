package edu.asu.bsse.biespana.mypodcasts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class PodcastActivity extends Activity {
    private ImageView coverArtView;
    private TextView titleView;
    private TextView authorView;
    private TextView descriptionView;
    private ListView episodesList;

    private Bitmap podcastCoverArt;
    private String podcastTitle;
    private String podcastAuthor;
    private String podcastDescription;
    private String feedUrl;

    private Context thisContext = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcast);
        Intent incomingIntent = getIntent();
        Bundle intentBundle = incomingIntent.getBundleExtra("podcastBundle");

        podcastTitle = intentBundle.getString("podcastTitle");
        podcastAuthor = intentBundle.getString("podcastAuthor");
        podcastCoverArt = intentBundle.getParcelable("podcastImage");
        feedUrl = intentBundle.getString("podcastFeedUrl");


        coverArtView = (ImageView) findViewById(R.id.podcastActivityCover);
        titleView = (TextView) findViewById(R.id.podcastActivityTitle);
        authorView = (TextView) findViewById(R.id.podcastActivityAuthor);
        descriptionView = (TextView) findViewById(R.id.podcastActivityDescription);
        episodesList = (ListView)findViewById(R.id.episodesList);

        coverArtView.setImageBitmap(podcastCoverArt);
        titleView.setText(podcastTitle);
        authorView.setText("By "+podcastAuthor);
        setTitle(podcastTitle);

        //Start background task to get the list of episodes and the urls to stream from
        System.out.println("Got this: "+feedUrl);
        new AsyncGetEpisodes().execute(feedUrl);

    }

    private class AsyncGetEpisodes extends AsyncTask<String, Void, List<String>> {

        @Override
        protected List<String> doInBackground(String... params) {
            String feed = params[0];
            RssParser parser = new RssParser();
            List items = null;
            try{
                URL url = new URL(feed);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();

                items = parser.parseXML(conn.getInputStream());
            }
            catch(Exception e){
                e.printStackTrace();
            }

            if(items == null){
                System.out.println("items is null");
                //System.out.println("received this from the parser: "+items.toString());
            }

            return items;
        }

        @Override
        protected void onPostExecute(List<String> items){
            if(items!=null){
                ArrayList episodeTitles = new ArrayList();
                ArrayList episodeStreamUrls = new ArrayList();

                podcastDescription = items.get(0);
                //descriptionView = (TextView)findViewById()
                descriptionView.setText(podcastDescription);

                System.out.println("Episodes Information");
                int size = items.size();

                for(int i = 1; i < size; i ++){
                    System.out.println(items.get(i));
                    String[] splitString = items.get(i).split(Pattern.quote("_biespana_"));
                    //System.out.println("splitstring length is : "+splitString.length);
                    episodeTitles.add(splitString[0]);
                    episodeStreamUrls.add(splitString[1]);
                }
                //Set custom adapter that will receive titles and urls ('items' array)
                //ArrayAdapter adapter = new ArrayAdapter(thisContext,android.R.layout.simple_list_item_1, episodeTitles);
                //EpisodeListAdapter adapter = new EpisodeListAdapter(thisContext,R.layout.individual_episode,items);
                items.remove(0);
                EpisodeListAdapter adapter = new EpisodeListAdapter(thisContext,R.layout.individual_episode,items);
                episodesList.setAdapter(adapter);




            }

        }
    }
}
