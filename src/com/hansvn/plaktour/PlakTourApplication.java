package com.hansvn.plaktour;

import java.util.ArrayList;

import android.app.Application;

public class PlakTourApplication extends Application {
	/**
	 * Here the global variables like user, internet, ... are stored
	 * 
	 * set:
	 * ((MyApplication) this.getApplication()).setSomeVariable("foo");
	 * 
	 * get:
	 * String s = ((MyApplication) this.getApplication()).getSomeVariable();
	 */
	private static final String STORAGE_FILENAME = "PlakTour_Data";
	
	private String internet;
	private User user;
	
	private ArrayList<Tour> tours = new ArrayList<Tour>();
	
	public static String getStorageFilename() { return STORAGE_FILENAME; }
	
	public String getInternet() { return internet; }
	public void setInternet(String internet) { this.internet = internet; }
	
	public boolean isConnected() {
		if(internet == "internet") { return true; }
		else { return false; }
	}
	
	public User getUser() { return user; }
	public void setUser(User user) { this.user = user; }

	public ArrayList<Tour> getTours() { return tours; }
	public void setTours(ArrayList<Tour> tours) { this.tours = tours;}

}
