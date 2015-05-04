package com.denel.facepatrol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class SplashScreen extends Activity
{
	private static int SPLASH_TIME_OUT = 4000;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		
		// Request window with no title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		// get contwnt from layout
		setContentView(R.layout.layout_splash);
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				Intent i = new Intent(SplashScreen.this,MainContactSearch.class);
				startActivity(i);
				
				// close this activity
				finish();
			}
		},SPLASH_TIME_OUT);
	}
	
	
}
