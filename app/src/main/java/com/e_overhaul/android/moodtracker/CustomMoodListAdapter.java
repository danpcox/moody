package com.e_overhaul.android.moodtracker;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.e_overhaul.android.moodtracker.object.Mood;

/**
 * Created by dcox on 4/6/16.
 */
public class CustomMoodListAdapter extends ArrayAdapter<Mood> {
    private final Activity _context;
    private final List<Mood> _moodList;
    public CustomMoodListAdapter (Activity context, List<Mood> moodList) {
        super(context, R.layout.mood_entry, moodList);
        _context = context;
        _moodList = moodList;
    }

    public View getView(int position, View view, ViewGroup parent) {
        Mood currentMood = _moodList.get(position);
// Get the row we want to display
        LayoutInflater inflater = _context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.mood_entry, null, true);
        TextView moodName = (TextView)rowView.findViewById(R.id.textViewMoodNameMoodListItem);
        TextView moodTime = (TextView)rowView.findViewById(R.id.textViewMoodTimeMoodListItem);
        ImageView imageView = (ImageView)rowView.findViewById(R.id.imageViewMoodListItem);
// Set the data in the list view
        moodName.setText(currentMood.getMoodName());
        moodTime.setText(currentMood.getTimeSince());
        imageView.setImageBitmap(currentMood.getMoodImage());

        return rowView;
    }
}
