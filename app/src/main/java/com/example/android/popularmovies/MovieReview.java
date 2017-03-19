/*
 * Copyright (c) 2017 EConceptes. All rights reserved.
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 * http://www.econceptes.com
 */

/*
 * Copyright (c) 2017 EConceptes. All rights reserved.
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 * http://www.econceptes.com
 */

package com.example.android.popularmovies;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by jlainezs on 18/03/2017 for PopularMovies
 */

public class MovieReview {

    private String id;
    private String author;
    private String content;
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public MovieReview(JSONObject review)
    {
        try{
            setAuthor(review.getString("author"));
            setContent(review.getString("content"));
            setId(review.getString("id"));
            setUrl(review.getString("url"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
