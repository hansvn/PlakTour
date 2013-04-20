package com.hansvn.plaktour;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	public final static String SELECTED_TOUR = "com.hansvn.plaktour.TOUR";
	public static TourListAdapter tourListAdapter;
	private static int selectedTour;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//set data on listview
		final ListView toursListView = (ListView) findViewById(R.id.listViewTours);
		tourListAdapter = new TourListAdapter();
		toursListView.setAdapter(tourListAdapter);
				
		//final swipedetector to access it from the onItemClick function
		final SwipeDetector swipeDetector = new SwipeDetector();
		toursListView.setOnTouchListener(swipeDetector);
		
		toursListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long arg){
	            if (swipeDetector.swipeDetected()){
	                // do the onSwipe action 
	            	Intent intent = new Intent(view.getContext(), TourDetailActivity.class);
					intent.putExtra(SELECTED_TOUR, Integer.toString(position));
					startActivity(intent);
	            } else {
	                // do the onItemClick action
	            	//Toast.makeText(getApplicationContext(), "You clicked at item "+ position, Toast.LENGTH_LONG).show();
	            	view.setSelected(true);
	            	selectedTour = position;
	            	//een animatieke op achtergrond:
	            	/*
	            	LinearLayout listItem = (LinearLayout) view.findViewById(R.id.listItem_layout);
	            	listItem.setBackgroundDrawable(background)
	            	*/
	            	//een animatie-list maken om op achtergrond te zetten
	            	//animatie verwijderen van zodra de onclick stopt...
	            }
			}
		});
		
		toursListView.setOnItemLongClickListener(new OnItemLongClickListener() {
	        @Override
	        public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long arg) {
	            if (swipeDetector.swipeDetected()){
	                // do the onSwipe action 
	            	Toast.makeText(getApplicationContext(), "You Longswiped at item "+ position, Toast.LENGTH_LONG).show();
	            } else {
	                // do the onItemLongClick action
	            	Toast.makeText(getApplicationContext(), "You LongClicked at item "+ position, Toast.LENGTH_LONG).show();
	            }
	            return true;
	        }
	    });
	}
	
	//actie van start tour button onclick staat in xml
	public void startTour(View view) {
		//get te tour to pass:
		Tour tourToOpen = (Tour) tourListAdapter.getItem(selectedTour);
		//test message:
		Toast.makeText(getApplicationContext(), "the selected tour: "+ tourToOpen.getTitle(), Toast.LENGTH_LONG).show();
	}
	
	//actie van comments button
	public void viewTourComments(View view) {
		Toast.makeText(getApplicationContext(), "you clicked the comments button", Toast.LENGTH_LONG).show();
	}
	
	//actie van newTour button
	public void newTour(View view) {
		Toast.makeText(getApplicationContext(), "you clicked the new tour", Toast.LENGTH_LONG).show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
