package com.hansvn.plaktour;

import java.io.Serializable;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;

public class Point implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//public MarkerOptions so it can be accessed directly
	//and not an overload of getters and setters are needed
	public transient MarkerOptions markerOptions;
	private int posters; //the number of posters for this point
	private boolean isDone; //if this point is being done
	private boolean isUpdated; //if the point is updated with internet
	private int internetID;

	public Point() {
		
	}
	
	public int getPosters() {
		return posters;
	}

	public void setPosters(int posters) {
		this.posters = posters;
	}

	public boolean isDone() {
		return isDone;
	}

	public void setDone(boolean isDone) {
		this.isDone = isDone;
		
		new PostPointProgress().execute();
	}
	
	public boolean isUpdated() {
		return isUpdated;
	}
	
	public int getInternetID() {
		return internetID;
	}

	public void setInternetID(int internetID) {
		this.internetID = internetID;
	}
	
	
	public void setMarkerOptions() {
		//this must be done when the map is initialised, otherwise there will be an error on the icon.
		MarkerOptions mo = new MarkerOptions();
		mo.position(this.markerOptions.getPosition());
		mo.title(this.markerOptions.getTitle());
		mo.icon(BitmapDescriptorFactory.fromResource(R.drawable.pole_icon));
		this.markerOptions = mo;
	}
	
	class PostPointProgress extends AsyncTask<Void, Void, Boolean> {
		private Exception exception;
		
		@Override
		protected Boolean doInBackground(Void... arg0) {
		    try {
		    	NetworkActivities na = new NetworkActivities(Point.this);
		    	Boolean retBool = (Boolean)na.update();
		        return retBool;
		    } catch (Exception e) {
		        this.exception = e;
		        Log.e("AsyncTask", exception.toString());
		        return false;
		    }
		}
		
		@Override
		protected void onPostExecute(Boolean status) {		
			Point.this.isUpdated = status;
		}

	}
}
