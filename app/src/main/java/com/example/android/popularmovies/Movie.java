package com.example.android.popularmovies;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;


public class Movie implements Serializable {
    private String poster_path;
    private String overview;
    private String release_date;
    private Long id;
    private String original_title;
    private String title;
    private Double vote_average;

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getVote_average() {
        return vote_average;
    }

    public void setVote_average(Double vote_average) {
        this.vote_average = vote_average;
    }

    public Movie(JSONObject jsonMovie)
    {
        try {
            setPoster_path(jsonMovie.getString("poster_path"));
            setOverview(jsonMovie.getString("overview"));
            setRelease_date(jsonMovie.getString("release_date"));
            setOriginal_title(jsonMovie.getString("original_title"));
            setTitle(jsonMovie.getString("title"));
            setVote_average(jsonMovie.getDouble("vote_average"));
        } catch (JSONException e){
            e.printStackTrace();
        }
    }
}
