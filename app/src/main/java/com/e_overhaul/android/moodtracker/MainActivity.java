package com.e_overhaul.android.moodtracker;

import java.util.zip.Inflater;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.e_overhaul.android.moodtracker.api.FetchMoodDescriptionList;
import com.e_overhaul.android.moodtracker.api.FetchMoodList;
import com.e_overhaul.android.moodtracker.api.RecordMood;
import com.e_overhaul.android.moodtracker.util.DataCache;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String gender = sp.getString("gender", "");
// If first run, show gender choice screen
        if (gender.isEmpty()) {
            setContentView(R.layout.first_run_screen);
        } else {
            setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
        }
// Populate the DataCache with info.
// Store the location in the DataCache layer (used in all API calls)
        DataCache.getInstance().setLocation(getLastBestLocation());
        String android_id = Settings.Secure.getString(this.getContentResolver(),
            Settings.Secure.ANDROID_ID);
// Store Device ID and Gender (used in all API calls)
        DataCache.getInstance().setDeviceID(android_id);
        DataCache.getInstance().setGender(gender);
// Set up the mood images map of strings --> drawables
        setMoodImages();
// Start the notification service
        startNotifications();

    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_mood_list) {
            Intent intent = new Intent(this, MoodList.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void recordMood (View v) {
        RecordMood rm = new RecordMood();
        String intentExtra = "";
        switch (v.getId()) {
        case R.id.imageButtonAngry:
            intentExtra = "angry";
            break;
        case R.id.imageButtonAnxious:
            intentExtra = "anxious";
            break;
        case R.id.imageButtonFart:
            intentExtra = "farty";
            break;
        case R.id.imageButtonDepressed:
            intentExtra = "depressed";
            break;
        case R.id.imageButtonExcited:
            intentExtra = "excited";
            break;
        case R.id.imageButtonHappy:
            intentExtra = "happy";
            break;
        case R.id.imageButtonnervous:
            intentExtra = "nervous";
            break;
        case R.id.imageButtonSad:
            intentExtra = "sad";
            break;
        default:
            Toast.makeText(getApplicationContext(), "Invalid record?", Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent(this, ConfirmMood.class).putExtra(Intent.EXTRA_TEXT, intentExtra);
        startActivity(intent);
    }

    public void login(View v) {
        String gender = "Male";
        if(v.getId() == R.id.imageButtonFemale) {
            gender = "Female";
        }
        SharedPreferences sp = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("gender", gender);
        editor.apply();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private Location getLastBestLocation () {
        LocationManager locationManager = (LocationManager)
            getSystemService(Context.LOCATION_SERVICE);

        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location locationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(locationGPS != null) {
                return locationGPS;
            }
            if(locationNetwork != null) {
                return locationNetwork;
            }
            throw new Error("No location could be found, but we had permissions!");
        } catch (Error e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        return null;
    }
    private void setMoodImages() {
        DataCache dc = DataCache.getInstance();
        dc.addMoodImage("angry", BitmapFactory.decodeResource(getResources(), R.drawable.angry));
        dc.addMoodImage("anxious", BitmapFactory.decodeResource(getResources(), R.drawable.anxious));
        dc.addMoodImage("depressed", BitmapFactory.decodeResource(getResources(), R.drawable.depressed));
        dc.addMoodImage("excited", BitmapFactory.decodeResource(getResources(), R.drawable.excited));
        dc.addMoodImage("happy", BitmapFactory.decodeResource(getResources(), R.drawable.happy));
        dc.addMoodImage("nervous", BitmapFactory.decodeResource(getResources(), R.drawable.nervous));
        dc.addMoodImage("sad", BitmapFactory.decodeResource(getResources(), R.drawable.sad));
        dc.addMoodImage("farty", BitmapFactory.decodeResource(getResources(), R.drawable.fart));
    }
    private void startNotifications() {
        Intent notificationIntent = new Intent(getApplicationContext(), ShowNotificationService.class);
        PendingIntent contentIntent = PendingIntent.getService(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.cancel(contentIntent);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (AlarmManager.INTERVAL_FIFTEEN_MINUTES / 15), (AlarmManager.INTERVAL_FIFTEEN_MINUTES / 15), contentIntent);
    }
}
