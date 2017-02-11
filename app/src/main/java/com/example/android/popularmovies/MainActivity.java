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
import com.example.android.popularmovies.async.AsyncTaskCompleteListener;
import com.example.android.popularmovies.async.MoviesFetcher;
import com.example.android.popularmovies.utilities.Network;
import com.example.android.popularmovies.utilities.TMDBApi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Movie> movies = new ArrayList<>();
    private int sortmovies = 0;

    public class FetchMoviesTaskCompleteLister  implements AsyncTaskCompleteListener<ArrayList<Movie>> {
        @Override
        public void onTaskComplete(ArrayList<Movie> result, Exception exception) {
            if (movies.size() > 0) {
                movies.clear();
            }
            movies = result;
            GridView gridView = (GridView) findViewById(R.id.gridview);
            ((MoviesAdapter) gridView.getAdapter()).addAll(movies);

            if (exception.getClass() == ConnectException.class){
                Toast.makeText(MainActivity.this, R.string.no_network_available, Toast.LENGTH_LONG).show();
            } else if (exception != null) {
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

        new MoviesFetcher(this, new FetchMoviesTaskCompleteLister()).execute(urlList);
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
