package com.e_overhaul.android.moodtracker.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;


import com.e_overhaul.android.moodtracker.CustomMoodListAdapter;
import com.e_overhaul.android.moodtracker.object.Mood;
import com.e_overhaul.android.moodtracker.util.APIHelper;
import com.e_overhaul.android.moodtracker.util.DataCache;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by dcox on 3/31/16.
 */
public class FetchMoodList extends AsyncTask<List<String>, Void, List<Mood>>{

    private CustomMoodListAdapter _mMoodListAdapter;
    private SwipeRefreshLayout _mSwipeRefreshLayout;
    private static List<Mood> _mMoodList;

    public FetchMoodList(CustomMoodListAdapter adapter, SwipeRefreshLayout refreshLayout) {
        _mMoodListAdapter = adapter;
        _mSwipeRefreshLayout = refreshLayout;
    }

    public static void resetMoodList () {
        if(_mMoodList != null) {
            Log.v(LOG_TAG, "Resetting list son!");
            _mMoodList.clear();
        }
    }


    private static final String LOG_TAG = FetchMoodList.class.getSimpleName();

    @Override
    protected List<Mood> doInBackground (List<String>... params) {
        if(_mMoodList != null && !_mMoodList.isEmpty()) {
            Log.v(LOG_TAG, "Returning cached Workout Routines");
            return _mMoodList;
        } else {
            Log.v(LOG_TAG, "_mWorkoutRoutines is null, go get data");
        }
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonResult = null;
        int daysToLookBack = 25;
        String apiURI = "https://www.e-overhaul.com/Moods/api/listMoods.php?user=" + DataCache.getInstance().getDeviceID();

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
            _mMoodList = APIHelper.getAvailableMoods(jsonResult);
            return _mMoodList;

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
    protected void onPostExecute(List<Mood> data) {
        Log.v(LOG_TAG, "In onPostExecute about to set adapter");
        _mMoodListAdapter.clear();
        for(int i=0; i<data.size(); i++) {
            _mMoodListAdapter.add(data.get(i));
        }
        _mSwipeRefreshLayout.setRefreshing(false);
        super.onPostExecute(data);
    }



}
