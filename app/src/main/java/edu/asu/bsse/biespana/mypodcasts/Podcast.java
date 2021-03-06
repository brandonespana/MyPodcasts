package edu.asu.bsse.biespana.mypodcasts;

import android.graphics.Bitmap;

import java.util.ArrayList;

//  Copyright (c) 2015 Brandon Espana,
//  The professor and TA have the right to build and evaluate this software package
//
//  @author: Brandon Espana mailto:biespana@asu.edu
//  @Version: May 1, 2015

public class Podcast {
    private String title;
    private String author;
    private String feedUrl;
    private Bitmap artworkImage;
    private String description;


    private int coverArt;
    private ArrayList<String> episodes;

    public Podcast(String title, String author, String feedUrl, Bitmap artworkImage){
        this.title = title;
        this.author = author;
        this.feedUrl = feedUrl;
        this.artworkImage = artworkImage;
    }

    public Bitmap getArtworkImage(){
        return artworkImage;
    }

    public String getDescription(){
        return description;
    }
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getFeedUrl() {
        return feedUrl;
    }

    public int getCoverArt() {
        return coverArt;
    }



}
