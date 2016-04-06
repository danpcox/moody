package com.e_overhaul.android.moodtracker.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.e_overhaul.android.moodtracker.util.APIHelper;
import com.e_overhaul.android.moodtracker.util.DataCache;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by dcox on 3/31/16.
 */
public class RecordMood extends AsyncTask<String, Void, Boolean>{


    private static final String LOG_TAG = RecordMood.class.getSimpleName();

    @Override
    protected Boolean doInBackground (String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonResult = null;
        final String MOOD_PARAM = "mood";
        final String USER_PARAM = "user";
        final String LAT_PARAM = "lat";
        final String LONG_PARAM = "long";
        final String GENDER_PARAM = "gender";

        String user = DataCache.getInstance().getDeviceID();
        String lat = Double.valueOf(DataCache.getInstance().getLat()).toString();
        String longitude = Double.valueOf(DataCache.getInstance().getLong()).toString();
        String gender = DataCache.getInstance().getGender();
        String apiURI = "https://www.e-overhaul.com/Moods/api/addMood.php?";

        try {
            // Construct the URL for the mood query
            Uri builtUri = Uri.parse(apiURI).buildUpon()
                              .appendQueryParameter(MOOD_PARAM, params[0])
                              .appendQueryParameter(USER_PARAM, user)
                              .appendQueryParameter(LAT_PARAM, lat)
                              .appendQueryParameter(LONG_PARAM, longitude)
                              .appendQueryParameter(GENDER_PARAM, gender)
                              .build();
            URL url = new URL(builtUri.toString());
            // Create the request to /api/addMood.php
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            Log.v(LOG_TAG, "Got get URL - " + builtUri.toString());

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line = null;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            jsonResult = buffer.toString();
            Log.v(LOG_TAG, "JSON Result: " + jsonResult);
            return APIHelper.successfulPost(jsonResult);

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
    protected void onPostExecute(Boolean data) {
        Log.v(LOG_TAG, "In onPostExecute about to say " + data);
        if(data == false) {

        }
        super.onPostExecute(data);
    }
}
