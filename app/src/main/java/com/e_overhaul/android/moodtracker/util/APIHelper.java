package com.e_overhaul.android.moodtracker.util;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.e_overhaul.android.moodtracker.object.Mood;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dcox on 3/31/16.
 */
public class APIHelper {
    private static final String LOG_TAG = APIHelper.class.getSimpleName();

    public static List<Mood> getAvailableMoods(String jsonStr) throws JSONException {
        List<Mood> returnList = new ArrayList<Mood>();
        JSONObject result = new JSONObject(jsonStr);
        if(result.has("moods")) {
            JSONArray moodAry = result.getJSONArray("moods");
            for (int i = 0; i < moodAry.length(); i++) {
                JSONObject statObj = moodAry.getJSONObject(i);
                String moodName = statObj.getString("mood_name");
                String moodImage = statObj.getString("mood_time");
                double latitude = statObj.getDouble("latitude");
                double longitude = statObj.getDouble("longitude");
                returnList.add(new Mood(moodName, moodImage, latitude, longitude));
            }
        }
        Log.v(LOG_TAG, "Returning array of size: " + returnList.size());

        return returnList;
    }

    public static boolean successfulPost(String jsonStr) throws JSONException {
        JSONObject result = new JSONObject(jsonStr);
        if(result.has("success")) {
            return true;
        }
        return false;
    }
}
