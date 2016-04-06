package com.e_overhaul.android.moodtracker.object;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Bitmap;
import android.util.Log;

import com.e_overhaul.android.moodtracker.util.DataCache;

/**
 * Created by dcox on 3/31/16.
 * Initial Mood object.  Populated by APIHelper and used all over the place.
 */
public class Mood {
    private static final String LOG_TAG = Mood.class.getSimpleName();

    private String mMoodName;
    private Bitmap mMoodImage;
    private String mMoodTimeString;
    private Date mMoodDate;
    private double mLatitude;
    private double mLongitude;
// Create a Mood object with the required information
    public Mood(String moodName, String moodTime, double lat, double longitude) {
        mMoodName = moodName;
        mMoodTimeString = moodTime;
        mLatitude = lat;
        mLongitude = longitude;

// The mood images are created in MainActivity and stored in DataCache so we can access them from anywhere
        mMoodImage = DataCache.getInstance().getMoodImage(mMoodName);

//Attempt to parse the date.  Stored in PST
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        try {
            Log.v(LOG_TAG, "Parsing " + mMoodTimeString);
            mMoodDate = format.parse(mMoodTimeString);
            Log.v(LOG_TAG, "got " + mMoodDate.toString());
        } catch (ParseException e) {
            Log.e(LOG_TAG, e.getLocalizedMessage());
        }
    }
// Generic Getters
    public Bitmap getMoodImage() {
        return mMoodImage;
    }
    public String getMoodName() {
        return mMoodName;
    }
    public String getMoodTime() {
        return mMoodTimeString;
    }

// Get how many minutes, hours, or days this was posted.
    public String getTimeSince() {
        StringBuilder result = new StringBuilder();
        Date curDate = new Date();
        Log.v(LOG_TAG, "Current item time - " + mMoodDate.toString());
        Log.v(LOG_TAG, "Now Time - " + curDate.toString());
        long timeDiff = curDate.getTime() - mMoodDate.getTime();
        Log.v(LOG_TAG, "Time since - " + timeDiff);

        return result.toString();
    }
}
