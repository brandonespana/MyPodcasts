package edu.asu.bsse.biespana.mypodcasts;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Copyright 2015 Brandon España
 * <p/>
 * Instructor and TA have the right to build and evaluate the software package
 *
 * @author Brandon España Brandon.Espana@asu.edu
 * @version April 29, 2015
 */
public class SearchResultsAdapter extends ArrayAdapter<Podcast>{
    private int resource;
    private Context context;
    private List<Podcast> podcasts;

    public SearchResultsAdapter(Context context, int resource, List<Podcast> podcasts) {
        super(context, resource, podcasts);
        this.context = context;
        this.resource = resource;
        this.podcasts = podcasts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TextView titleView = null;
        TextView authorView = null;
        ImageView artworkView = null;

        String title = podcasts.get(position).getTitle();
        String author = podcasts.get(position).getAuthor();
        Bitmap coverArtImage = podcasts.get(position).getArtworkImage();

        //WeatherHolder holder = null;

        if(row == null)
        {
            //LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();

            row = inflater.inflate(resource, parent, false);



//            holder = new WeatherHolder();
//            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
//            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);

            //row.setTag(holder);
        }
//        else
//        {
//            //holder = (WeatherHolder)row.getTag();
//
//        }

        titleView = (TextView)row.findViewById(R.id.podcastTitle);
        authorView = (TextView)row.findViewById(R.id.podcastAuthor);
        artworkView = (ImageView)row.findViewById(R.id.coverArtIcon);

        titleView.setText(title);
        authorView.setText(author);
        artworkView.setImageBitmap(coverArtImage);

//        Weather weather = data[position];
//        holder.txtTitle.setText(weather.title);
//        holder.imgIcon.setImageResource(weather.icon);

        return row;
    }
}
