package com.denel.facepatrol;

import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import java.io.*;

public class Settings extends Activity
{
	TextView ethics, delete_folder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		// set content
		setContentView(R.layout.settings);
		ethics = (TextView)findViewById(R.id.code_ethics);
		delete_folder = (TextView)findViewById(R.id.delete_database);
		ethics.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v){
				Intent intent = new Intent(Settings.this,dummypage.class);
				intent.putExtra("title","Code Of Ethics");
				intent.putExtra("content",getResources().getString(R.string.code_of_ethics));
				startActivity(intent);
			}
		});
		delete_folder.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v){
					File appDir = getApplicationContext().getDir("pictures",0);
					if (appDir.exists()) { 
						String[] children = appDir.list(); 
						for (String s : children) { 
							File f = new File(appDir, s); 
							if(deleteDir(f)) Log.i("delete", String.format("*** DELETED -> (%s) ***", f.getAbsolutePath()));
						}
					}
				}
		});
	}
	
	private static boolean deleteDir(File dir) { 
		if (dir != null && dir.isDirectory()) { 
			String[] children = dir.list(); 
			for (int i = 0; i < children.length; i++) { 
				boolean success = deleteDir(new File(dir, children[i])); 
				if (!success) { 
					return false; 
				} 
			} 
		} 
		return dir.delete();
	}
}
