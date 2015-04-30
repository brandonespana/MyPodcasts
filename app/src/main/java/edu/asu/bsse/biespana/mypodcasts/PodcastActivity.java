package edu.asu.bsse.biespana.mypodcasts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


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

        coverArtView.setImageBitmap(podcastCoverArt);
        titleView.setText(podcastTitle);
        authorView.setText(podcastAuthor);
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

                parser.parseXML(conn.getInputStream());
            }
            catch(Exception e){
                e.printStackTrace();
            }

            if(items != null){
                System.out.println("received this from the parser: "+items.toString());
            }
            else{
                System.out.println("items is null");
            }


            return items;
        }

        @Override
        protected void onPostExecute(List<String> items){
            System.out.println("items 0: "+ items.get(0));
        }
    }
}
