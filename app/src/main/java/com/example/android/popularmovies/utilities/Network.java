
package com.example.android.popularmovies.utilities;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class Network {
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = null;
        StringBuilder resultStrBuilder = new StringBuilder();
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();
            BufferedReader inp = new BufferedReader(new InputStreamReader(in));
            String inputStr;

            while ((inputStr = inp.readLine()) != null){
                resultStrBuilder.append(inputStr);
            }

        } catch (IOException e){
            resultStrBuilder.setLength(0);    // clear the string builder
            throw e;
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return resultStrBuilder.toString();
    }
}
