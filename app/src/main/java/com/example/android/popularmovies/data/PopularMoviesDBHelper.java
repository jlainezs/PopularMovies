/*
 * Copyright (c) 2017 EConceptes. All rights reserved.
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 * http://www.econceptes.com
 */

package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jlainezs on 20/03/2017 for PopularMovies
 */

public class PopularMoviesDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "popularmovies.db";
    private static final int DATABASE_VERSION = 6;

    public PopularMoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVMOVIE_TABLE = "CREATE TABLE " +
                FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME + "(" +
                FavoriteMovieContract.FavoriteMovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_NAME_MOVIEID + " INTEGER NOT NULL," +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_NAME_TITLE + " TEXT NOT NULL," +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_NAME_OVERVIEW + " TEXT NOT NULL,"+
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_NAME_RATING + " FLOAT DEFAULT 0,"+
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_NAME_RELEASED + " TEXT NOT NULL,"+
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_NAME_POSTER + " TEXT"+
                ")";
        db.execSQL(SQL_CREATE_FAVMOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
