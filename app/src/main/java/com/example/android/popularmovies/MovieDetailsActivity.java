package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.adapters.MovieReviewsAdapter;
import com.example.android.popularmovies.adapters.MovieVideosAdapter;
import com.example.android.popularmovies.async.AsyncTaskCompleteListener;
import com.example.android.popularmovies.async.MoviesReviewsFetcher;
import com.example.android.popularmovies.async.MoviesVideosFetcher;
import com.example.android.popularmovies.data.FavoriteMovieContract;
import com.example.android.popularmovies.data.PopularMoviesDBHelper;
import com.example.android.popularmovies.pojos.Movie;
import com.example.android.popularmovies.pojos.MovieReview;
import com.example.android.popularmovies.pojos.MovieVideo;
import com.example.android.popularmovies.utilities.TMDBApi;
import com.example.android.popularmovies.views.ExpandableHeightListView;
import com.squareup.picasso.Picasso;

import java.net.ConnectException;
import java.net.URL;
import java.util.ArrayList;

public class MovieDetailsActivity extends AppCompatActivity {
    private Movie movie;
    private ArrayList<MovieVideo> movieVideos = new ArrayList<>();
    private ArrayList<MovieReview> movieReviews = new ArrayList<>();
    private TMDBApi api = new TMDBApi();
    private static final String TAG = MovieDetailsActivity.class.getSimpleName();
    private static final String ACTIVITY_MOVIE = "movie";
    private SQLiteDatabase mDb;

    public class FetchMoviesVideosTaskCompleteLister  implements AsyncTaskCompleteListener<ArrayList<MovieVideo>> {

        @Override
        public void onTaskComplete(ArrayList<MovieVideo> result, Exception exception) {
            if (movieVideos.size() > 0)
            {
                movieVideos.clear();
            }

            movieVideos = result;
            ListView listView = (ListView) findViewById(R.id.movie_videos_list);
            MovieVideosAdapter adapter = (MovieVideosAdapter) listView.getAdapter();
            adapter.addAll(movieVideos);
            // setListViewHeightBasedOnChildren(listView);

            if (exception != null) {
                if (exception.getClass() == ConnectException.class){
                    Toast.makeText(MovieDetailsActivity.this, R.string.no_network_available, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MovieDetailsActivity.this, R.string.error_message_cant_load_movies, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public class FetchMoviesReviewsTaskCompleteListener implements AsyncTaskCompleteListener<ArrayList<MovieReview>>{
        @Override
        public void onTaskComplete(ArrayList<MovieReview> result, Exception exception)
        {
            if (movieReviews.size() > 0)
            {
                movieReviews.clear();
            }

            movieReviews = result;
            ListView listView = (ListView) findViewById(R.id.movie_reviews_list);
            MovieReviewsAdapter adapter = (MovieReviewsAdapter) listView.getAdapter();
            adapter.addAll(movieReviews);
            TextView txtReviewsTitle = (TextView) findViewById(R.id.movie_reviews_title);

            if (movieReviews.size() == 0)
            {
                txtReviewsTitle.setVisibility(View.GONE);
            } else {
                String ttl = txtReviewsTitle.getText() + " (" + String.valueOf(movieReviews.size()) + ")";
                txtReviewsTitle.setText(ttl);
            }

            //setListViewHeightBasedOnChildren(listView);

            if (exception != null) {
                if (exception.getClass() == ConnectException.class){
                    Toast.makeText(MovieDetailsActivity.this, R.string.no_network_available, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MovieDetailsActivity.this, R.string.error_message_cant_load_movies, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Intent movieIntent = getIntent();
        if (movieIntent != null) {
            if (movieIntent.hasExtra("movie")) {
                movie = (Movie) getIntent().getSerializableExtra("movie");
            }
        }

        ImageView moviePoster = (ImageView) findViewById(R.id.movie_poster);
        String imageUrl = api.getMovieImageUrl(movie.getPoster_path()).toString();
        Picasso.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.dummy)
                .error(R.drawable.dummy)
                .into(moviePoster);
        TextView movieTitle = (TextView) findViewById(R.id.movie_title);
        movieTitle.setText(movie.getOriginal_title());
        TextView overview = (TextView) findViewById(R.id.movie_overview);
        overview.setText(movie.getOverview());
        TextView releaseDate = (TextView) findViewById(R.id.movie_releasedate);
        releaseDate.setText(movie.getRelease_date());
        RatingBar rating = (RatingBar) findViewById(R.id.movie_rating);
        rating.setRating(movie.getVote_average().floatValue());

        // Prepare the videos list
        populateMovieVideosList();
        ExpandableHeightListView videosList = (ExpandableHeightListView) findViewById(R.id.movie_videos_list);
        videosList.setExpanded(true);
        videosList.setAdapter(new MovieVideosAdapter(this, movieVideos));
        videosList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieVideo video = movieVideos.get(position);

                try {
                    String newVideoPath = video.getVideoPath().toString();
                    if (newVideoPath != null) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(newVideoPath));
                        startActivity(intent);
                    } else {
                        Toast.makeText(MovieDetailsActivity.this, "Can't show video: unsupported platform", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(MovieDetailsActivity.this, "Can't show this video now!", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Prepare the reviews list
        populateMovieReviewsList();
        ExpandableHeightListView reviewsList = (ExpandableHeightListView) findViewById(R.id.movie_reviews_list);
        reviewsList.setExpanded(true);
        reviewsList.setAdapter(new MovieReviewsAdapter(this, movieReviews));

        PopularMoviesDBHelper dbHelper = new PopularMoviesDBHelper(this);
        mDb = dbHelper.getWritableDatabase();

        // Sets the button to add favorite items
        ImageView favImage = (ImageView) this.findViewById(R.id.add_to_fav);
        favImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ContentValues cv = new ContentValues();
                    cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_NAME_MOVIEID, movie.getId());
                    cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_NAME_TITLE, movie.getTitle());
                    cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_NAME_OVERVIEW, movie.getOverview());
                    cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_NAME_POSTER, movie.getPoster_path());
                    cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_NAME_RATING, movie.getVote_average());
                    cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_NAME_RELEASED, movie.getRelease_date());
                    // mDb.insert(FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME, null, cv);
                    Uri uri = getContentResolver().insert(FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI, cv);
                    if (uri != null) {
                        Toast.makeText(v.getContext(), R.string.saved_to_favorites, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(v.getContext(), R.string.cannot_save_to_favorites, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    private void populateMovieReviewsList(){
        URL reviewsUrl = api.getMovieReviews(movie.getId().toString());
        new MoviesReviewsFetcher(this, new FetchMoviesReviewsTaskCompleteListener()).execute(reviewsUrl);
    }

    private void populateMovieVideosList() {
        URL videosUrl = api.getMovieVideos(movie.getId().toString());
        new MoviesVideosFetcher(this, new FetchMoviesVideosTaskCompleteLister()).execute(videosUrl);
    }
}
