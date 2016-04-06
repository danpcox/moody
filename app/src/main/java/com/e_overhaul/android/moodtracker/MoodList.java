package com.e_overhaul.android.moodtracker;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.e_overhaul.android.moodtracker.api.FetchMoodList;

public class MoodList extends AppCompatActivity {
    SwipeRefreshLayout mSwipeRefreshLayout;
    CustomMoodListAdapter mCustomAdapter;



    private void refresh() {
        FetchMoodList.resetMoodList();
        FetchMoodList fml = new FetchMoodList(mCustomAdapter, mSwipeRefreshLayout);
        fml.execute();
        Toast.makeText(getApplicationContext(), "Refresh", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mCustomAdapter = new CustomMoodListAdapter(this, new ArrayList<Mood>());
        ListView lv = (ListView) this.findViewById(R.id.moodListListView);
        lv.setAdapter(mCustomAdapter);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(
            new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh () {
                    refresh();
                }
            });
        refresh();


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
        if(id == R.id.action_refresh) {
            refresh();
        }

        return super.onOptionsItemSelected(item);
    }
}
