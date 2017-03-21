package com.example.android.popularmovies.async;

/**
 * Created by jlainezs on 11/02/2017 for PopularMovies
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.android.popularmovies.data.FavoriteMovieContract;
import com.example.android.popularmovies.data.PopularMoviesDBHelper;
import com.example.android.popularmovies.pojos.Movie;
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
public class MoviesFetcher extends AsyncTask<URL, Void, ArrayList<Movie>> {
    private static final String TAG = "FetchMovies";
    private Context context;
    private AsyncTaskCompleteListener<ArrayList<Movie>> listener;
    private SQLiteDatabase mDb;

    Exception exception = null;

    public MoviesFetcher(Context cts, AsyncTaskCompleteListener<ArrayList<Movie>> listener){
        this.context = cts;
        this.listener = listener;
        // Prepares the database
        PopularMoviesDBHelper dbHelper = new PopularMoviesDBHelper(cts);
        mDb = dbHelper.getWritableDatabase();
    }

    @Override
    protected ArrayList<Movie> doInBackground(URL... urls) {
        ArrayList<Movie> movies;

        if (urls[0] != null) {
            movies = getMoviesFromURL(urls[0]);
        } else {
            movies = getMoviesFromDatabase();
        }

        return movies;
    }

    @NonNull
    private ArrayList<Movie> getMoviesFromURL(URL url) {
        URL searchUrl = url;
        ArrayList<Movie> movies = new ArrayList<>();
        try {
            if (Network.isOnline(context)) {
                String jsonMoviesStr = Network.getResponseFromHttpUrl(searchUrl);
                JSONObject jsonMovies = new JSONObject(jsonMoviesStr);
                JSONArray results = jsonMovies.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject result = results.getJSONObject(i);
                    Movie movie = new Movie(result);
                    movies.add(movie);
                }
            } else {
                throw new ConnectException(context.getString(R.string.no_network_available));
            }
        }
        catch (Exception e){
            exception = e;
        }
        return movies;
    }

    public ArrayList<Movie> getMoviesFromDatabase() {
        ArrayList<Movie> movies = new ArrayList<>();
        Cursor csr = getAllFavorites();

        if (csr.moveToFirst())
        {
            do {
                Movie movie = new Movie();
                movie.setId(csr.getLong(csr.getColumnIndex(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_NAME_MOVIEID)));
                movie.setTitle(csr.getString(csr.getColumnIndex(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_NAME_TITLE)));
                movie.setRelease_date(csr.getString(csr.getColumnIndex(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_NAME_RELEASED)));
                movie.setPoster_path(csr.getString(csr.getColumnIndex(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_NAME_POSTER)));
                movie.setVote_average(csr.getDouble(csr.getColumnIndex(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_NAME_RATING)));
                movie.setOverview(csr.getString(csr.getColumnIndex(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_NAME_OVERVIEW)));
                movies.add(movie);
            }
            while (csr.moveToNext());
        }

        return movies;
    }


    /**
     * Gets all favorites
     *
     * @return Cursor
     */
    private Cursor getAllFavorites(){
        return mDb.query(
                FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_NAME_TITLE
        );
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> moviesResult) {
        super.onPostExecute(moviesResult);
        listener.onTaskComplete(moviesResult, exception);
    }
}
