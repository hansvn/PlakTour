package com.hansvn.plaktour;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class TourMapActivity extends FragmentActivity {
	private final String TAG = getClass().getSimpleName();
	private GoogleMap mMap;
	private Tour mTour;
	private LocationManager locManager;
	private Location loc;

	@SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_map);
        // Show the Up button in the action bar.
 		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
 			getActionBar().setDisplayHomeAsUpEnabled(true);
 		}
 		
		// Get the selected tour from the intent
	    Intent intent = getIntent();
	    int tourIndex = Integer.parseInt(intent.getStringExtra(MainActivity.SELECTED_TOUR));
	    
	    //Get the tour
	    mTour = (Tour) MainActivity.tourListAdapter.getItem(tourIndex);   
 		
	    //Use the location manager through GPS
	    locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    String provider = locManager.getBestProvider(new Criteria(), false);
	    provider = locManager.GPS_PROVIDER;
	    locManager.requestLocationUpdates(provider, 0, 0, listener);
	    
	    //Get current location
	    Location location = locManager.getLastKnownLocation(provider);
	    
	    //Log.i("Location", location.toString());
	    
 		//Access the map
 		setUpMapIfNeeded();
 		
    }
	
    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }
    
	private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.tourMap))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
                Log.i("Map Status", "map created");
            }
        }
    }
	
    private void setUpMap() {
    	mMap.addMarker(new MarkerOptions()
    		.position(new LatLng(0, 0))
    		.title("Hello world"));
    }
    
    private LocationListener listener = new LocationListener() {

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onLocationChanged(Location location) {
			Log.e(TAG, "location update : " + location);
			loc = location;
			locManager.removeUpdates(listener);
		}
	};

}
