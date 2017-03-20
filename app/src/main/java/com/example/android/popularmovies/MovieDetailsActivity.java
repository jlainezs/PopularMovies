package com.example.android.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.android.popularmovies.dataclasses.Movie;
import com.example.android.popularmovies.dataclasses.MovieReview;
import com.example.android.popularmovies.dataclasses.MovieVideo;
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
        movie = (Movie) getIntent().getSerializableExtra("movie");

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

        populateMovieReviewsList();
        ExpandableHeightListView reviewsList = (ExpandableHeightListView) findViewById(R.id.movie_reviews_list);
        reviewsList.setExpanded(true);
        reviewsList.setAdapter(new MovieReviewsAdapter(this, movieReviews));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void populateMovieReviewsList(){
        URL reviewsUrl = api.getMovieReviews(movie.getId().toString());
        new MoviesReviewsFetcher(this, new FetchMoviesReviewsTaskCompleteListener()).execute(reviewsUrl);
    }

    private void populateMovieVideosList() {
        URL videosUrl = api.getMovieVideos(movie.getId().toString());
        new MoviesVideosFetcher(this, new FetchMoviesVideosTaskCompleteLister()).execute(videosUrl);
    }

    /**
     *
     * @param listView
     * @link https://kennethflynn.wordpress.com/2012/09/12/putting-android-listviews-in-scrollviews/
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        if (adapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        totalHeight += listView.getPaddingTop() + listView.getPaddingBottom();

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
