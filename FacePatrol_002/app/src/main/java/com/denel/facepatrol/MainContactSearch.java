package com.denel.facepatrol;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.database.sqlite.*;
import android.widget.*;
import android.content.*;
import android.database.*;
import java.util.*;
import android.text.*;
import android.util.*;
import android.support.v4.app.*;
import android.view.*;
import android.net.*;
import android.app.*;
import android.app.ActionBar.*;

public class MainContactSearch extends FragmentActivity implements 
ContactList.onContactItemListener, ActionBar.TabListener
{

	@Override
	public void onTabSelected(ActionBar.Tab p1, android.app.FragmentTransaction p2)
	{
		// TODO: Implement this method
	}

	@Override
	public void onTabUnselected(ActionBar.Tab p1, android.app.FragmentTransaction p2)
	{
		// TODO: Implement this method
	}

	@Override
	public void onTabReselected(ActionBar.Tab p1, android.app.FragmentTransaction p2)
	{
		// TODO: Implement this method
	}

	String  contact_phone, contact_email;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_page);
		
		// Set up the action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setHomeButtonEnabled(false);

        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		actionBar.addTab(actionBar.newTab().setText("Search").setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("Products").setTabListener(this));
		
		// setup bundle to pass argument to fragment class
		final Bundle args = new Bundle();
		
		// setup text search to be passed to fragment as arguments
		// this appear on all layout types
		
		// Check whether the activity is using the layout version with
        // the fragment_container FrameLayout. If so, we must add the first fragment
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }
			
			ContactList firstFragment = new ContactList();
			// In case this activity was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments
			firstFragment.setArguments(args);
            //firstFragment.setArguments(getIntent().getExtras());
			
            // Add the fragment to the 'fragment_container' FrameLayout in contact_page.xml
            getSupportFragmentManager().beginTransaction()
				.add(R.id.fragment_container, firstFragment).commit();
		}		
	}
	
	@Override
	public void onContactSelected(Bundle bundle)
	{
		// TODO: Implement this method
		ContactDetails detailsfrag = (ContactDetails)
		getSupportFragmentManager().findFragmentById(R.id.detail_fragment);
		
		contact_phone = bundle.getString("phone");
		contact_email = bundle.getString("email");

        if (detailsfrag != null) {
            // If article frag is available, we're in two-pane layout...

            // Call a method in the ArticleFragment to update its content
			// -------------------------
			// UPDATE THE NULL TO BUNDLE
            detailsfrag.updateContactView(bundle);

        } else {
            // If the frag is not available, we're in the one-pane layout and must swap frags...

            // Create fragment and give it an argument for the selected article
            ContactDetails newFragment = new ContactDetails();
            
            //args.putInt(detailsfrag.ARG_POSITION, position);
            newFragment.setArguments(bundle);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        }
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
}
