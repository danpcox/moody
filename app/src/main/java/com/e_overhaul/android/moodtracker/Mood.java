package com.e_overhaul.android.moodtracker;

import android.graphics.Bitmap;
import android.provider.ContactsContract;

import com.e_overhaul.android.moodtracker.util.DataCache;

/**
 * Created by dcox on 3/31/16.
 */
public class Mood {
    private String mMoodName;
    private Bitmap mMoodImage;
    private String mMoodTime;
    private double mLatitude;
    private double mLongitude;
    public Mood(String moodName, String moodTime, double lat, double longitude) {
        mMoodName = moodName;
        mMoodTime = moodTime;
        mMoodImage = DataCache.getInstance().getMoodImage(mMoodName);
        mLatitude = lat;
        mLongitude = longitude;
    }

    public Bitmap getMoodImage() {
        return mMoodImage;
    }
    public String getMoodName() {
        return mMoodName;
    }
    public String getMoodTime() {
        return mMoodTime;
    }
}
