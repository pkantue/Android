package com.denel.facepatrol;

import android.app.*;
import android.os.*;

public class dummypage extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dummy_page);
		
		Bundle args;
		args = getIntent().getExtras();
		setTitle(args.getString("title"));
	}	
}
