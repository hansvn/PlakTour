package com.hansvn.plaktour;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;

public class SettingsActivity extends Activity {
	public static final String PLAKTOUR_PREFS = "PlakTourPrefs";
	private int DISTANCE_NEAR_POINT = 120;	//20 meters
	private int TIME_NEAR_POINT = 60*1000;  //one minute
	private boolean doChange = true;	//to set if texts are updated by user or not
	
	EditText distanceNearPointText = null;
	EditText timeNearPointText = null;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		// Show the Up button in the action bar.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.header_background));
		}
		
		loadPreferences();
		
		//link to the editTexts
		distanceNearPointText = (EditText) findViewById(R.id.settings_distanceToPointValue);
		timeNearPointText = (EditText) findViewById(R.id.settings_timeNearPointValue);
		
		//set the preferences in the editTexts
		setTexts();
		
		//add the changewatchers
		distanceNearPointText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(doChange) {
					if(s.length() > 0) {	//to avoid numberformatexceptions
						SettingsActivity.this.updatePreferences();
					}
				}
			}
		});
		timeNearPointText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(doChange) {
					if(s.length() > 0) {	//to avoid numberformatexceptions
						SettingsActivity.this.updatePreferences();
					}
				}
			}
		});		
	}
	
    //write the preferences to internal memory
    @Override
    protected void onStop() {
        super.onStop();
        
        //save the preferences
        savePreferences();

    }
    
    private void setTexts() {
		distanceNearPointText.setText(Integer.toString(DISTANCE_NEAR_POINT));
		timeNearPointText.setText(Integer.toString(TIME_NEAR_POINT/1000));
    }
    
    //the restore default button
    public void restoreDefault(View v){
    	DISTANCE_NEAR_POINT = 20;
    	TIME_NEAR_POINT = 60*1000;
    	doChange = false;	//prevent from outomatically updating
    	setTexts();
    	savePreferences();
    	doChange = true;	//reset so that values can be updated by user again
    }
    
    protected void updatePreferences() {
    	try {
        	DISTANCE_NEAR_POINT = Integer.parseInt(distanceNearPointText.getText().toString());
        	TIME_NEAR_POINT = Integer.parseInt(timeNearPointText.getText().toString())*1000;	//multiplied by 1000 to get milliseconds
    	}
    	catch (NumberFormatException nfe) {
    		Log.e("Settings_NumberFormatException", nfe.getMessage());
    	}
    	savePreferences();
    }
    
    private void loadPreferences() {
    	SharedPreferences settings = getSharedPreferences(PLAKTOUR_PREFS, MODE_PRIVATE); 
    	DISTANCE_NEAR_POINT = settings.getInt("DistanceNearPoint", 20);
    	TIME_NEAR_POINT = settings.getInt("TimeNearPoint", 60*1000);
    }
    
    private void savePreferences() {
        //set up the editor
        SharedPreferences settings = getSharedPreferences(PLAKTOUR_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("DistanceNearPoint", DISTANCE_NEAR_POINT);
        editor.putInt("TimeNearPoint", TIME_NEAR_POINT);

        // Commit the edits!
        editor.commit();
    }
    
    
    //header menu
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

}
