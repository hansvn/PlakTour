package com.hansvn.plaktour;

import com.google.android.gms.maps.model.MarkerOptions;

public class Point {
	//public MarkerOptions so it can be accessed directly
	//and not an overload of getters and setters are needed
	public MarkerOptions markerOptions;
	private int posters; //the number of posters for this point
	private boolean isDone; //if this point is being done
	private int internetID;

	public Point() {
		// TODO Auto-generated constructor stub
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
	}
	
	public int getInternetID() {
		return internetID;
	}

	public void setInternetID(int internetID) {
		this.internetID = internetID;
	}
}
