package com.denel.facepatrol;

import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class ContactPic extends Activity
{
	TextView header,division,dept,title,products,region,work,personal,birthday;
	public static String contact_phone = "+2320343";
	public static String contact_email = "plus@gmail.com";

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
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setData(Uri.parse("mailto:"));		
		emailIntent.putExtra(Intent.EXTRA_EMAIL,new String[]{contact_email});
		emailIntent.setType("message/rfc822");
		emailIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		startActivity(Intent.createChooser(emailIntent,"Send Email..."));
		//finish();
	}
	
	public void updateContactView (Bundle bundle){
		// enter code here

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
	}
	
}
