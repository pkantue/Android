package com.denel.facepatrol;

import android.net.Uri;
import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import javax.crypto.*;
import android.text.*;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import java.util.*;
import android.util.Log;

import android.widget.AdapterView.*; // Java encryption of downloaded SQL databases

public class MainActivity extends FragmentActivity implements ActionBar.TabListener 
{
	SQLiteDatabase db;
	public SimpleCursorAdapter cursor_adapter;
	
	static final String dbname = "ContactsDB";
	static final String dbtable = "contacts";
		
	// columns for Overview Table
	static final String contact_name = "name";	
	
	// detail page textview variables
	TextView header,division,dept,title,products,region,work,personal;
	String contact_phone = "+2320343";
	String contact_email = "plus@gmail.com";
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
       	super.onCreate(savedInstanceState);
       	setContentView(R.layout.main);
		
		// Set up the action bar.
        final ActionBar actionBar = getActionBar();
		
		// Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setHomeButtonEnabled(false);

        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		// setup tabs title - EXAMPLE - Doesn't work like that
		//actionBar.addTab(actionBar.newTab().setText("Contact Search"));
		//actionBar.addTab(actionBar.newTab().setText("Denel Organogram"));
		//actionBar.addTab(actionBar.newTab().setText("Denel Products"));
		
		// set main_xml variables
		header = (TextView)findViewById(R.id.header_name);
		division = (TextView)findViewById(R.id.division_name);
		dept = (TextView)findViewById(R.id.dept_name);
		title = (TextView)findViewById(R.id.contact_title);
		products = (TextView)findViewById(R.id.contact_products);
		region = (TextView)findViewById(R.id.contact_region);
		work = (TextView)findViewById(R.id.work_interests);
		personal = (TextView)findViewById(R.id.personal_interests);

		// create a new database with data included
		createDB();
		
		// setup Cursor with all the data in the database
		// put null in 2nd columns argument to collect all columns
		final Cursor db_cursor = db.query(dbtable,null,null,null,null,null,"name");
		
		// return cursor to start position
		if (db_cursor != null){db_cursor.moveToFirst();}
		
		String name = db_cursor.getString(db_cursor.getColumnIndex("name"));
		String surname = db_cursor.getString(db_cursor.getColumnIndex("surname"));

		// set listview to textview
		header.setText(name+ " " + surname);

		division.setText(db_cursor.getString(db_cursor.getColumnIndex("division")));
		dept.setText(db_cursor.getString(db_cursor.getColumnIndex("dept")));
		title.setText(db_cursor.getString(db_cursor.getColumnIndex("title")));
		contact_email = db_cursor.getString(db_cursor.getColumnIndex("email"));
		contact_phone = db_cursor.getString(db_cursor.getColumnIndex("phone"));
		region.setText(db_cursor.getString(db_cursor.getColumnIndex("region")));
		products.setText(db_cursor.getString(db_cursor.getColumnIndex("product")));
		work.setText(db_cursor.getString(db_cursor.getColumnIndex("work_int")));
		personal.setText(db_cursor.getString(db_cursor.getColumnIndex("personal")));
		
		// map the listview (from contact_list.xml) layout with columns of the cursor
		cursor_adapter = new SimpleCursorAdapter(
		this,R.layout.contact_list,
		db_cursor,
		new String[]{"name","surname"},
		new int[]{R.id.contactlistname,R.id.contactlistsurname},
		0);
		
		ListView listview = (ListView) findViewById(R.id.contacts_listview);
		
		// assign adapter to listview
		listview.setAdapter(cursor_adapter);
		
