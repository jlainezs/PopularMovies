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

package com.example.android.popularmovies.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.popularmovies.dataclasses.MovieReview;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.utilities.TMDBApi;

import java.util.ArrayList;


public class MovieReviewsAdapter extends ArrayAdapter<MovieReview> {
    private static final String LOG_TAG = MovieReviewsAdapter.class.getName();
    public static final int CONTENT_REVIEW_LENGTH = 200;

    public MovieReviewsAdapter(Activity context, ArrayList<MovieReview> movieReviews) {
        super(context, 0, movieReviews);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MovieReview movieReview = getItem(position);
        TMDBApi api = new TMDBApi();

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_review_item, parent, false);
        }

        TextView txtAuthor = (TextView) convertView.findViewById(R.id.movie_review_author);
        txtAuthor.setText(movieReview.getAuthor());

        TextView txtContent = (TextView) convertView.findViewById(R.id.movie_review_content);
        String review = movieReview.getContent();
        String reviewTitle = movieReview.getContentHeadline();

        if (reviewTitle.length() > 0)
        {
            review = reviewTitle;
        } else {

            if (review.length() > CONTENT_REVIEW_LENGTH) {
                review = review.substring(0, CONTENT_REVIEW_LENGTH) + "...";
            }
        }

        txtContent.setText(review);

        /*
        TextView txtUrl = (TextView) convertView.findViewById(R.id.movie_review_url);
        txtUrl.setText(movieReview.getUrl());
        */

        return convertView;
    }
}
