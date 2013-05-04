package com.hansvn.plaktour;

import java.util.ArrayList;

public class Tour {
	private String title;
	private String description;
	private ArrayList<Point> points;

	private String time;
	private String lastActivity;
	
	public Tour(String title, String description){
		this.title = title;
		this.description = description;
		
		this.time = "0:00";
		this.lastActivity = "niet vandaag";
	}
	
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	public int getLength() { return points.size(); }
	public String getTime() { return time; }
	public void setTime(String time) { this.time = time; }
	public String getLastActivity() { return lastActivity; }
	public void setLastActivity(String lastActivity) { this.lastActivity = lastActivity; }
	
	
	public ArrayList<Point> getPoints() { return points; }
	public void setPoints(ArrayList<Point> points) { this.points = points; }
	
	public int getPosters() { 
		int number = 0;
		for(Point p : points){
			number += p.getPosters();
		}
		return number; 
	}
	
	public int getCompletedpointsNumber() {
		int number = 0;
		for(Point p : points){
			if(p.isDone()) { number++; }
		}
		return number;
	}
}
