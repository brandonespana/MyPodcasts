package edu.asu.bsse.biespana.mypodcasts;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

//  Copyright (c) 2015 Brandon Espana,
//  The professor and TA have the right to build and evaluate this software package
//
//  @author: Brandon Espana mailto:biespana@asu.edu
//  @Version: May 1, 2015

public class EpisodeListAdapter extends ArrayAdapter<String> {
    private Context context;
    private int resource;
    private List<String> list;
    private static MyAudioPlayer myPlayer;

    public EpisodeListAdapter(Context context, int resource, List<String> items) {
        super(context, resource, items);
        this.context = context;
        this.resource = resource;
        this.list = items;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TextView titleView = null;

        final String[] splitString = list.get(position).split(Pattern.quote("_biespana_"));
        String title = splitString[0];

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();

            row = inflater.inflate(resource, parent, false);
        }

        titleView = (TextView)row.findViewById(R.id.episodeTitle);
        final Button playButton = (Button)row.findViewById(R.id.episodePlayButton);
        final Button pauseButton = (Button)row.findViewById(R.id.episodePauseButton);
        final View loadingCircle = (View)row.findViewById(R.id.episodeLoading);

        titleView.setText(title);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(splitString[1]!= null){

                    System.out.println("will setup audio stream for url: "+splitString[1]);
                    try{
                        String url = splitString[1];

                        MyAudioPlayer myPlayer = new MyAudioPlayer();
                        myPlayer.playEpisode(url, playButton, loadingCircle );
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return row;
    }

    static class MyAudioPlayer implements MediaPlayer.OnPreparedListener{
        private static MediaPlayer player = null;
        private Button playButton = null;
        private View loading = null;

        public MyAudioPlayer(){
            if(player == null){
                player = new MediaPlayer();
                System.out.println("initializing media player, it was null");
            }
        }

        public void playEpisode(String url, Button play, View loadingCircle) throws IOException {
            System.out.println("inside playEpisode");
            player.reset();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(url);
            player.setOnPreparedListener(this);
            player.prepareAsync();
            playButton = play;
            loading = loadingCircle;
            play.setVisibility(View.GONE);
            loadingCircle.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            System.out.println("ready to play");
            playButton.setVisibility(View.VISIBLE);
            loading.setVisibility(View.GONE);
            mp.start();
        }
    }
}
