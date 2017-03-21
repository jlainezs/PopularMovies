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

import com.example.android.popularmovies.pojos.MovieVideo;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.utilities.TMDBApi;

import java.util.ArrayList;


public class MovieVideosAdapter extends ArrayAdapter<MovieVideo> {
    private static final String LOG_TAG = MovieVideosAdapter.class.getName();

    public MovieVideosAdapter(Activity context, ArrayList<MovieVideo> movieVideos) {
        super(context, 0, movieVideos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MovieVideo movieVideo = getItem(position);
        TMDBApi api = new TMDBApi();

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_video_item, parent, false);
        }

        TextView txt = (TextView) convertView.findViewById(R.id.video_title);
        txt.setText(movieVideo.getName());

        return convertView;
    }
}
