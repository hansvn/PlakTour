package com.hansvn.plaktour;
import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class TourListAdapter extends BaseAdapter {
	
	private ArrayList<Tour> tours = new ArrayList<Tour>();
	
	//testData, delete this constructor later to remove testdata
	public TourListAdapter() {
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

}
