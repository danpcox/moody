package com.e_overhaul.android.moodtracker.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.e_overhaul.android.moodtracker.CustomMoodListAdapter;
import com.e_overhaul.android.moodtracker.object.Mood;
import com.e_overhaul.android.moodtracker.util.APIHelper;
import com.e_overhaul.android.moodtracker.util.DataCache;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by dcox on 3/31/16.
 */
public class FetchMoodDescriptionList extends AsyncTask<Void, Void, List<String>>{

    private SwipeRefreshLayout _mSwipeRefreshLayout;
    private static List<String> _mMoodDescriptionList;
    private ArrayAdapter<String> _mArrayAdapter;

    public FetchMoodDescriptionList (SwipeRefreshLayout refreshLayout, ArrayAdapter<String> arrayAdapter) {
        _mSwipeRefreshLayout = refreshLayout;
        _mArrayAdapter = arrayAdapter;
    }

    public static void resetMoodDescriptionList () {
        if(_mMoodDescriptionList != null) {
            Log.v(LOG_TAG, "Resetting list son!");
            _mMoodDescriptionList.clear();
        }
    }


    private static final String LOG_TAG = FetchMoodDescriptionList.class.getSimpleName();

    @Override
    protected List<String> doInBackground (Void... params) {

        if(_mMoodDescriptionList != null && !_mMoodDescriptionList.isEmpty()) {
            Log.v(LOG_TAG, "Returning cached Workout Routines");
            return _mMoodDescriptionList;
        } else {
            Log.v(LOG_TAG, "_mWorkoutRoutines is null, go get data");
        }
        List<String> returnList = new ArrayList<String>();

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonResult = null;
        String apiURI = "https://www.e-overhaul.com/Moods/api/listMoodDescriptions.php?user=" + DataCache.getInstance().getDeviceID();

        try {
            // Construct the URL for the stats query
            URL url = new URL(apiURI);
            // Create the request to /api/listRoutines.php
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            Log.v(LOG_TAG, "Got get URL - " + apiURI);

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
            Log.v(LOG_TAG, "jsonResult is - " + jsonResult);

            _mMoodDescriptionList = APIHelper.getMoodDescriptions(jsonResult);
            return _mMoodDescriptionList;

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
    protected void onPostExecute(List<String> data) {
        Log.v(LOG_TAG, "In onPostExecute about to set adapter");
        _mArrayAdapter.clear();
        for(int i=0; i<data.size(); i++) {
            _mArrayAdapter.add(data.get(i));
        }
        if(_mSwipeRefreshLayout != null) {
            _mSwipeRefreshLayout.setRefreshing(false);
        }
        super.onPostExecute(data);
    }



}
