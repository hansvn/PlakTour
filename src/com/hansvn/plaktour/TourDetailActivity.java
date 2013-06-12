package com.hansvn.plaktour;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
//import android.content.Intent;
import android.widget.Toast;

public class TourDetailActivity extends Activity {
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tour_detail);
		// Show the Up button in the action bar.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.header_background));
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
			case R.id.action_settings:
		        Intent Settings = new Intent(this, SettingsActivity.class);
		        this.startActivity(Settings);
		        return true;
		    case R.id.profile_icon:
		    	showProfileMenu(findViewById(R.id.profile_icon));
		    	return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void showProfileMenu(View v) {
		PopupMenu popup = new PopupMenu(this, v);
	    MenuInflater inflater = popup.getMenuInflater();
	    inflater.inflate(R.menu.profile_menu, popup.getMenu());
	    popup.show();
	}
	
	//actie van start tour button onclick staat in xml
	public void startTour(View view) {
		Toast.makeText(getApplicationContext(), "function not included yet (return to previous screen)", Toast.LENGTH_LONG).show();
	}
	
	public void displayData(Tour tour){
		// Access the text view
		// next set the value
		TextView detail_title = (TextView) findViewById(R.id.detail_title);
		detail_title.setText(tour.getTitle());
		
		TextView detail_pointsValue = (TextView) findViewById(R.id.detail_pointsValue);
		detail_pointsValue.setText(Integer.toString(tour.getLength()));
		
		TextView detail_postersValue = (TextView) findViewById(R.id.detail_postersValue);
		detail_postersValue.setText(Integer.toString(tour.getPosters()));
		
		TextView detail_timeValue = (TextView) findViewById(R.id.detail_timeValue);
		detail_timeValue.setText(tour.getTime());
		
		TextView detail_lastActivityValue = (TextView) findViewById(R.id.detail_lastActivityValue);
		detail_lastActivityValue.setText(tour.getLastActivity());
		
		TextView detail_pointsDoneValue = (TextView) findViewById(R.id.detail_pointsDoneValue);
		detail_pointsDoneValue.setText(Integer.toString(tour.getCompletedpointsNumber()));
	}
}
