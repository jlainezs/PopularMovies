package com.example.android.popularmovies.utilities;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by jlainezs on 06/02/2017 for PopularMovies
 */

public class TMDBApi {
    private final static String THEMOVIEDB_API_BASE_URL = "https://api.themoviedb.org/3";
    private final static String THEMOVIEDB_IMAGES_BASE_URL = "http://image.tmdb.org/t/p";
    // "w92", "w154", "w185", "w342", "w500", "w780"
    private final static String THEMOVIEDB_IMAGES_SIZE = "w500";
    private final static String THEMOVIEDB_API_KEY = "__YOUR_API_KEY_HERE__";

    /**
     * Gets the popular movies JSON url
     *
     * @return URL
     */
    public URL getPopularMoviesUrl()
    {
        Uri builtUri = Uri.parse(THEMOVIEDB_API_BASE_URL).buildUpon()
                .appendPath("movie")
                .appendPath("popular")
                .appendQueryParameter("api_key", THEMOVIEDB_API_KEY)
                .build();
        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public URL getTopRatedMoviesUrl()
    {
        Uri builtUri = Uri.parse(THEMOVIEDB_API_BASE_URL).buildUpon()
                        .appendPath("movie")
                        .appendPath("top_rated")
                .appendQueryParameter("api_key", THEMOVIEDB_API_KEY)
                .build();
        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public URL getMovieVideos(String movieId)
    {
        Uri builtUri = Uri.parse(THEMOVIEDB_API_BASE_URL).buildUpon()
                .appendPath("movie")
                .appendPath(movieId)
                .appendPath("videos")
                .appendQueryParameter("api_key", THEMOVIEDB_API_KEY)
                .build();
        URL  url = null;

        try{
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public URL getMovieReviews(String movieId)
    {
        Uri builtUri = Uri.parse(THEMOVIEDB_API_BASE_URL).buildUpon()
                .appendPath("movie")
                .appendPath(movieId)
                .appendPath("reviews")
                .appendQueryParameter("api_key", THEMOVIEDB_API_KEY)
                .build();
        URL  url = null;

        try{
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * Gets an image URL
     *
     * @param imageName Name of the image file
     * @return URL
     */
    public URL getMovieImageUrl(String imageName)
    {
        Uri imageUri = Uri.parse(THEMOVIEDB_IMAGES_BASE_URL).buildUpon()
                .appendPath(THEMOVIEDB_IMAGES_SIZE)
                .appendPath(imageName.replace("/", ""))
                .build();
        URL url = null;

        try {
            url = new URL(imageUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }
}
