package com.e_overhaul.android.moodtracker.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.e_overhaul.android.moodtracker.object.Mood;
import com.e_overhaul.android.moodtracker.util.APIHelper;
import com.e_overhaul.android.moodtracker.util.DataCache;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by dcox on 4/7/16.
 * Goal is to fetch a small map from google and display it to the user.
 */
public class FetchMap extends AsyncTask<String, Void, Bitmap> {
//https://maps.googleapis.com/maps/api/staticmap?zoom=11&size=300x280&center=37.3042,-122.0946&markers=color:green%7Clabel:F%7C37.3042,-122.0946
    private ImageView _imageView;

    public FetchMap(ImageView iv) {
        _imageView = iv;
    }

    private static final String LOG_TAG = FetchMoodList.class.getSimpleName();

    @Override
    protected Bitmap doInBackground (String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        Bitmap returnBitmap = null;
        String latitude = params[0];
        String longitude = params[1];
        String imageURL = "https://maps.googleapis.com/maps/api/staticmap?zoom=11&size=300x280&center=" + latitude + "," + longitude + "&markers=color:green%7Clabel:F%7C"
                        + latitude + "," + longitude;

        try {
            // Construct the URL for the stats query
            URL url = new URL(imageURL);
            // Create the request to /api/listRoutines.php
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            Log.v(LOG_TAG, "Got get URL - " + imageURL);
            InputStream in = urlConnection.getInputStream();

            if(in != null) {
                returnBitmap = BitmapFactory.decodeStream(in);
                if(returnBitmap != null) {
//                    saveImageToDisk(_mStat, returnBitmap);
                } else {
                    Log.e(LOG_TAG, "Crap, can't create bitmap " + imageURL);
                }
            } else {
                Log.e(LOG_TAG, "Crap, can't open image at " + imageURL);
            }
            return returnBitmap;
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        if(_imageView != null) {
            _imageView.setImageBitmap(result);
        }
    }

}
