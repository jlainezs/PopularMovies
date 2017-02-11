package com.example.android.popularmovies.async;

/**
 *
 * @link http://www.jameselsey.co.uk/blogs/techblog/extracting-out-your-asynctasks-into-separate-classes-makes-your-code-cleaner/
 * @param <T>
 */
public interface AsyncTaskCompleteListener<T> {
    public void onTaskComplete(T result, Exception exception);
}
