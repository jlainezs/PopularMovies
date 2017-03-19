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

import com.example.android.popularmovies.MovieReview;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.utilities.TMDBApi;

import java.util.ArrayList;


public class MovieReviewsAdapter extends ArrayAdapter<MovieReview> {
    private static final String LOG_TAG = MovieReviewsAdapter.class.getName();

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
        txtContent.setText(movieReview.getContent());

        TextView txtUrl = (TextView) convertView.findViewById(R.id.movie_review_url);
        txtUrl.setText(movieReview.getUrl());

        return convertView;
    }
}
