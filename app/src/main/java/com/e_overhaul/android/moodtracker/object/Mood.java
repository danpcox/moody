package com.e_overhaul.android.moodtracker.object;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

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
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            mMoodDate = format.parse(mMoodTimeString);
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
        long timeDiff = curDate.getTime() - mMoodDate.getTime();
        timeDiff = timeDiff/1000;
        if(timeDiff < 3600) {
            result.append(timeDiff/60 + " minutes ago");
        } else if(timeDiff >=3600 && timeDiff < 24*3600) {
            result.append(timeDiff/3600 + " hours Ago");
        } else {
            result.append(timeDiff/(24*3600) + " days Ago");
        }
        return result.toString();
    }
}
