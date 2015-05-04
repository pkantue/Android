package com.denel.facepatrol;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.zip.*;
import android.util.*;
import android.content.*;
import android.net.*;

public class ContactDetails extends Fragment 
{
	final static String ARG_POSITION = "position";
	TextView header,division,dept,title,products,region,work,personal;
	String contact_phone = "+2320343";
	String contact_email = "plus@gmail.com";
    int mCurrentPosition = -1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// If activity recreated (such as from screen rotate), restore
        // the previous article selection set by onSaveInstanceState().
        // This is primarily necessary when in the two-pane layout.
		if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
        }
		// TODO: Implement this method
		// the inflater argument need to be replaced with the layout view
		return inflater.inflate(R.layout.contact_detail,container,false);
	}

	@Override
	public void onStart()
	{
		super.onStart();
		// During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
		
		Bundle args = getArguments();
        if (args != null) 
		{
			// enter code if there any passed arguments
			updateContactView(args);
		}
	}
	
	public void updateContactView (Bundle bundle){
		// enter code here
		
		// set main_xml variables
		header = (TextView)getActivity().findViewById(R.id.header_name);
		division = (TextView)getActivity().findViewById(R.id.division_name);
		dept = (TextView)getActivity().findViewById(R.id.dept_name);
		title = (TextView)getActivity().findViewById(R.id.contact_title);
		products = (TextView)getActivity().findViewById(R.id.contact_products);
		region = (TextView)getActivity().findViewById(R.id.contact_region);
		work = (TextView)getActivity().findViewById(R.id.work_interests);
		personal = (TextView)getActivity().findViewById(R.id.personal_interests);
		
		header.setText(bundle.getString("name") + " " + bundle.getString("surname"));
		division.setText(bundle.getString("division"));
		dept.setText(bundle.getString("dept"));
		title.setText(bundle.getString("title"));
		products.setText(bundle.getString("product"));
		region.setText(bundle.getString("region"));
		work.setText(bundle.getString("work_int"));
		personal.setText(bundle.getString("personal"));
		contact_phone = bundle.getString("phone");
		contact_email = bundle.getString("email");
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		// TODO: Implement this method
		super.onSaveInstanceState(outState);
		
	}
	
}
