package com.denel.facepatrol;
import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;

public class Products extends Activity implements ActionBar.TabListener
{
    int flag = 0;
	@Override
	public void onTabSelected(ActionBar.Tab p1, FragmentTransaction p2)
	{
		// TODO: Implement this method

		int pos = p1.getPosition();
		if (pos == 0 && flag == 1){
			Intent intent = new Intent(this,MainContactSearch.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("pos",pos);
			startActivity(intent);
			flag = 0;
			//intent to start MainContactSearch
		}
	}

	@Override
	public void onTabUnselected(ActionBar.Tab p1, FragmentTransaction p2)
	{
		// TODO: Implement this method
	}

	@Override
	public void onTabReselected(ActionBar.Tab p1, FragmentTransaction p2)
	{
		// TODO: Implement this method
	}


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dummy_page);
		
		// arguments passed from other activities
		Bundle args = getIntent().getExtras();		
		Log.d("null","pos "+ args.getInt("pos"));
		
		// Set up the action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setHomeButtonEnabled(false);

        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		actionBar.addTab(actionBar.newTab().setText("Search").setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("Products").setTabListener(this));
		
		actionBar.setSelectedNavigationItem(1);//args.getInt("pos"));
	}

	@Override
	protected void onStart()
	{
		// TODO: Implement this method
		super.onStart();
		flag = 1;
	}
	
	
}
