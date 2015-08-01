package com.denel.facepatrol;

import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.text.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import java.io.*;
import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.spec.*;

public class PasswordInput extends Activity
{
	Button submit;
	EditText pass_submit;
	EditText pass_confirm;
	TextView internet_text,forgot_pass;
	String pass1_text;
	String pass2_text;	
	Boolean online_flag;
	Context mycontext;
	boolean encryptDatabaseExists = false;
	String passw_input = null;
	SecretKey skey = null;
	String dbname = "DenelDB";
	String dbname_en = "EncryptDB";	
	int passw_count = 0;
	ImageView pass_help;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		
		// Request window with no title bar
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		// check if encrypt file exists
		File outfile = getApplicationContext().getDatabasePath(dbname_en); 		
		if (outfile.exists()){
			encryptDatabaseExists = true;
			//delete 'denelDB' file in the event it exists - Housekeeping
			//File infile_err = getApplicationContext().getDatabasePath(dbname);
			//if(infile_err.exists()){infile_err.delete();}
		}
		
		// show different layout 
		if (encryptDatabaseExists){
			setContentView(R.layout.password_login);
			forgot_pass = (TextView)findViewById(R.id.login_forgot_password);
		}else{
			setContentView(R.layout.password_input);
			pass_confirm = (EditText)findViewById(R.id.passwordconfirm);
			
			pass_help = (ImageView)findViewById(R.id.passwordinput_about);
			pass_help.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View p1)
					{
						AlertDialog.Builder builder = new AlertDialog.Builder(PasswordInput.this); 
						builder.setTitle("Password Recommendation")
							.setMessage("Enter at least 4-digit password") 
							.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
								public void onClick(DialogInterface dialog, int id) { 
									// FIRE ZE MISSILES! 
								} });
						builder.show();
					}				
			});
		}
		
		submit = (Button)findViewById(R.id.passwordSubmit);
		pass_submit = (EditText)findViewById(R.id.passwordinput);
		internet_text = (TextView)findViewById(R.id.passwordinputTextView1);
			
		online_flag = isOnline();
		if(online_flag == true){
			internet_text.setText("");
		}
		else{
			internet_text.setText("Unable to connect to server. Please check your network connection");
		}
		
		if (encryptDatabaseExists){
		forgot_pass.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View p1){
				AlertDialog.Builder builder = new AlertDialog.Builder(PasswordInput.this); 
				builder.setTitle("Application Security and Access")
					.setMessage(R.string.password_help) 
					.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
						public void onClick(DialogInterface dialog, int id) { 
							// FIRE ZE MISSILES! 
						} });
				builder.show();
			}
		});
		}

		pass_submit.addTextChangedListener(new TextWatcher() {

				@Override
				public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4)
				{
					// TODO: Implement this method
				}

				@Override
				public void onTextChanged(CharSequence p1, int p2, int p3, int p4)
				{
					// TODO: Implement this method
				}

				@Override
				public void afterTextChanged(Editable p1)
				{
					// TODO: Implement this method
					pass1_text = pass_submit.getText().toString();
				}
		});
		
		if (!encryptDatabaseExists){
			pass_confirm.addTextChangedListener(new TextWatcher() {

				@Override
				public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4)
				{
					// TODO: Implement this method
				}

				@Override
				public void onTextChanged(CharSequence p1, int p2, int p3, int p4)
				{
					// TODO: Implement this method
				}

				@Override
				public void afterTextChanged(Editable p1)
				{
					// TODO: Implement this method
					pass2_text = pass_confirm.getText().toString();
				}
			});
		}

		submit.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View p1)
			{
				// TODO: Implement this method	
				if (encryptDatabaseExists){	
				    File dbfile_en = getApplicationContext().getDatabasePath(dbname_en);
					File dbfile = getApplicationContext().getDatabasePath(dbname);
					// decrypt database based on password and 
					// test to see if database is readbable
					// 1 - create secret key based on password
					if (pass1_text != null){
						try{
							skey = generateKey(pass1_text.toCharArray(),"sdf764rew51sdr98d2".getBytes());
						}catch(NoSuchAlgorithmException | InvalidKeySpecException f)
						{Log.e(null,"Unable to generate key throws Exception");}
						// decrypt file
						try{
							decryptfile(getApplicationContext(),skey);
						}catch (IOException | NoSuchAlgorithmException | 
						NoSuchPaddingException | InvalidKeyException ex){
							Log.e(null,"Unable to decrypt file throws Exception");
						}
						// check if decryption is successful as encrypted file would have 
						// been deleted,
						if(dbfile_en.exists())
						{
							// later use an alert dialog!!!
							Toast.makeText(getApplicationContext(),
							"Wrong Password - Database could not be decrypted",Toast.LENGTH_LONG).show();
							dbfile.delete();
						}else{
							Intent i = new Intent(PasswordInput.this,MainActivity.class);
							i.putExtra("passw",pass1_text);
							startActivity(i);
							finish();
						}
					}
				}else{
				
					if(pass1_text.equals(pass2_text))
					{
						// download the database if internet
						if (online_flag == true){
							
							// download file and copy to internal directory
							copydatabase(getApplicationContext());
							Intent i = new Intent(PasswordInput.this,MainActivity.class);
							i.putExtra("passw",pass1_text);
							startActivity(i);
							finish();
						}
					}
					else{
						Toast.makeText(getApplicationContext(), "Incorrect Passwords! Enter Again",Toast.LENGTH_SHORT).show();
						pass_confirm.setText("");
						pass_submit.setText("");
					}
				}

			}
		});
			
	}
	
	// check if the device is online before making getting to the app
	public boolean isOnline() {
		ConnectivityManager cm =
			(ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
		return cm.getActiveNetworkInfo() != null &&  cm.getActiveNetworkInfo().isConnectedOrConnecting();
	}

	@Override
	protected void onStart()
	{
		// TODO: Implement this method
		super.onStart();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate main_menu.xml 
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.login_menu, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item)
	{
		// TODO: Implement this method
		switch (item.getItemId())
		{
			case R.id.login_help:
				AlertDialog.Builder builder = new AlertDialog.Builder(this); 
				builder.setTitle("Application Security and Access")
				.setMessage(R.string.password_help) 
				.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
					public void onClick(DialogInterface dialog, int id) { 
				// FIRE ZE MISSILES! 
				} });
				//.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { 
				//	public void onClick(DialogInterface dialog, int id) { 
				// User cancelled the dialog 
				//} });
				builder.show();
				// exit the application
				//finish();
				return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	public void copydatabase(Context mcontext){

		File outfile = mcontext.getDatabasePath(dbname); 
		if (outfile.exists()) { 
			return; 
		}
		outfile = mcontext.getDatabasePath(dbname + ".temp");
		// parent directory for his file if it doesn't exist,
		// in this case it returns a false.
		outfile.getParentFile().mkdirs();
		try{			
		    // This will change once we download the file!!!!
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
