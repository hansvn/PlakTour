package com.hansvn.plaktour;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class TourListAdapter extends BaseAdapter {
	
	private ArrayList<Tour> tours = new ArrayList<Tour>();
	private Context ctx = null;	//the parents context for the internal storage
	private static String STORAGE_FILENAME = "PlakTour_Data";
	
	//testData, delete this constructor later to remove testdata
	public TourListAdapter(String mode, Context ctx) {		
		/**
		 * get the data from internet or local storage
		 * mode is ofwel via internet ofwel via local storage
		 **/
		this.ctx = ctx;
		
		if(mode == "internet"){
			//do the internet call
			new RetreiveToursTask().execute("http://www.hansvn.be/PlakTour_app/json/getTours.php");	//of 192.168.123.89
		}
		else {
			//get from local storage
			tours = readFromInternalStorage();
		}
		
		///*
		tours.add(new Tour(
		"Een", "Feeling good!"));
		tours.add(new Tour(
		"Twee", "Tired. Needed more caffeine"));
		tours.add(new Tour(
		"Drie", "I’m rocking it!"));
		tours.add(new Tour(
		"Vier", "Pretty good."));
		
		ArrayList<Point> points = new ArrayList<Point>();
		
		MarkerOptions firstPoint = new MarkerOptions();
		firstPoint.position(new LatLng(50.990792, 4.409897));
		firstPoint.title("ergens");
		Point punt = new Point();
		punt.markerOptions = firstPoint;
		points.add(punt);
		
		MarkerOptions secondPoint = new MarkerOptions();
		secondPoint.position(new LatLng(50.990550, 4.410184));
		secondPoint.title("ergens opnieuw");
		Point punt2 = new Point();
		punt2.markerOptions = secondPoint;
		points.add(punt2);
		
		for (int i=0;i<10;i++){
			MarkerOptions mo = new MarkerOptions();
			mo.position(new LatLng(50.962338+i, 4.457528+i));
			mo.title("punt "+i);
			Point p = new Point();
			p.markerOptions = mo;
			points.add(p);
		}
		tours.get(3).setPoints(points);
		//*/
	}
	
	
	@Override
	public int getCount() {
		return tours.size();
	}

	@Override
	public Tour getItem(int arg0) {
		return tours.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			convertView = inflater.inflate(R.layout.tour_list_item, parent, false);
		}
		
		Tour tour = tours.get(position);
		
		//set title and description on every item
		TextView titleTextView = (TextView) convertView.findViewById(R.id.title_view);
		titleTextView.setText(tour.getTitle());
		
		TextView descriptionTextView = (TextView) convertView.findViewById(R.id.description_view);
		descriptionTextView.setText(tour.getDescription());
		
		return convertView;
	}
	
	public void saveToInternalStorage() {
		try {
			FileOutputStream fos = ctx.openFileOutput(STORAGE_FILENAME, Context.MODE_PRIVATE);
			ObjectOutputStream of = new ObjectOutputStream(fos);
			of.writeObject(tours);
			of.flush();
			of.close();
			fos.close();
		}
		catch (Exception e) {
			Log.e("PlakTour_InternalStorage", e.getLocalizedMessage());
		}
	}
	
	@SuppressWarnings("unchecked")	//voor de cast naar arraylist<Tour>
	public ArrayList<Tour> readFromInternalStorage() {
	    ArrayList<Tour> toReturn = new ArrayList<Tour>();
	    FileInputStream fis;
	    try {
	        fis = ctx.openFileInput(STORAGE_FILENAME);
	        ObjectInputStream oi = new ObjectInputStream(fis);
	        toReturn = (ArrayList<Tour>) oi.readObject();
	        oi.close();
	    } catch (FileNotFoundException e) {
	        Log.e("PlaktTour_InternalStorage", e.getMessage());
	    } catch (IOException e) {
	        Log.e("PlaktTour_InternalStorage", e.getMessage()); 
	    } catch (ClassNotFoundException e) {
	    	Log.e("PlaktTour_InternalStorage", e.getMessage());
	    }
	    return toReturn;
	} 
	
	
	class RetreiveToursTask extends AsyncTask<String, Void, ArrayList<Tour>> {
		private Exception exception;
		
		@Override
		protected ArrayList<Tour> doInBackground(String... urls) {
		    try {
		        URL url= new URL(urls[0]);
		        //return arraylist
		        ArrayList<Tour> toReturn = getFromInternet(url);
		        return toReturn;
		    } catch (Exception e) {
		        this.exception = e;
		        Log.e("AsyncTask", exception.toString());
		        return null;
		    }
		}
		
		@Override
		protected void onPostExecute(ArrayList<Tour> toursArray) {
		    //add the tours from internet to the array
			if(toursArray != null) {
				tours.addAll(toursArray);
				TourListAdapter.this.notifyDataSetChanged();
			}
			else {
				Log.e("Http_tours","No tours were added from internet");
			}
		}
	}
	
	//this must return a JSONArray that is filtered on the tours...
	private static ArrayList<Tour> getFromInternet(URL api_url) {
		JSONObject json = getJSONfromURL(api_url);
		ArrayList<Tour> toursFromInternet = new ArrayList<Tour>();		
		
		try{
			//Get the element that holds the tours ( JSONArray )
			int statusCode = Integer.parseInt(json.getString("statusCode"));
			if(statusCode != 1){
				//the query didn't execute...
				Log.e("PlakTour_HTTPRequest","query didn't execute");
			}
			else
			{
				Tour t = new Tour();
				int tourId = 0;
				int jsonLength = json.length();
				if(jsonLength <= 1){
					throw new JSONException("json answer too short...");
				}
				//iterate over the points
				for(int i=0; i<jsonLength-1; i++) {
					JSONObject punt = json.getJSONObject(Integer.toString(i));
					int currentTourId = Integer.parseInt(punt.getString("tour_id"));
					
					//initialiseer de eerste tour
					if(tourId == 0){
						tourId = currentTourId;
						
						t.setInternetID(currentTourId);
						t.setTitle(punt.getString("tour_naam"));
						t.setDescription(punt.getString("tour_beschrijving"));
						t.setLastActivity(punt.getString("tour_laatsteActiviteit"));
						t.setTime(punt.getString("tour_tijd"));
					}
					
					if(currentTourId == tourId) {
						//punt behoort tot dezelfde tour
						Point p = new Point();
						p.setInternetID(Integer.parseInt(punt.getString("punt_id")));
						p.setPosters(Integer.parseInt(punt.getString("punt_posters")));
						MarkerOptions mo = new MarkerOptions();
						mo.title(punt.getString("punt_naam"));
						//mo.icon() nog zetten met punt.getString("punt_type_icoon");
						mo.position(new LatLng(Double.parseDouble(punt.getString("punt_latitude")), Double.parseDouble(punt.getString("punt_longitude"))));
						p.markerOptions = mo;
						
						t.addPoint(p);
					}
					else {
						//punt behoort tot een nieuwe tour: zet huidige tour weg in de lijst en maak een nieuwe tour aan.
						toursFromInternet.add(t);
						
						t = new Tour();
						t.setInternetID(currentTourId);
						t.setTitle(punt.getString("tour_naam"));
						t.setDescription(punt.getString("tour_beschrijving"));
						t.setLastActivity(punt.getString("tour_laatsteActiviteit"));
						t.setTime(punt.getString("tour_tijd"));
						
						//create the point
						Point p = new Point();
						p.setInternetID(Integer.parseInt(punt.getString("punt_id")));
						p.setPosters(Integer.parseInt(punt.getString("punt_posters")));
						MarkerOptions mo = new MarkerOptions();
						mo.title(punt.getString("punt_naam"));
						//mo.icon() nog zetten met punt.getString("punt_type_icoon");
						mo.position(new LatLng(Double.parseDouble(punt.getString("punt_latitude")), Double.parseDouble(punt.getString("punt_longitude"))));
						p.markerOptions = mo;
						
						t.addPoint(p);
						
						tourId = currentTourId;
					}
				} //end for loop
				
				toursFromInternet.add(t);
			}
			
		}
		catch(JSONException e) {
			Log.e("log_tag", "Error parsing data "+e.toString());
		}
		
		return toursFromInternet;
	}
	
	/**
	 * fetch the data from the internet or local storage
	 */
	
	public static JSONObject getJSONfromURL(URL url){
		//initialize
		InputStream is = null;
		String result = "";
		JSONObject jArray = null;

		//http post
		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url.toString());
			HttpResponse response = httpclient.execute(httppost);
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
			result=sb.toString();
		}catch(Exception e){
			Log.e("log_tag", "Error converting result "+e.toString());
		}

		//try parse the string to a JSON object
		try{
	        jArray = new JSONObject(result);
		}catch(JSONException e){
			Log.e("log_tag", "Error parsing data "+e.toString());
		}

		return jArray;
	}
	
}
