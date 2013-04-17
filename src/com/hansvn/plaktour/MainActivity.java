package com.hansvn.plaktour;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends Activity {
	public final static String SELECTED_TOUR = "com.hansvn.plaktour.TOUR";
	public static TourListAdapter tourListAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ListView toursListView = (ListView) findViewById(R.id.listViewTours);
		tourListAdapter = new TourListAdapter();
		toursListView.setAdapter(tourListAdapter);
		
		toursListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long arg){
				//testmessage:
				//Toast.makeText(getApplicationContext(), "You clicked at item "+ position, Toast.LENGTH_LONG).show();
				
				//open tourdetail met item dat op positie position staat...
				//intent enzovoort
				
				Intent intent = new Intent(view.getContext(), TourDetailActivity.class);
				intent.putExtra(SELECTED_TOUR, position+"");
				startActivity(intent);
			}
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
