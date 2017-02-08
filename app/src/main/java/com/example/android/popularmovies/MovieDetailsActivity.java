/*
 * Copyright (c) 2017 EConceptes. All rights reserved.
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 * http://www.econceptes.com
 */

package com.example.android.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.popularmovies.utilities.TMDBApi;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {
    private Movie movie;

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
                .into(moviePoster);
        TextView movieTitle = (TextView) findViewById(R.id.movie_title);
        movieTitle.setText(movie.getOriginal_title());
        TextView overview = (TextView) findViewById(R.id.movie_overview);
        overview.setText(movie.getOverview());
        TextView releaseDate = (TextView) findViewById(R.id.movie_releasedate);
        releaseDate.setText(movie.getRelease_date());
        RatingBar rating = (RatingBar) findViewById(R.id.movie_rating);
        rating.setRating(movie.getVote_average().floatValue());
    }
}
