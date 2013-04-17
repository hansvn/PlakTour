package com.hansvn.plaktour;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
//import android.content.Intent;

public class TourDetailActivity extends Activity {
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tour_detail);
		// Show the Up button in the action bar.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
		// Get the selected tour from the intent
	    Intent intent = getIntent();
	    int tourIndex = Integer.parseInt(intent.getStringExtra(MainActivity.SELECTED_TOUR));
	    
	    // Get the tour from the adapter
	    Tour tour = (Tour) MainActivity.tourListAdapter.getItem(tourIndex);
	    
	    // Display the data from the tour on the activity
	    displayData(tour);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void displayData(Tour tour){
		// Access the text view
		// next set the value
		TextView detail_title = (TextView) findViewById(R.id.detail_title);
		detail_title.setText(tour.getTitle());
		
		TextView detail_pointsValue = (TextView) findViewById(R.id.detail_pointsValue);
		detail_pointsValue.setText(tour.getLength());
		
		TextView detail_postersValue = (TextView) findViewById(R.id.detail_postersValue);
		detail_postersValue.setText(tour.getPosters());
		
		TextView detail_timeValue = (TextView) findViewById(R.id.detail_timeValue);
		detail_timeValue.setText(tour.getTime());
		
		TextView detail_lastActivityValue = (TextView) findViewById(R.id.detail_lastActivityValue);
		detail_lastActivityValue.setText(tour.getLastActivity());
		
		TextView detail_pointsDoneValue = (TextView) findViewById(R.id.detail_pointsDoneValue);
		detail_pointsDoneValue.setText(tour.getCompletedPoints());
	}
}
