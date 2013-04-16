package com.hansvn.plaktour;

import android.annotation.SuppressLint;
import android.app.Activity;
//import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

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
		
		init();

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
	
	public void init(){
		TextView detail_title = (TextView) findViewById(R.id.detail_title);
		detail_title.setText("Vilvoorde");
		
		TextView detail_pointsValue = (TextView) findViewById(R.id.detail_pointsValue);
		detail_pointsValue.setText("5");
		
		TextView detail_postersValue = (TextView) findViewById(R.id.detail_postersValue);
		detail_postersValue.setText("5");
		
		TextView detail_timeValue = (TextView) findViewById(R.id.detail_timeValue);
		detail_timeValue.setText("5min");
		
		TextView detail_lastActivityValue = (TextView) findViewById(R.id.detail_lastActivityValue);
		detail_lastActivityValue.setText("5");
		
		TextView detail_pointsDoneValue = (TextView) findViewById(R.id.detail_pointsDoneValue);
		detail_pointsDoneValue.setText("5");
	}
}
