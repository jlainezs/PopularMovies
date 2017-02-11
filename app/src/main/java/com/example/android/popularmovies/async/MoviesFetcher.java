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
import android.widget.GridView;
import android.widget.Toast;

import com.example.android.popularmovies.MainActivity;
import com.example.android.popularmovies.Movie;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapters.MoviesAdapter;
import com.example.android.popularmovies.utilities.Network;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

/**
 * Fetches movies
 */
public class MoviesFetcher extends AsyncTask<URL, Void, ArrayList<Movie>> {
    private static final String TAG = "FetchMovies";
    private Context context;
    private AsyncTaskCompleteListener<ArrayList<Movie>> listener;

    Exception exception = null;

    public MoviesFetcher(Context cts, AsyncTaskCompleteListener<ArrayList<Movie>> listener){
        this.context = cts;
        this.listener = listener;
    }

    @Override
    protected ArrayList<Movie> doInBackground(URL... urls) {
        URL searchUrl = urls[0];
        ArrayList<Movie> movies = new ArrayList<>();
        try {
            if (Network.isOnline(context)) {
                String jsonMoviesStr = Network.getResponseFromHttpUrl(searchUrl);
                JSONObject jsonMovies = new JSONObject(jsonMoviesStr);
                JSONArray results = jsonMovies.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject result = results.getJSONObject(i);
                    movies.add(new Movie(result));
                }
            } else {
                throw new Exception("No internet connection detected.");
            }
        }
        catch (Exception e){
            exception = e;
        }

        return movies;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> moviesResult) {
        super.onPostExecute(moviesResult);
        listener.onTaskComplete(moviesResult, exception);
    }
}
