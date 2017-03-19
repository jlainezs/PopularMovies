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

package com.example.android.popularmovies.dataclasses;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by jlainezs on 18/03/2017 for PopularMovies
 */

public class MovieVideo {
    private String id;
    private String iso_639_1;
    private String iso_3166_1;
    private String key;
    private String name;
    private String site;
    private String size;
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIso_639_1() {
        return iso_639_1;
    }

    public void setIso_639_1(String iso_639_1) {
        this.iso_639_1 = iso_639_1;
    }

    public String getIso_3166_1() {
        return iso_3166_1;
    }

    public void setIso_3166_1(String iso_3166_1) {
        this.iso_3166_1 = iso_3166_1;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MovieVideo(JSONObject jsonMovie)
    {
        try {
            setId(jsonMovie.getString("id"));
            setIso_639_1(jsonMovie.getString("iso_639_1"));
            setIso_3166_1(jsonMovie.getString("iso_3166_1"));
            setKey(jsonMovie.getString("key"));
            setName(jsonMovie.getString("name"));
            setSite(jsonMovie.getString("site"));
            setSize(jsonMovie.getString("size"));
            setType(jsonMovie.getString("type"));
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public URL getVideoPath() throws MalformedURLException {
        URL videoPath = null;

        if (getSite().toLowerCase().equals("youtube")) {
            Uri videoUri = Uri.parse("http://www.youtube.com")
                    .buildUpon()
                    .appendPath("watch")
                    .appendQueryParameter("v", getKey())
                    .build();
            videoPath = new URL(videoUri.toString());
        }

        return videoPath;
    }
}
