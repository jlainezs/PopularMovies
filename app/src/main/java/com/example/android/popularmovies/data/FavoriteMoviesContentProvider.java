/*
 * Copyright (c) 2017 EConceptes. All rights reserved.
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 * http://www.econceptes.com
 */

package com.example.android.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.android.popularmovies.R;

/**
 * Created by jlainezs on 22/03/2017 for PopularMovies
 */

public class FavoriteMoviesContentProvider extends ContentProvider {
    private PopularMoviesDBHelper moviesDBHelper;
    private static final int FAVORITEMOVIES = 100;
    private static final int FAVORITEMOVIES_WITH_ID = 101;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(FavoriteMovieContract.AUTHORITY, FavoriteMovieContract.PATH_FAVORITEMOVIES, FAVORITEMOVIES);
        uriMatcher.addURI(FavoriteMovieContract.AUTHORITY, FavoriteMovieContract.PATH_FAVORITEMOVIES + "/#", FAVORITEMOVIES_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        moviesDBHelper = new PopularMoviesDBHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = moviesDBHelper.getReadableDatabase();
        int match =     sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match){
            case FAVORITEMOVIES:
                retCursor = db.query(FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME,
                    projection, selection, selectionArgs, null, null, sortOrder
                    );
                break;
            case FAVORITEMOVIES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                String mSelection = "_id=?";
                String[] mSelectionArgs = new String[]{id};
                retCursor = db.query(FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME,
                        projection, mSelection, mSelectionArgs, null, null, sortOrder
                );

                break;
            default:
                throw new UnsupportedOperationException(getContext().getString(R.string.unsupported_uri) + uri);
        }

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = moviesDBHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match){
            case FAVORITEMOVIES:
                long id = db.insert(FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI, id);
                } else {
                    throw new SQLException(getContext().getString(R.string.cannot_save_to_favorites));
                }
                break;
            default:
                throw new UnsupportedOperationException(getContext().getString(R.string.unsupported_uri) + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = moviesDBHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int result = 0;

        switch (match)
        {
            case FAVORITEMOVIES:
                // Remove all
                result = db.delete(FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME,null, null);
                break;
            case FAVORITEMOVIES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                String mSelection = FavoriteMovieContract.FavoriteMovieEntry.COLUMN_NAME_MOVIEID + "=?";
                String[] mSelectionArgs = new String[]{id};
                result = db.delete(FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME, mSelection, mSelectionArgs);
                break;
            default:
                throw new UnsupportedOperationException(getContext().getString(R.string.unsupported_uri) + uri);
        }

        return result;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
