/*
 * Copyright (c) 2017 EConceptes. All rights reserved.
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 * http://www.econceptes.com
 */

package com.example.android.popularmovies.data;

import android.provider.BaseColumns;

/**
 * Created by jlainezs on 20/03/2017 for PopularMovies
 */

public class FavoriteMovieContract {
    private FavoriteMovieContract() {}

    public static class FavoriteMovieEntry implements BaseColumns{
        public static final String TABLE_NAME = "favmovies";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_MOVIEID = "movieid";
        public static final String COLUMN_NAME_OVERVIEW = "overview";
        public static final String COLUMN_NAME_RELEASED = "released";
        public static final String COLUMN_NAME_RATING = "rating";
        public static final String COLUMN_NAME_POSTER = "poster";
    }

}
