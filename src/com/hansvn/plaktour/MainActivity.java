package com.hansvn.plaktour;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	TourListAdapter tourListAdapter;
	
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
				Toast.makeText(getApplicationContext(), "You clicked at item "+ position, Toast.LENGTH_LONG).show();
				
				//open tourdetail met item dat op positie position staat...
				//intent enzovoort
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