		listview.setOnItemClickListener( new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> listView, View view,
			int position, long id){
				// position cursor to the right result
				Cursor cursor = (Cursor) listView.getItemAtPosition(position);
				
				// get contact details
				String name = cursor.getString(cursor.getColumnIndex("name"));
				String surname = cursor.getString(cursor.getColumnIndex("surname"));

				// set listview to textview
				header.setText(name+ " " + surname);				

				division.setText(cursor.getString(cursor.getColumnIndex("division")));
				dept.setText(cursor.getString(cursor.getColumnIndex("dept")));
				title.setText(cursor.getString(cursor.getColumnIndex("title")));
				contact_email = cursor.getString(cursor.getColumnIndex("email"));
				contact_phone = cursor.getString(db_cursor.getColumnIndex("phone"));
				region.setText(cursor.getString(cursor.getColumnIndex("region")));
				products.setText(cursor.getString(cursor.getColumnIndex("product")));
				work.setText(cursor.getString(cursor.getColumnIndex("work_int")));
				personal.setText(cursor.getString(cursor.getColumnIndex("personal")));
				
			}
		});
		
	   	// identify editext object
		EditText text_search = (EditText)findViewById(R.id.contact_search);
		
		// add listener based on text changed
		text_search.addTextChangedListener(new TextWatcher() {
			
			public void afterTextChanged( Editable s){
				// filter the adapter cursor content based on the changing text
				cursor_adapter.getFilter().filter(s);				
			}
			public void beforeTextChanged(CharSequence s, int start,
			int count, int after){}
			public void onTextChanged(CharSequence s, int start,
			int before, int count){} 
		});
		
		cursor_adapter.setFilterQueryProvider(new FilterQueryProvider(){
			public Cursor runQuery(CharSequence constraint){
				Cursor cursor_query = null;
				// determine if string contains white spaces
				if (constraint.toString().contains(" ")){
					//keywords query
					cursor_query = KeyWordsQuery(constraint.toString());
					cursor_query.moveToFirst();
				}
				else{
					// simple query
					cursor_query = SimpleQuery(constraint.toString());
					cursor_query.moveToFirst();					
				}				
				return cursor_query;
			}
		});
			
	}
	
	public void ContactPhone (View view){
		Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
		phoneIntent.setData(Uri.parse(contact_phone));
		startActivity(Intent.createChooser(phoneIntent,"Phone Number"));
		finish();
	}
	
	public void ContactEmail (View view){
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setData(Uri.parse("mailto:"));		
		emailIntent.putExtra(Intent.EXTRA_EMAIL,new String[]{contact_email});
		emailIntent.setType("message/rfc822");
		startActivity(Intent.createChooser(emailIntent,"Send Email..."));
		finish();
	}

	//close database ondestroy method activated
	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		db.close();
		super.onDestroy();
	}
	
	@Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        //mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate main_menu.xml 
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}
	
	public void createDB()
	{
		// open or create Contact SQL database
		db=openOrCreateDatabase(dbname, Context.MODE_PRIVATE, null);
		
		// delete table before moving on
		db.execSQL("DROP TABLE if exists " + dbtable);
		
		db.execSQL("CREATE TABLE IF NOT EXISTS " + dbtable + 
		"(_id INTEGER PRIMARY KEY AUTOINCREMENT," + 
		"name VARCHAR(255), " + 
		"surname VARCHAR(255), " + 
		"division VARCHAR(255), " + 
		"dept VARCHAR(255), " + 
		"title VARCHAR(255), " + 
		"email VARCHAR(255), " + 
		"phone VARCHAR(255), " + 
		"twitter VARCHAR(255), " + 
		"facebook VARCHAR(255), " + 
		"region, " + 		
		"product TEXT, " +
		"work_int TEXT, " + 		
		"personal TEXT);"); 
		
		// populate database with initial data		
		initialData(); 
	}
	
	public Cursor KeyWordsQuery (String constraint){
		// split string
		String[] split_str = constraint.split(" ");
		Cursor cursor = null;

		// check if there are no null strings
		int count = split_str.length;
		String id_index = "";

		//for (int i=0; i < count; i++){
		//get query for id_index
		cursor = SimpleQuery(split_str[0]);
		int NoRows = cursor.getCount();
		cursor.moveToFirst();
		
		int [] hold_index = getUserId(cursor);
		int [] current_index = getUserId(cursor);
		
		for (int i=0; i < count; i++){
			// user id index
			id_index = getIndex(hold_index,current_index);
			hold_index = new int[current_index.length];
		    hold_index = Arrays.copyOf(current_index,current_index.length);
			// id_index 
			cursor = QueryWithUserID(split_str[i],"_id",id_index);
			current_index = new int[cursor.getCount()];
			current_index = getUserId(cursor);
		}
		
		cursor.moveToFirst();
		
		return cursor;
	}
	
	public int[] getUserId(Cursor cursor){
		int count = cursor.getCount();
		int int_index[] = new int[count];
		
		cursor.moveToFirst();
		
		// get int
		for (int i = 0; i < count; i++){
			
			int_index[i] = cursor.getInt((cursor.getColumnIndex("_id")));
			if(!cursor.isLast()){cursor.moveToNext();}
		}
		return int_index;
	}


	public String getIndex(int a[], int b[] ){

	    // perform common integer comparison
		// to determine number of array length of 
		int temp[] = new int[a.length];
		int k = 0;
		for(int i = 0; i < a.length; i++) { 
			for(int j = 0; j < b.length; j++) { 
				if(a[i] == b[j]) { 
				   k++;
				} 
			} 
		}
		// create correct size of array and run the loop
		// to collect the data
		int z = 0;
		int [] comp_int = new int[k];
		for(int i = 0; i < a.length; i++) { 
			for(int j = 0; j < b.length; j++) { 
				if(a[i] == b[j]) { 
					comp_int[z] = a[i];
					z++;
				} 
			} 
		}

		String index = "(" + String.valueOf(comp_int[0]);
        for(int i = 1; i < comp_int.length; i++) { 
			index +=  "," + String.valueOf(comp_int[i]);
		}
		index += ")";
		return index;
	}	
	
	public Cursor SimpleQuery(String constraint){
		return db.query(dbtable,null,"name like '%" + constraint + "%' " +
						"OR surname like '%" + constraint + "%' " +
						"OR division like '%" + constraint + "%' " +
						"OR dept like '%" + constraint + "%' " +
						"OR title like '%" + constraint + "%' " +
						"OR email like '%" + constraint + "%' " +
						"OR phone like '%" + constraint + "%' " +
						"OR twitter like '%" + constraint + "%' " +
						"OR facebook like '%" + constraint + "%' " +
						"OR region like '%" + constraint + "%' " +
						"OR product like '%" + constraint + "%' " +
						"OR work_int like '%" + constraint + "%' " +
						"OR personal like '%" + constraint + "%' "
						,null,null,null,"name");
	}

	public Cursor QueryWithUserID(String constraint, String group_by,
	String userid_sequence){
		return db.query(dbtable,null,"name like '%" + constraint + "%' " +
						"OR surname like '%" + constraint + "%' " +
						"OR division like '%" + constraint + "%' " +
						"OR dept like '%" + constraint + "%' " +
						"OR title like '%" + constraint + "%' " +
						"OR email like '%" + constraint + "%' " +
						"OR phone like '%" + constraint + "%' " +
						"OR twitter like '%" + constraint + "%' " +
						"OR facebook like '%" + constraint + "%' " +
						"OR region like '%" + constraint + "%' " +
						"OR product like '%" + constraint + "%' " +
						"OR work_int like '%" + constraint + "%' " +
						"OR personal like '%" + constraint + "%' "
						,null,group_by, group_by + " in " + userid_sequence,"name");
	}
	
	public void initialData()
	{
		String dataCols = "(name, surname, division, dept, title, email, phone, region, personal)";
		db.execSQL("INSERT INTO " + dbtable + dataCols + " VALUES('Vinnesh', 'Singh', 'Land Systems', 'Integration', 'Engineer', 'vinnesh@gmail.com','+23593392','All', 'running, reading books, superbikes, motoracing, braaie');");
		db.execSQL("INSERT INTO " + dbtable + dataCols + " VALUES('Paulin', 'Kantu', 'Dynamics', 'Engineering', 'Engineer', 'pkantue@gmail.com','+27834940168','All', 'basketball, braaie, model aircraft, camping');");
		db.execSQL("INSERT INTO " + dbtable + dataCols + " VALUES('Pamela', 'Nozipho', 'DCO', 'Communications', 'External', 'pam.e@gmail.com','+23339392','Middle-East', 'painting, baking, boxing, camping');");
		db.execSQL("INSERT INTO " + dbtable + dataCols + " VALUES('Tebogo', 'Ramaile', 'Aviation', 'Quality', 'Engineer', 'tebogoR@gmail.com','+23321392','Europe', 'running, braaie, motoracing, tennis');");
	}	
}
