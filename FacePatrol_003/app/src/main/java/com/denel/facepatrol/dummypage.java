package com.denel.facepatrol;

import android.app.*;
import android.os.*;
import android.widget.*;

public class dummypage extends Activity
{
    TextView text;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dummy_page);
		
		Bundle args;
		args = getIntent().getExtras();
		setTitle(args.getString("title"));
		text = (TextView)findViewById(R.id.dummypage_textview);
		text.setText(args.getString("content"));
		
	}	
}
