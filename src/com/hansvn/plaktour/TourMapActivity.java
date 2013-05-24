package com.hansvn.plaktour;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

@SuppressLint("ValidFragment")
public class TourMapActivity extends FragmentActivity {
	private final String TAG = getClass().getSimpleName();
	private static final int DISTANCE_NEAR_POINT = 20;	//20 meters
	private static final int TIME_NEAR_POINT = 60*1000;  //one minute
	private static final int ONE_SECOND = 1000;
	private static int timeOutToPrompt = 60*1000;
	private static Timer timer = new Timer();
	
	private GoogleMap mMap;
	private Tour mTour;
    private LocationManager mLocationManager;
	private Location gpsLocation;
	private Location currentLocation;
	private boolean isNearPoint;
	private Point nextPoint = new Point();

	@SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_map);
        // Show the Up button in the action bar.
 		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
 			getActionBar().setDisplayHomeAsUpEnabled(true);
 		}
 		
 		timeOutToPrompt = TIME_NEAR_POINT;
 		
 		//Access the map
 		setUpMapIfNeeded();
 		
		// Get the selected tour from the intent
	    Intent intent = getIntent();
	    int tourIndex = Integer.parseInt(intent.getStringExtra(MainActivity.SELECTED_TOUR));
	    
	    //Get the tour
	    mTour = (Tour) MainActivity.tourListAdapter.getItem(tourIndex);   
	    
	    //set center of map and add points to the map
	    invalidateMap();
	    addPoints();
	        
	    //Log.i("Location", location.toString());
 		
	    // Get a reference to the LocationManager object.
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        
        //initialise the first point
        nextPoint = getNextPoint();
    }
	
    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }
    
    // Stop receiving location updates whenever the Activity becomes invisible.
    @Override
    protected void onStop() {
        super.onStop();
        mLocationManager.removeUpdates(listener);
    }
    
    @Override
    protected void onStart() {
        super.onStart();

        // Check if the GPS setting is currently enabled on the device.
        // This verification should be done during onStart() because the system calls this method
        // when the user returns to the activity, which ensures the desired location provider is
        // enabled each time the activity resumes from the stopped state.
        LocationManager locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!gpsEnabled) {
            // Build an alert dialog here that requests that the user enable
            // the location services, then when the user clicks the "OK" button,
            // call enableLocationSettings()
            new EnableGpsDialogFragment().show(getSupportFragmentManager(), "enableGpsDialog");
        }
        
	    //Set up the location manager through GPS
	    setUpLocationManager();
    }
    
    // Method to launch (GPS) Settings
    private void enableLocationSettings() {
        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(settingsIntent);
    }
    
    //hier moet nog iets aangepast worden: zoomlevel enzo verdwijnt na roteren device...
	private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.tourMap))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                Log.i(TAG+": Map Status", "map created");
            }
        }
    }
    
    private void setUpLocationManager() {
    	gpsLocation = requestUpdatesFromProvider(
                LocationManager.GPS_PROVIDER, R.string.not_support_gps);
        // Update the UI immediately if a location is obtained.
        if (gpsLocation != null) {
        	Log.i(TAG+": Location Status", "location : " + gpsLocation);
        }
    }
    
    /**
     * Method to register location updates with a desired location provider.  If the requested
     * provider is not available on the device, the app displays a Toast with a message referenced
     * by a resource id.
     */
    private Location requestUpdatesFromProvider(final String provider, final int errorResId) {
        Location location = null;
        if (mLocationManager.isProviderEnabled(provider)) {
            mLocationManager.requestLocationUpdates(provider, ONE_SECOND, 1, listener);
            location = mLocationManager.getLastKnownLocation(provider);
        } else {
            Toast.makeText(this, errorResId, Toast.LENGTH_LONG).show();
        }
        return location;
    }
    
    private final LocationListener listener = new LocationListener() {
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
			// A new location update is received.  Update the app with the location update.
			Log.i(TAG, "location update : " + location);
			currentLocation = location;
			//first calculate the distance to the pole
			//then calculate the time spent in the area of the pole.
			calculateConditions();
		}
	};
	
	//AsyncTask<Params:doInBackground, Progress:onProgressUpdate, Result:onPostExecute >
	
	protected void invalidateMap() {
		mMap.setMyLocationEnabled(true);
		mMap.moveCamera(CameraUpdateFactory.newLatLng(mTour.getPoints().get(0).markerOptions.getPosition()));
		mMap.moveCamera(CameraUpdateFactory.zoomTo(19));
	}
	
	protected void addPoints() {
		ArrayList<Point> points = mTour.getPoints();
		for (int i = 0; i < mTour.getLength(); i++) {
			Point p = (Point)points.get(i);
			drawOnMap(p);
         }
	}
	protected void drawOnMap(Point point) {
		mMap.addMarker(point.markerOptions);
	}
	
	private void calculateConditions() {
		LatLng point = nextPoint.markerOptions.getPosition();
		Location nextPointLocation = new Location("NextPoint");
		nextPointLocation.setLatitude(point.latitude);
		nextPointLocation.setLongitude(point.longitude);
		
		//get the next point if the current next point isn't done
		if (nextPoint.isDone()){
			nextPoint = getNextPoint();		
		}
		
		//calculate distance between two points in meters
		float distance = currentLocation.distanceTo(nextPointLocation);

		if(distance < DISTANCE_NEAR_POINT) {
			//start the timer if it isn't already started
			if(!isNearPoint){
				isNearPoint = true;
				
				//start timer
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						timeOutToPrompt -= ONE_SECOND;
					}
				}, 0, ONE_SECOND);
			}
			
			if(timeOutToPrompt/1000 <= 0){
				//prompt to ask if point is done
				new ConfirmPointDoneDialogFragment().show(getSupportFragmentManager(), "confirmPointDoneDialog");
				//set active tour on active point to done
				
				//reset the timer
				timer.cancel();
				timer.purge();
				timer = new Timer();
				timeOutToPrompt = TIME_NEAR_POINT;
				isNearPoint = false;
			}

		}
	}
	
	private Point getNextPoint() {
		ArrayList<Point> points = mTour.getPoints();
		Point returnValue = new Point();
		for (int i = 0; i < mTour.getLength(); i++) {
			Point p = (Point)points.get(i);
			if(!p.isDone()) {
				returnValue = p;
				break;
			}
         }
		return returnValue; 
	}
	
    /**
     * Dialog to prompt users to enable GPS on the device.
     */
    private class EnableGpsDialogFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.enable_gps)
                .setMessage(R.string.enable_gps_dialog)
                .setPositiveButton(R.string.enable_gps, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        enableLocationSettings();
                    }
                })
                .create();
        }
    }
    
    /**
     * Dialog to prompt users confirm if the point is done
     */
    private class ConfirmPointDoneDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.confirm_point_done)
                .setMessage(R.string.confirm_point_done_dialog)
                .setPositiveButton(R.string.confirm_point_done_action, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    	nextPoint.setDone(true);
                    	setDoneMarker(nextPoint);
                    }
                })
                .create();
        }
        
        private void setDoneMarker(Point pointToMark) {
        	MarkerOptions moToMark = pointToMark.markerOptions;
        	moToMark.icon(BitmapDescriptorFactory.fromResource(R.drawable.check_icon));
        	mMap.addMarker(moToMark);
        }
    }

}
