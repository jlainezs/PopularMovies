/*
 * Copyright (c) 2017 EConceptes. All rights reserved.
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 * http://www.econceptes.com
 */

package com.example.android.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by jlainezs on 20/03/2017 for PopularMovies
 */

public class FavoriteMovieContract {

    public static final String AUTHORITY = "com.example.android.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAVORITEMOVIES = "favoritemovies";

    private FavoriteMovieContract() {}

    public static class FavoriteMovieEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITEMOVIES).build();

        public static final String TABLE_NAME = "favmovies";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_MOVIEID = "movieid";
        public static final String COLUMN_NAME_OVERVIEW = "overview";
        public static final String COLUMN_NAME_RELEASED = "released";
        public static final String COLUMN_NAME_RATING = "rating";
        public static final String COLUMN_NAME_POSTER = "poster";
    }

}
