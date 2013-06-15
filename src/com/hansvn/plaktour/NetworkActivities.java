package com.hansvn.plaktour;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class NetworkActivities {
	private final String BASEURL = "http://www.hansvn.be/PlakTour_app/";
	private final String postProgressUrl = BASEURL+"json/postProgress.php";
	private final String postNewPointUrl = BASEURL+"json/postPoint.php";
	private String type = null;
	private Object value = null;

	public NetworkActivities() {
		this.type = "undefined";
	}
	
	public NetworkActivities(Object value) {
		this.value = value;
	}
	
	public NetworkActivities(Point value) {
		this.type = "point";
		this.value = value;
	}
	
	public NetworkActivities(Point value, String type) {
		this.type = type;
		this.value = value;
	}
	
	public boolean update() {
		//returnwaarde
		boolean returnValue = false;
		
		//kijken of het een punt is
		if(this.type == "point"){
			//waarde naar punt casten
			Point pointToUpdate = (Point) value;
			
			//posten:
			//initialize
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(postProgressUrl.toString());
			HttpResponse response = null;
			InputStream is = null;
			
			//http post
			try{
				JSONObject jObject = new JSONObject();
				jObject.put("userId", 0);
				jObject.put("toerId", 0);
				jObject.put("puntId", pointToUpdate.getInternetID());
				jObject.put("opmerkingId", 0);
				
				httppost.setHeader("json", jObject.toString());
				StringEntity se = new StringEntity(jObject.toString());
				se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
				httppost.setEntity(se);
				response = httpclient.execute(httppost);
				
				HttpEntity entity = response.getEntity();
				is = entity.getContent();

			}catch(Exception e){
				Log.e("log_tag", "Error in http connection "+e.toString());
			}

			//convert response to string
			try{
				BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				String result = sb.toString();
				
				JSONObject jArray = null;
				
				try {
					jArray = new JSONObject(result);
				}catch(JSONException e){
					Log.e("log_tag", "Error parsing data "+e.toString());
				}
				
				//get the status
				if (jArray.getString("status").equals("success")) {
					returnValue = true;
				}
				else returnValue = false;
				
			}catch(Exception e){
				Log.e("log_tag", "Error converting result "+e.toString());
			}
			
		}
		
		return returnValue;
	}
	
	public int postPoint(Tour tour, Point point) {
		//returnwaarde is de nieuwe intenernetId van het punt
		int returnValue = -1;
		
		//posten:
		//initialize
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(postNewPointUrl.toString());
		HttpResponse response = null;
		InputStream is = null;
		
		//http post
		try{
			JSONObject jObject = new JSONObject();
			jObject.put("userId", 0);
			jObject.put("toerId", tour.getInternetID());
			jObject.put("puntNaam", point.getName());
			jObject.put("puntType", 1);
			jObject.put("puntLatitude", point.markerOptions.getPosition().latitude);
			jObject.put("puntLongitude", point.markerOptions.getPosition().longitude);
			jObject.put("puntPosters", point.getPosters());
			
			httppost.setHeader("json", jObject.toString());
			StringEntity se = new StringEntity(jObject.toString());
			se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			httppost.setEntity(se);
			response = httpclient.execute(httppost);
			
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		}catch(Exception e){
			Log.e("PlakTour Network", "Error in http connection "+e.getMessage());
		}

		//convert response to string
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			String result = sb.toString();
			
			JSONObject jArray = null;
			
			try {
				jArray = new JSONObject(result);
			}catch(JSONException e){
				Log.e("log_tag", "Error parsing data "+e.toString());
			}
			
			//get the status
			if (jArray.getString("status").equals("success")) {
				returnValue = Integer.parseInt(jArray.getString("internetId"));
			}
			else returnValue = 0;
			
		}catch(Exception e){
			Log.e("log_tag", "Error converting result "+e.toString());
		}
		
		return returnValue;
	}

}