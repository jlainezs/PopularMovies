package com.example.android.popularmovies;

import android.content.Intent;
import android.net.Uri;
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
import com.example.android.popularmovies.data.FavoriteMovieContract;
import com.example.android.popularmovies.pojos.Movie;
import com.example.android.popularmovies.utilities.TMDBApi;

import java.net.ConnectException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Movie> movies = new ArrayList<>();
    private int showMovies = 0;
    private final int SHOW_POPULAR_MOVIES = 0;
    private final int SHOW_TOPRATED_MOVIES = 1;
    private final int SHOW_FAVORITE_MOVIES = 2;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String LIFECYCLE_SHOW_MOVIES = "showmovies";
    private static final String LIFECYCLE_GRID_POSITION = "gridposition";
    private static int gridPosition = -1;

    public class FetchMoviesTaskCompleteLister  implements AsyncTaskCompleteListener<ArrayList<Movie>> {
        @Override
        public void onTaskComplete(ArrayList<Movie> result, Exception exception) {
            if (movies.size() > 0) {
                movies.clear();
            }
            movies = result;
            GridView gridView = (GridView) findViewById(R.id.gridview);
            ((MoviesAdapter) gridView.getAdapter()).addAll(movies);

            if (exception != null) {
                if (exception.getClass() == ConnectException.class){
                    Toast.makeText(MainActivity.this, R.string.no_network_available, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, R.string.error_message_cant_load_movies, Toast.LENGTH_LONG).show();
                }
            }

            if (movies.size() > 0){
                gridView.setVisibility(View.VISIBLE);
                findViewById(R.id.no_content_to_show).setVisibility(View.GONE);
            } else {
                gridView.setVisibility(View.GONE);
                findViewById(R.id.no_content_to_show).setVisibility(View.VISIBLE);
            }

            if (gridPosition > -1){
                gridView.smoothScrollToPosition(gridPosition);
                gridPosition = -1;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(LIFECYCLE_SHOW_MOVIES)) {
                showMovies = savedInstanceState.getInt(LIFECYCLE_SHOW_MOVIES);
            }
            if (savedInstanceState.containsKey(LIFECYCLE_GRID_POSITION)){
                gridPosition = savedInstanceState.getInt(LIFECYCLE_GRID_POSITION);
            }
        }

        initializeGrid();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(LIFECYCLE_SHOW_MOVIES, showMovies);
        GridView gridView = (GridView) findViewById(R.id.gridview);
        int position = gridView.getFirstVisiblePosition();
        outState.putInt(LIFECYCLE_GRID_POSITION, position);
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

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (showMovies == SHOW_FAVORITE_MOVIES) {
                    Movie m = movies.get(position);
                    Uri uri = FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI
                            .buildUpon().appendPath(m.getId().toString()).build();
                    int deleted = getContentResolver().delete(uri, null, null);
                    if (deleted > 0) {
                        Toast.makeText(getApplicationContext(), R.string.favorite_removed, Toast.LENGTH_SHORT).show();
                        reloadList();
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.cannot_remove_favorite, Toast.LENGTH_SHORT).show();
                    }
                }

                return true;
            }
        });
    }

    private void populateMoviesList() {
        TMDBApi api = new TMDBApi();
        URL urlList;

        switch (showMovies){
            case SHOW_POPULAR_MOVIES:
                urlList = api.getPopularMoviesUrl();
                break;
            case SHOW_TOPRATED_MOVIES:
                urlList = api.getTopRatedMoviesUrl();
                break;
            default:
                urlList = null;
        }

        new MoviesFetcher(this, new FetchMoviesTaskCompleteLister()).execute(urlList);
    }

    private void reloadList() {
        populateMoviesList();
        GridView gv = (GridView) findViewById(R.id.gridview);
        MoviesAdapter mvsAdapt = (MoviesAdapter) gv.getAdapter();
        mvsAdapt.clear();
        mvsAdapt.notifyDataSetChanged();
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
                showMovies = SHOW_POPULAR_MOVIES;
                reloadList();
                return true;
            case R.id.menuSortTopRated:
                showMovies = SHOW_TOPRATED_MOVIES;
                reloadList();
                return true;
            case R.id.menuShowFavs:
                showMovies = SHOW_FAVORITE_MOVIES;
                reloadList();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
