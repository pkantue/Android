/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.denel.facepatrol;

import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.support.v4.app.*;
import android.support.v4.view.*;
import android.util.*;
import android.view.*;
import java.io.*;
import java.net.*;
import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.spec.*;

import android.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class MainActivity extends FragmentActivity implements 
ContactList.onContactItemListener, ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
     * three primary sections of the app. We use a {@link android.support.v4.app.FragmentPagerAdapter}
     * derivative, which will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will display the three primary sections of the app, one at a
     * time.
     */
    ViewPager mViewPager;
	String  contact_phone, contact_email;
	Context mycontext;
	

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		
		mycontext = getApplicationContext();
		
		//copy database from assets folder
		copydatabase(mycontext);
		
        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setHomeButtonEnabled(false);

        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }
	
	public void onGroupEmail(String[] email_grp)
	{
		// enter code here 
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setData(Uri.parse("mailto:"));
		emailIntent.putExtra(Intent.EXTRA_EMAIL,email_grp);
		emailIntent.setType("message/rfc822");
		emailIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		startActivity(Intent.createChooser(emailIntent,"Send Group Email..."));
	}
	
	@Override
	public void onContactSelected(Bundle bundle)
	{
		contact_phone = bundle.getString("phone");
		contact_email = bundle.getString("email");
	}

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
				case 0:
					return new ContactList();
				case 1:
					return new ProductsFragment();
                case 2:
                    return new ServicesFragment();

                default:
                    return new ContactList();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
			switch (position){
				case 0:
					return "Main Search";
				case 1:
					return "Products";
				case 2:
					return "Services";
			}
            return null;
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

	public void copydatabase(Context mcontext){
		
		String dbname = "DenelDB"; 
		File outfile = mcontext.getDatabasePath(dbname); 
		if (outfile.exists()) { 
		return; 
		}
		outfile = mcontext.getDatabasePath(dbname + ".temp");
		outfile.getParentFile().mkdirs();
		try{			
			InputStream instream =  mcontext.getAssets().open(dbname);
			OutputStream outstream = new FileOutputStream(outfile);
			//transfer bytes from instream to outstream
			byte[] buffer = new byte[1024];
			int length;
			while((length = instream.read(buffer))>0){
				outstream.write(buffer,0,length);
			}
			outstream.flush();
			outstream.close();
			instream.close();
			outfile.renameTo(mcontext.getDatabasePath(dbname));
		}catch (IOException e){
			if (outfile.exists()) { outfile.delete();}
		}
	}
	
	private void startDownload (){
		String url = "http://fdsfsd..sdf";
		new DownloadFileAsync().execute(url);
	}
	
	class DownloadFileAsync extends AsyncTask<String, String, String>
	{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			//showDialog(DIALOG_DOWNLOAD_PROGRESS);
		}
		
		@Override
		protected String doInBackground(String... f_url)
		{
			// TODO: Implement this method
			int count;
			try {
				URL url = new URL(f_url[0]);
				URLConnection connection = url.openConnection();
				connection.connect();
				// Get Music file length
				int lenghtOfFile = connection.getContentLength();
				// input stream to read file - with 8k buffer
				InputStream input = new BufferedInputStream(url.openStream(),10*1024);
				// Output stream to write file in SD card
				OutputStream output = new FileOutputStream(Environment.
				getExternalStorageDirectory().getPath()+"file_name.ext");
				byte data[] = new byte[1024];
				long total = 0;
				while ((count = input.read(data)) != - 1) {
					total += count;
					// Publish the progress which triggers onProgressUpdate
					//publishProgress( "" + ( int ) ((total * 100) / lenghtOfFil
					// Write data to file
					output.write(data, 0 , count);
				}
				// Flush output
				output.flush();
				// Close streams
				output.close();
				input.close();
			} catch (Exception e) {
				Log.e( "Error: " , e.getMessage());
			}
			return null;
		}
		
	}
	
	// generate secretkey with a user-password or pin
	private static SecretKey generateKey(char[] passphraseOrPin, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException { 
		// Number of PBKDF2 hardening rounds to use. Larger values increase 
		// computation time. You should select a value that causes computation 
		// to take >100ms. 
		final int iterations = 1000;
		// Generate a 256-bit key
		final int outputKeyLength = 256;
		SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1"); 
		KeySpec keySpec = new PBEKeySpec(passphraseOrPin, salt, iterations, outputKeyLength); 
		SecretKey secretKey = secretKeyFactory.generateSecret(keySpec); 
		return secretKey; 
	}
}
