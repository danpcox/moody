package com.e_overhaul.android.moodtracker;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.e_overhaul.android.moodtracker.api.FetchMoodList;
import com.e_overhaul.android.moodtracker.object.Mood;


// This activity displays a list of moods sorted chronologically.
// The user can refresh the list by swiping down at the top of the activity :-)
public class MoodList extends AppCompatActivity {

    private static final String LOG_TAG = MoodList.class.getSimpleName();

// Needed for the FetchMoodList AsyncTask
    SwipeRefreshLayout mSwipeRefreshLayout;
    CustomMoodListAdapter mCustomAdapter;

// Refresh the mood list data
    private void refresh(boolean reset) {
        if(reset) {
            FetchMoodList.resetMoodList();
        }
        FetchMoodList fml = new FetchMoodList(mCustomAdapter, mSwipeRefreshLayout);
        fml.execute();
        Toast.makeText(getApplicationContext(), "Refresh", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onCreate (Bundle savedInstanceState) {
// Set up the main view + toolbar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
// Create our custom adapter for moods and store it for callbacks
        mCustomAdapter = new CustomMoodListAdapter(this, new ArrayList<Mood>());
        ListView lv = (ListView) this.findViewById(R.id.moodListListView);
        lv.setAdapter(mCustomAdapter);
// Initiate the "swipe up" refresh layout object
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(
            new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh () {
                    refresh(true);
                }
            });
// Refresh the data the onCreate
        refresh(false);
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mood_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
