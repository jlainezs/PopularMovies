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

package com.example.android.popularmovies.async;

/**
 * Created by jlainezs on 11/02/2017 for PopularMovies
 */

import android.content.Context;
import android.os.AsyncTask;

import com.example.android.popularmovies.pojos.MovieVideo;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.utilities.Network;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Fetches movies
 */
public class MoviesVideosFetcher extends AsyncTask<URL, Void, ArrayList<MovieVideo>> {
    private static final String TAG = "FetchMovies";
    private Context context;
    private AsyncTaskCompleteListener<ArrayList<MovieVideo>> listener;

    Exception exception = null;

    public MoviesVideosFetcher(Context cts, AsyncTaskCompleteListener<ArrayList<MovieVideo>> listener){
        this.context = cts;
        this.listener = listener;
    }

    @Override
    protected ArrayList<MovieVideo> doInBackground(URL... urls) {
        URL searchUrl = urls[0];
        ArrayList<MovieVideo> movieVideos = new ArrayList<>();

        try {
            if (Network.isOnline(context)) {
                String jsonMoviesVideosStr = Network.getResponseFromHttpUrl(searchUrl);
                JSONObject jsonMoviesVideos = new JSONObject(jsonMoviesVideosStr);
                JSONArray results = jsonMoviesVideos.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject video = results.getJSONObject(i);
                    movieVideos.add(new MovieVideo(video));
                }
            } else {
                throw new ConnectException(context.getString(R.string.no_network_available));
            }
        }
        catch (Exception e){
            exception = e;
        }

        return movieVideos;
    }

    @Override
    protected void onPostExecute(ArrayList<MovieVideo> moviesResult) {
        super.onPostExecute(moviesResult);
        listener.onTaskComplete(moviesResult, exception);
    }


}
