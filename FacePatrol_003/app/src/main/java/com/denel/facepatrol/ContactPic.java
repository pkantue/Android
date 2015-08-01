package com.denel.facepatrol;

import android.app.*;
import android.content.*;
import android.graphics.drawable.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.io.*;

public class ContactPic extends Activity
{
	TextView header,division,dept,title,products,region,work,personal,birthday;
	public static String contact_phone = "+2320343";
	public static String contact_email = "plus@gmail.com";
	public static String feedback_body, feedback_subject;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_detail);
		//set the activity
		Bundle args = new Bundle();
		args = getIntent().getExtras();
		updateContactView(args);		
	}	

	// ImageView onClick methods
	public void ContactPhone (View view){
		Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
		phoneIntent.setData(Uri.parse("tel:"+contact_phone));
		phoneIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		startActivity(Intent.createChooser(phoneIntent,"Phone Number"));
		//finish();
	}

	public void ContactEmail (View view){
		Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
		emailIntent.setData(Uri.parse("mailto:"));		
		emailIntent.putExtra(Intent.EXTRA_EMAIL,new String[]{contact_email});
		emailIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		startActivity(Intent.createChooser(emailIntent,"Send Email..."));
		//finish();
	}
	
	public void ContactEdit (View view){
		AlertDialog.Builder builder = new AlertDialog.Builder(this); 
		builder.setTitle("Contact Information Feedback")
			.setMessage("You're about to edit and send personal information. "+
						"Please note that the current database will only reflect your modification"+
						" once the IT department verifies the change and the updated database"+
						" is synced to your device. \n \n Do you want to continue?") 
			.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
				public void onClick(DialogInterface dialog, int id) { 
					Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
					emailIntent.setData(Uri.parse("mailto:")); 
					emailIntent.putExtra(Intent.EXTRA_EMAIL,new String[]{"pkantue@gmail.com"}); // this email address will change
					emailIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
					emailIntent.putExtra(Intent.EXTRA_SUBJECT,feedback_subject);
					emailIntent.putExtra(Intent.EXTRA_TEXT,feedback_body);
					startActivity(Intent.createChooser(emailIntent,"Send Email..."));
				} })
			.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { 
		    	public void onClick(DialogInterface dialog, int id) { 
					// User cancelled the dialog 
				} 
			});
		builder.show();
		// exit the application
		//finish();		
	}
	
	public void updateContactView (Bundle bundle){
		
		// set main_xml variables
		header = (TextView)findViewById(R.id.header_name);
		division = (TextView)findViewById(R.id.division_name);
		dept = (TextView)findViewById(R.id.dept_name);
		title = (TextView)findViewById(R.id.contact_title);
		products = (TextView)findViewById(R.id.contact_products);
		region = (TextView)findViewById(R.id.contact_region);
		work = (TextView)findViewById(R.id.work_interests);
		birthday = (TextView)findViewById(R.id.contact_birthday);
		personal = (TextView)findViewById(R.id.personal_interests);

		ImageView img = (ImageView)findViewById(R.id.contact_picture);
		String pathName = getApplicationContext().getDir("pictures",0).getAbsolutePath() +"/";
		String filename = bundle.getString("name") + " " + bundle.getString("surname") +".png";
		File ip = new File(pathName+filename);
		if (ip.exists())
		{
			Drawable d = Drawable.createFromPath(pathName+filename);
			img.setImageDrawable(d);
		}
		else{
			img.setImageResource(R.drawable.pic_6); // default image if no picture exists
		}	

		header.setText(bundle.getString("name") + " " + bundle.getString("surname"));
		division.setText(bundle.getString("division"));
		dept.setText(bundle.getString("dept"));
		title.setText(bundle.getString("title"));
		products.setText(bundle.getString("product"));
		region.setText(bundle.getString("region"));
		work.setText(bundle.getString("work_int"));
		personal.setText(bundle.getString("personal"));
		birthday.setText(bundle.getString("birthday"));
		contact_phone = bundle.getString("phone");
		contact_email = bundle.getString("email");
		feedback_body = "Face Patrol - Contact Update: "+bundle.getString("name")+" "+bundle.getString("surname");
		feedback_subject = "Please edit the contact information below BEFORE submission \n\n"+
			"Name: "+bundle.getString("name")+" "+bundle.getString("surname")+"\n"+
			"Division: "+bundle.getString("division")+"\n"+
			"Department: " +bundle.getString("dept")+"\n"+
			"Title: " +bundle.getString("title")+"\n"+
			"Products Expertise: " +bundle.getString("product")+"\n"+
			"Regions: " +bundle.getString("region")+"\n"+
			"Work Interests: " +bundle.getString("work_int")+"\n"+
			"Personal Interests: " +bundle.getString("personal")+"\n"+
			"Birthday: "+bundle.getString("birthday")+"\n"+
			"Phone No: "+bundle.getString("phone")+"\n"+
			"Email Address: "+bundle.getString("email")+"\n";
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate main_menu.xml 
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.sub_menu, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item)
	{
		// TODO: Implement this method
		switch (item.getItemId())
		{
			case R.id.action_settings:
				Intent intent1 = new Intent(this,Settings.class);
				startActivity(intent1);
				return true;
			case R.id.main_help:
				// start activity using dummy class
				run_quick_tips();
				//Intent intent = new Intent(this,dummypage.class);
				//intent.putExtra("title","Help");
				//startActivity(intent); 
				return true;
			case R.id.main_about:
				AlertDialog.Builder builder = new AlertDialog.Builder(this); 
				builder.setTitle("About")
					.setMessage(R.string.about) 
					.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
						public void onClick(DialogInterface dialog, int id) { 
							// FIRE ZE MISSILES! 
						} });
				//.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { 
				//	public void onClick(DialogInterface dialog, int id) { 
				// User cancelled the dialog 
				//} });
				builder.show();
				return true;
			case R.id.action_exit:
				// exit the application
				finish();
				return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	public void run_quick_tips(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this); 
		builder.setTitle("Quick Tips")
			.setMessage(R.string.quick_tips) 
			.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
				public void onClick(DialogInterface dialog, int id) { 
					// FIRE ZE MISSILES! 
				} });
		//.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { 
		//	public void onClick(DialogInterface dialog, int id) { 
		// User cancelled the dialog 
		//} });
		builder.show();
	}
}
