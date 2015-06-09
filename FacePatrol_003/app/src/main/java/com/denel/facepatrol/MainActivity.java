/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License atge
 *
 *     http://www.apache.org/licenses/LINSE-2.0
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
import java.util.zip.*;

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
	String pass1_text = null;
	SecretKey skey = null;
	String dbname = "DenelDB";
	String dbname_en = "EncryptDB";
	boolean is_db_encrypted = false;
	
	// This is for checking external storage for pictures
	BroadcastReceiver mExternalStorageReceiver;
	boolean mExternalStorageAvailable = false;
	boolean mExternalStorageWritable = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		
		mycontext = getApplicationContext();		
		
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

	@Override
	protected void onStart()
	{
		// TODO: Implement this method
		super.onStart();
		Bundle bundle;
		bundle = getIntent().getExtras();
		if (bundle != null){
			pass1_text = bundle.getString("passw");
			try{
				if (pass1_text != null){ 
					skey = generateKey(pass1_text.toCharArray(),"sdf764rew51sdr98d2".getBytes());
				}
			}catch(NoSuchAlgorithmException | InvalidKeySpecException f)
			{Log.e(null,"Unable to generate key throws Exception");}

		}
	}
	
	

	@Override
	protected void onPause()
	{
		// TODO: Implement this method
		super.onPause();
		// encrypt data
		// create secret key based on password
		if(!is_db_encrypted){
			try{
				encryptfile(getApplicationContext(),skey);
			}catch (IOException | NoSuchAlgorithmException | 
			NoSuchPaddingException | InvalidKeyException ex){
				Log.e(null,"Unable to encrypt file throws Exception");
			}
			is_db_encrypted = true;
		}
	}

	@Override
	protected void onResume()
	{
		// TODO: Implement this method
		super.onResume();
		// decrypt data
		if(is_db_encrypted){
			try{
				decryptfile(getApplicationContext(),skey);
			}catch (IOException | NoSuchAlgorithmException | 
			NoSuchPaddingException | InvalidKeyException ex){
				Log.e(null,"Unable to decrypt file throws Exception");
			}
			is_db_encrypted = false;
		}
	}

	@Override
	protected void onStop()
	{
		// TODO: Implement this method
		super.onStop();
		// encrypt data
		if(!is_db_encrypted){
			try{
				encryptfile(getApplicationContext(),skey);
			}catch (IOException | NoSuchAlgorithmException | 
			NoSuchPaddingException | InvalidKeyException ex){
				Log.e(null,"Unable to encrypt file throws Exception");
			}
			is_db_encrypted = true;
		}
	}

	@Override
	protected void onRestart()
	{
		// TODO: Implement this method
		super.onRestart();
		//decrypt data
		if(is_db_encrypted){
			try{
				decryptfile(getApplicationContext(),skey);
			}catch (IOException | NoSuchAlgorithmException | 
			NoSuchPaddingException | InvalidKeyException ex){
				Log.e(null,"Unable to decrypt file throws Exception");
			}
			is_db_encrypted = false;
		}
	}
	
	
	
	/*
	This method is used to download the zip files of contact pictures
	and store them in a folder for further use.
	*/
	private void downloadunzip () throws IOException{
		// for now the file is from assets but will be downloaded later
		String PATH = getApplicationContext().getDir("pictures",0).getAbsolutePath() +"/";
		
		InputStream is = getApplicationContext().getAssets().open("ContactsPics.zip");
		ZipInputStream zis = new ZipInputStream(new BufferedInputStream(is));
		try {
			ZipEntry ze;
			byte[] buffer = new byte[1024];
			while ((ze = zis.getNextEntry()) != null){
				
				if (ze.isDirectory()){
					_dirchecker((ze.getName()));
				}
				else{
					FileOutputStream baos = new FileOutputStream(PATH + ze.getName());
					BufferedInputStream in = new BufferedInputStream(zis);
					BufferedOutputStream out = new BufferedOutputStream(baos);
					
					int count;
					while ((count = in.read(buffer)) > 0){
						out.write(buffer,0,count);
					}
					zis.closeEntry();
					out.close();
				}
				
			}
		}finally{
			zis.close();
		}
	}
	
	private void _dirchecker (String dir){
		File f = new File(getApplicationContext().getDir("pictures",0).getAbsolutePath() +"/"+ dir);
		if (!f.isDirectory()){
			f.mkdirs();
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
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
			switch (position){
				case 0:
					return "Contact Search";
				case 1:
					return "Products & Services";
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

	public void copydatabase(Context mcontext){
		
		String dbname = "DenelDB"; 
		File outfile = mcontext.getDatabasePath(dbname); 
		if (outfile.exists()) { 
		return; 
		}
		outfile = mcontext.getDatabasePath(dbname + ".temp");
		// parent directory for his file if it doesn't exist,
		// in this case it returns a false.
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
		String url = "http://enter_link_to_database_file";
		new DownloadFileAsync().execute(url);
	}
	
	private void encryptfile (Context mcontext, SecretKey key) throws IOException, 
	NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException{

		// This will probably change when I will the database will be downloaded from the server
		boolean db_file_exists = mcontext.getDatabasePath(dbname).exists();
		InputStream fis = null;
		File infile = mcontext.getDatabasePath(dbname);
		// check if database file exists to prevent downloading the file each start
		if (db_file_exists){
			fis = new FileInputStream(infile);
		}else {
			fis = mcontext.getAssets().open(dbname);
		}
		// This stream write the encrypted text. This stream will be wrapped by another stream. 
		FileOutputStream fos = new FileOutputStream(mcontext.getDatabasePath(dbname_en).getAbsolutePath());
		// Length is 16 byte // Careful when taking user input!!! http://stackoverflow.com/a/3452620/1188357 
		SecretKeySpec sks = new SecretKeySpec(key.getEncoded(), "AES"); 
		// Create cipher 
		Cipher cipher = Cipher.getInstance("AES"); 
		cipher.init(Cipher.ENCRYPT_MODE, sks); 
		// Wrap the output stream 
		CipherOutputStream cos = new CipherOutputStream(fos, cipher); 
		// Write bytes 
		int b; byte[] d = new byte[8]; 
		while((b = fis.read(d)) != -1) { 
			cos.write(d, 0, b); 
		} // Flush and close streams. 
		cos.flush(); 
		cos.close(); 
		fis.close();
		// delete the decrypted file
		if(infile.exists()){infile.delete();}
	}
	
	private void decryptfile (Context mcontext, SecretKey key) throws IOException, 
	NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException{

		File infile = mcontext.getDatabasePath(dbname_en);
		InputStream fis = new FileInputStream(infile);
		File outfile = mcontext.getDatabasePath(dbname);
		// parent directory for his file if it doesn't exist,
		// in this case it returns a false.
		outfile.getParentFile().mkdirs();
		// This stream write the decrypted text. This stream will be wrapped by another stream. 
		FileOutputStream fos = new FileOutputStream(outfile);
		// Length is 16 byte // Careful when taking user input!!! 
		// http://stackoverflow.com/a/3452620/1188357 
		SecretKeySpec sks = new SecretKeySpec(key.getEncoded(), "AES"); 
		// Create cipher 
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, sks); 
		// Wrap the output stream 
		CipherOutputStream cos = new CipherOutputStream(fos, cipher); 
		// Write bytes 
		int b; byte[] d = new byte[8]; 
		while((b = fis.read(d)) != -1) { 
			cos.write(d, 0, b); 
		} // Flush and close streams. 
		cos.flush(); 
		cos.close(); 
		fis.close();
		// delete the encrypted file
		if(infile.exists()){infile.delete();}
	}
	
	private void updateExternalStorageState(){
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)){
			mExternalStorageAvailable = mExternalStorageWritable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
			mExternalStorageAvailable = true;
			mExternalStorageWritable = false;
		} else {
			mExternalStorageAvailable = mExternalStorageWritable = false;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate main_menu.xml 
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item)
	{
		// TODO: Implement this method
		switch (item.getItemId())
		{
			case R.id.action_settings:
				// add code
				return true;
			case R.id.action_download:
				try{downloadunzip();}catch (IOException e){}
				return true;
			case R.id.action_exit:
				// exit the application
				finish();
				return true;
		}
		return super.onMenuItemSelected(featureId, item);
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
}
