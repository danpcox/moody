package com.e_overhaul.android.moodtracker.util;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.location.Location;

/**
 * Created by dcox on 4/5/16.
 */
public class DataCache {
    private static DataCache _dc;
    private Map<String, Bitmap> _moodsToImages;
    private String _deviceID;
    private String _gender;
    private Location _currentLocation;
    DataCache() {
        _currentLocation = null;
        _moodsToImages = new HashMap<String, Bitmap>();
    }
    public static DataCache getInstance() {
        if(_dc == null) {
            _dc = new DataCache();
        }
        return _dc;
    }

    public void setLocation(Location currentLocation) {
        _currentLocation = currentLocation;
    }

    public Location getLocation() {
        return _currentLocation;
    }

    public double getLong() {
        if(_currentLocation != null) {
            return _currentLocation.getLongitude();
        }
        return 0;
    }

    public double getLat() {
        if(_currentLocation != null) {
            return _currentLocation.getLatitude();
        }
        return 0;
    }

    public void setDeviceID(String deviceID) {
        _deviceID = deviceID;
    }

    public String getDeviceID() {
        return _deviceID;
    }

    public void setGender(String gender) {
        _gender = gender;
    }

    public String getGender() {
        return _gender;
    }

    public void addMoodImage(String moodName, Bitmap bm) {
        _moodsToImages.put(moodName, bm);
    }

    public Bitmap getMoodImage(String moodName) {
        return _moodsToImages.get(moodName);
    }

}
