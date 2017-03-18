package com.example.android.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.async.AsyncTaskCompleteListener;
import com.example.android.popularmovies.async.MoviesFetcher;
import com.example.android.popularmovies.async.MoviesVideosFetcher;
import com.example.android.popularmovies.utilities.TMDBApi;
import com.squareup.picasso.Picasso;

import java.net.ConnectException;
import java.net.URL;
import java.util.ArrayList;

public class MovieDetailsActivity extends AppCompatActivity {
    private Movie movie;
    private ArrayList<MovieVideo> movieVideos = new ArrayList<>();

    public class FetchMoviesVideosTaskCompleteLister  implements AsyncTaskCompleteListener<ArrayList<MovieVideo>> {

        @Override
        public void onTaskComplete(ArrayList<MovieVideo> result, Exception exception) {
            if (movieVideos.size() > 0)
            {
                movieVideos.clear();
            }

            movieVideos = result;

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
        movie = (Movie) getIntent().getSerializableExtra("movie");
        TMDBApi api = new TMDBApi();

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

        populateMovieVideosList();
    }

    private void populateMovieVideosList() {
        TMDBApi api = new TMDBApi();
        URL urlList = api.getMovieVideos(movie.getId().toString());
        new MoviesVideosFetcher(this, new FetchMoviesVideosTaskCompleteLister()).execute(urlList);
    }
}
