package com.e_overhaul.android.moodtracker;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.e_overhaul.android.moodtracker.api.FetchMoodDescriptionList;
import com.e_overhaul.android.moodtracker.api.RecordMood;

public class ConfirmMood extends AppCompatActivity {
    private ArrayAdapter<String> _mMoodDescriptionListAdapter;
    private String _currentMood;
    SwipeRefreshLayout _mSwipeRefreshLayout;

    // Refresh the mood list data
    private void refresh(boolean reset) {
        if(reset) {
            FetchMoodDescriptionList.resetMoodDescriptionList();
        }
        FetchMoodDescriptionList fmdl = new FetchMoodDescriptionList(_mSwipeRefreshLayout, _mMoodDescriptionListAdapter);
        fmdl.execute();
    }

    public void recordNewMoodDescription(View v) {
        EditText et = (EditText)findViewById(R.id.newDescriptionEditText);
        String newDescription = et.getText().toString();
        if(newDescription.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please specify a description", Toast.LENGTH_SHORT).show();
            return;
        } else {
            RecordMood rm = new RecordMood();
            rm.execute(_currentMood, newDescription);
            Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        _currentMood = intent.getStringExtra(Intent.EXTRA_TEXT);
        setContentView(R.layout.activity_confirm_mood);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView tv = (TextView)findViewById(R.id.currentMoodTextView);
        tv.setText(_currentMood);
        //
        _mMoodDescriptionListAdapter = new ArrayAdapter<String>(this, R.layout.mood_description, R.id.mood_description_textview);

        // Initiate the "swipe up" refresh layout object
        _mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshMoodDescription);
        _mSwipeRefreshLayout.setOnRefreshListener(
            new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh () {
                    refresh(true);
                }
            });
        ListView lv = (ListView)findViewById(R.id.moodDescriptionListView);
        if(lv != null) {
            lv.setAdapter(_mMoodDescriptionListAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String currentStat = _mMoodDescriptionListAdapter.getItem(position);
                    Toast.makeText(getApplicationContext(), currentStat, Toast.LENGTH_LONG).show();
                    RecordMood rm = new RecordMood();
                    if(position > 0) {
                        rm.execute(_currentMood, currentStat);
                    } else {
                        rm.execute(_currentMood);
                    }
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            });
        }
        FetchMoodDescriptionList fmdl = new FetchMoodDescriptionList(null, _mMoodDescriptionListAdapter);
        fmdl.execute();
    }
}
