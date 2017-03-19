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

import com.example.android.popularmovies.dataclasses.MovieReview;
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
public class MoviesReviewsFetcher extends AsyncTask<URL, Void, ArrayList<MovieReview>> {
    private static final String TAG = "FetchReviews";
    private Context context;
    private AsyncTaskCompleteListener<ArrayList<MovieReview>> listener;

    Exception exception = null;

    public MoviesReviewsFetcher(Context cts, AsyncTaskCompleteListener<ArrayList<MovieReview>> listener){
        this.context = cts;
        this.listener = listener;
    }

    @Override
    protected ArrayList<MovieReview> doInBackground(URL... urls) {
        URL searchUrl = urls[0];
        ArrayList<MovieReview> movieReviews = new ArrayList<>();

        try {
            if (Network.isOnline(context)) {
                String jsonMoviesReviewsStr = Network.getResponseFromHttpUrl(searchUrl);
                JSONObject jsonMoviesReviews = new JSONObject(jsonMoviesReviewsStr);
                JSONArray results = jsonMoviesReviews.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject review = results.getJSONObject(i);
                    movieReviews.add(new MovieReview(review));
                }
            } else {
                throw new ConnectException(context.getString(R.string.no_network_available));
            }
        }
        catch (Exception e){
            exception = e;
        }

        return movieReviews;
    }

    @Override
    protected void onPostExecute(ArrayList<MovieReview> result) {
        super.onPostExecute(result);
        listener.onTaskComplete(result, exception);
    }
}
