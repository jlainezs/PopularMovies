package com.example.android.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.android.popularmovies.adapters.MoviesAdapter;
import com.example.android.popularmovies.utilities.Network;
import com.example.android.popularmovies.utilities.TMDBApi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Movie> movies = new ArrayList<>();
    private int sortmovies = 0;

    /**
     * Fetches movies
     */
    public class MoviesFetcher extends AsyncTask<URL, Void, ArrayList<Movie>> {
        Exception exception = null;
        @Override
        protected ArrayList<Movie> doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            ArrayList<Movie> movies = new ArrayList<>();
            try {
                String jsonMoviesStr = Network.getResponseFromHttpUrl(searchUrl);
                JSONObject jsonMovies = new JSONObject(jsonMoviesStr);
                JSONArray results = jsonMovies.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject result = results.getJSONObject(i);
                    movies.add(new Movie(result));
                }
            }
            catch (Exception e){
                exception = e;
            }

            return movies;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> moviesResult) {
            if (movies.size() >0 ){
                movies.clear();
            }
            movies = moviesResult;
            GridView gridView = (GridView) findViewById(R.id.gridview);
            ((MoviesAdapter) gridView.getAdapter()).addAll(movies);

            if (exception != null)
            {
                Toast.makeText(MainActivity.this, R.string.error_message_cant_load_movies, Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeGrid();
    }

    private void initializeGrid() {
        populateMoviesList();
        GridView gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(new MoviesAdapter(this, movies));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
                Movie m = movies.get(position);
                intent.putExtra("movie", m);
                startActivity(intent);
            }
        });
    }

    private void populateMoviesList() {
        TMDBApi api = new TMDBApi();
        URL urlList;

        if (sortmovies == 0)
        {
            urlList = api.getPopularMoviesUrl();
        }
        else
        {
            urlList = api.getTopRatedMoviesUrl();
        }

        new MoviesFetcher().execute(urlList);
    }

    private void reloadList()
    {
        populateMoviesList();
        GridView gv = (GridView) findViewById(R.id.gridview);
        MoviesAdapter mvsAdapt = (MoviesAdapter) gv.getAdapter();
        mvsAdapt.clear();
        mvsAdapt.notifyDataSetChanged();
        //gv.setAdapter(mvsAdapt);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuSortMostPopular:
                sortmovies = 0;
                reloadList();
                return true;
            case R.id.menuSortTopRated:
                sortmovies = 1;
                reloadList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
