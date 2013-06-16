package com.hansvn.plaktour;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

public class MainActivity extends Activity {
	public final static String SELECTED_TOUR = "com.hansvn.plaktour.TOUR";
	public static TourListAdapter tourListAdapter;
	private static int selectedTour = -1;
	public static String internet = ""; //om te kijken of internet aan staat of niet
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.header_background));
		}
		
		//test om te kijken of internet werkt
		ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
	        // fetch data
			internet = "internet";
			//*************************Toast.makeText(getApplicationContext(), "internet ok", Toast.LENGTH_LONG).show();
			
			//check for unupdated internet points first
			//todo: load local data and update undone points
			//then: clear all and renew list with online one.
	    } else {
	        // fetch data from local store
	    	internet = "local";
	    	Toast.makeText(getApplicationContext(), "internet not ok", Toast.LENGTH_LONG).show();
	    }
		//set to global variable:
		((PlakTourApplication) this.getApplication()).setInternet(internet);
		//einde internet test
		
		//set data on listview
		if( savedInstanceState != null ) {
			//load the arraylist from savedinstance, must be parcelable first
		}
		else {		
			final ListView toursListView = (ListView) findViewById(R.id.listViewTours);
			tourListAdapter = new TourListAdapter(internet, getApplicationContext());
			toursListView.setAdapter(tourListAdapter);
			tourListAdapter.notifyDataSetChanged();
					
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
		//end else onSavedInstance
		
	}
	
	@Override
	protected void onStart() {
        super.onStart();

	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//save the arraylist from the tours, must be parcelable first.
	}
	
	//actie van start tour button onclick staat in xml
	public void startTour(View view) {
		//get te tour to pass:
		//Tour tourToOpen = (Tour) tourListAdapter.getItem(selectedTour);
		//test message:
		//Toast.makeText(getApplicationContext(), "the selected tour: "+ tourToOpen.getTitle(), Toast.LENGTH_LONG).show();
		
		if(!(internet == "internet")){
			Toast.makeText(getApplicationContext(), "You Can't start tours without internet (yet)", Toast.LENGTH_LONG).show();
			//de markeroptions worden nog niet uit de storage gelezen...
		}
		else
		{
			Intent intent = new Intent(view.getContext(), TourMapActivity.class);
			if(selectedTour == -1){
				Toast.makeText(getApplicationContext(), "Please select a tour first...", Toast.LENGTH_LONG).show();
			}
			else {
				intent.putExtra(SELECTED_TOUR, Integer.toString(selectedTour));
				startActivity(intent);
			}
		}
	}
	
	//actie van comments button
	public void viewTourComments(View v) {
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
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {	    
	    switch (item.getItemId()) {
	    case R.id.action_settings:
	        Intent Settings = new Intent(this, SettingsActivity.class);
	        this.startActivity(Settings);
	        return true;
	    case R.id.profile_icon:
	    	showProfileMenu(findViewById(R.id.profile_icon));
	    	return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	public void showProfileMenu(View v) {
		PopupMenu popup = new PopupMenu(this, v);
	    MenuInflater inflater = popup.getMenuInflater();
	    inflater.inflate(R.menu.profile_menu, popup.getMenu());
	    popup.show();
	}

}
