package com.hansvn.plaktour;

public class Tour {
	private String title;
	private String description;
	
	private String points;//this needs to be the number of the points on the tour (map)
	private String posters;//this needs to be the number of posters (depending on the number of posters per point)
	private String time;
	private String lastActivity;
	private String pointsDone;
	
	public Tour(String title, String description){
		this.title = title;
		this.description = description;
		
		this.points = this.posters = this.pointsDone = "0";
		this.time = "0:00";
		this.lastActivity = "niet vandaag";
	}
	
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	
	public String getLength() { return points; }
	public void setLength(String length) { this.points = length; }	
	
	public String getPosters() { return posters; }
	public void setPosters(String posters) { this.posters = posters; }
	
	public String getTime() { return time; }
	public void setTime(String time) { this.time = time; }
	
	public String getLastActivity() { return lastActivity; }
	public void setLastActivity(String lastActivity) { this.lastActivity = lastActivity; }
	
	public String getCompletedPoints() { return pointsDone; }
	public void setCompletedPoints(String pointsDone) { this.pointsDone = pointsDone; }
}
