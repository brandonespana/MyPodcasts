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

/**
 * Created by biespana on 4/30/15.
 */
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
        Button playButton = null;

        final String[] splitString = list.get(position).split(Pattern.quote("_biespana_"));
        String title = splitString[0];

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();

            row = inflater.inflate(resource, parent, false);
        }

        titleView = (TextView)row.findViewById(R.id.episodeTitle);
        playButton = (Button)row.findViewById(R.id.episodePlayButton);

        titleView.setText(title);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(splitString[1]!= null){
                    System.out.println("will setup audio stream for url: "+splitString[1]);
                    try{
                        //String url = "http://........"; // your URL here
                        String url = splitString[1]; // your URL here
                        MediaPlayer mediaPlayer = new MediaPlayer();
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mediaPlayer.setDataSource(url);
                        mediaPlayer.prepare(); // might take long! (for buffering, etc)
                        mediaPlayer.start();

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
//        Weather weather = data[position];
//        holder.txtTitle.setText(weather.title);
//        holder.imgIcon.setImageResource(weather.icon);

        return row;
    }

    static class MyAudioPlayer implements MediaPlayer.OnPreparedListener{
        private MediaPlayer player;

        public MyAudioPlayer(){
            player = new MediaPlayer();

        }

        public void playEpisode(String url) throws IOException {
            player.reset();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(url);
            player.prepareAsync();
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            mp.start();
        }
    }
}
