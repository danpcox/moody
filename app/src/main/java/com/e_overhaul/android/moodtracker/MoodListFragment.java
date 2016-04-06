package com.e_overhaul.android.moodtracker;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoodListFragment extends Fragment {

    public MoodListFragment () {
    }
    String[] moods = {
        "Happy",
        "Sad",
        "Anxious"
    };
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mood_list, container, false);
        ListView lv = (ListView) rootView.findViewById(R.id.moodListListView);
        CustomMoodListAdapter customAdapter = new CustomMoodListAdapter(getActivity(), new ArrayList<Mood>());
        lv.setAdapter(customAdapter);

/*
        lv.setAdapter(new ArrayAdapter<String>(
                         getActivity(), R.layout.mood_entry, R.id.textViewMoodNameMoodListItem, moods
                      ));
*/
        return rootView;
    }
}
