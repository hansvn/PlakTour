package com.hansvn.plaktour;

import java.util.ArrayList;

import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

public class MainActivity extends Activity {

	public final static String EXTRA_MESSAGE = "com.hansvn.plaktour.MESSAGE";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initToursList();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void initToursList() {
		ListView toursList = (ListView) findViewById(R.id.listViewTours);
		ArrayList<View> tours = new ArrayList<View>();
		tours.add(findViewById(R.id.bottomActionBar));
		if (Build.VERSION.SDK_INT >= 16) {
			toursList.addView((View)findViewById(R.id.bottomActionBar));
		}
	}

}
