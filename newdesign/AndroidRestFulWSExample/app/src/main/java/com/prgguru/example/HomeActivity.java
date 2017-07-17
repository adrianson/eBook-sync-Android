package com.prgguru.example;

import android.app.Activity;
import android.os.Bundle;
/**
 * 
 * Home Screen Activity
 */
public class HomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Displays Home Screen
		setContentView(R.layout.home);
	}

}
