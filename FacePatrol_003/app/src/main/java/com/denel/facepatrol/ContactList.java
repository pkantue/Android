package com.denel.facepatrol;

import android.app.*;
import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.graphics.drawable.*;
import android.os.*;
import android.support.v4.app.*;
import android.text.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.widget.AbsListView.*;
import android.widget.AdapterView.*;
import java.io.*;

import android.support.v4.app.ListFragment;
import android.widget.AdapterView.OnItemLongClickListener;


public class ContactList extends ListFragment
{
	onContactItemListener mCallback;
	SQLiteDatabase db;
	Cursor db_cursor;
    SimpleCursorAdapter cursor_adapter;
	TextView header,division,dept,title,products,region,work,personal,birthday;
	public static String contact_phone = "+2320343";
	public static String contact_email = "plus@gmail.com";
	private ListView lv = null;
	public static String [] email_group = null;
	
	// The container Activity must implement this 
	// interface so the frag can deliver messages
    public interface onContactItemListener {
        /** Called by HeadlinesFragment when a list item is selected */
        public void onContactSelected(Bundle bundle);
		public void onGroupEmail(String[] email_grp);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		
		//create database  based on the parent activity
		ContactsDatabase database = new ContactsDatabase(getActivity());
		db_cursor = database.getAllContacts();
		
		// map the listview (from contact_list.xml) layout with columns of the cursor
		cursor_adapter = new SimpleCursorAdapter(
		getActivity(),R.layout.contact_list,
		db_cursor,
		new String[]{"name","surname"},
		new int[]{R.id.contactlistname,R.id.contactlistsurname},
		0);
		// bind List adapter to cursor_adapter
		setListAdapter(cursor_adapter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.contact_search,container,false);
		// identify editext object
		final EditText text_search = (EditText)v.findViewById(R.id.contact_search);
		Button clear_button = (Button)v.findViewById(R.id.clear_button);

		// add listener based on text changed
		text_search.addTextChangedListener(new TextWatcher() {
				public void afterTextChanged( Editable s){
					// filter the adapter cursor content based on the changing text				
					cursor_adapter.getFilter().filter(s);				
				}
				public void beforeTextChanged(CharSequence s, int start,int count, int after){}
				public void onTextChanged(CharSequence s, int start,int before, int count){} 
			});
			
		// add button click listener
		clear_button.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					text_search.setText("");
				}
		});
		
		return v;
	}
	

	@Override
	public void onStart()
	{
		// TODO: Implement this method
		super.onStart();
		// When in two-pane layout, set the listview  to highlight the selected list item
        // (We do this during onStart because at the point the listview is available.)
		// maybe this functionality is not necessary since the contact details will be shown
		
		//create database  based on the parent activity
		final ContactsDatabase database = new ContactsDatabase(getActivity());
		db_cursor = database.getAllContacts();
		Bundle args = new Bundle();
		db_cursor.moveToFirst();
		args = SetContacts(db_cursor);		
		
		lv = getListView();
		// set listview to have multipe selectable items
		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		/*
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
				{
					// TODO: Implement this method
					((ListView) parent).setItemChecked(position, 
					((ListView) parent).isItemChecked(position)); 
					
					return true;
					}
		});
		*/
		lv.setMultiChoiceModeListener(new MultiChoiceModeListener(){

				@Override
				public boolean onCreateActionMode(ActionMode mode, Menu menu)
				{
					// TODO: Implement this method
					mode.getMenuInflater().inflate(R.menu.context_menu,menu);
					mode.setTitle("Select items");
					return true;
				}
				
				@Override
				public void onItemCheckedStateChanged(ActionMode p1, int p2, long p3, boolean p4)
				{
					// TODO: Implement this method
					// Capture total checked items 

					final int checkedCount = lv.getCheckedItemCount();					
					SparseBooleanArray int_array = lv.getCheckedItemPositions();
					email_group = new String[checkedCount];
					for ( int i = 0; i < checkedCount; i++){
						int pos = int_array.keyAt(i);
						Cursor db_cursor = (Cursor) lv.getItemAtPosition(pos);
						email_group[i] = db_cursor.getString(db_cursor.getColumnIndex("email"));
					} 

					p1.setSubtitle(String.valueOf(checkedCount) + " items selected");
				}

				@Override
				public boolean onActionItemClicked(ActionMode mode, MenuItem p2)
				{
					// If settings clicked, log results
					switch (p2.getItemId()){
						case R.id.email_group:
							// close context menu
							final int checkedCount = lv.getCheckedItemCount();
							if (email_group != null)
							{
								if (checkedCount < 120){mCallback.onGroupEmail(email_group);}
								else{Toast.makeText(null,"Number of email recipients exceed 120",Toast.LENGTH_SHORT).show();}
							}
							// to close the contextual action bar
							//mode.finish();
							
							return true;
						case R.id.select_all:
							// enter code
							int count = lv.getCount();
							for (int i=0; i < count; i++)
							{
								lv.setItemChecked(i,true);
							}
							return true;
						default:
							return false;
					}
				}
				
				@Override
				public boolean onPrepareActionMode(ActionMode p1, Menu p2)
				{
					// TODO: Implement this method
					return false;
				}

				@Override
				public void onDestroyActionMode(ActionMode p1)
				{
					// TODO: Implement this method
				}
				
		});
		
		// if detail on the same view update it or else
		// start contact detail activity
		if(getActivity().findViewById(R.id.contact_details) != null)
		{
			updateContactView(args);
		}

		// setup query methods to search through cursor adapter
		cursor_adapter.setFilterQueryProvider(new FilterQueryProvider(){
				public Cursor runQuery(CharSequence constraint){
					Cursor cursor_query = null;
					// determine if string contains white spaces
					if (constraint.toString().contains(" ")){
						//keywords query
						cursor_query = database.KeyWordsQuery(constraint.toString());
						cursor_query.moveToFirst();
					}
					else{
						// simple query
						cursor_query = database.SimpleQuery(constraint.toString());
						cursor_query.moveToFirst();					
					}
					return cursor_query;
				}
		});		
	}	
	
	@Override
	public void onAttach(Activity activity)
	{
		// TODO: Implement this method
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (onContactItemListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
										 + " must implement onContactItemListener");
        }
	}
			
	@Override
	public void onListItemClick(ListView listview, View v, int position, long id)
	{
		// TODO: Implement this method
		super.onListItemClick(listview, v, position, id);
		
		// set putArgs for bundle to be passed unto the 
		// parent activity
		Bundle args = new Bundle();
		
		// position cursor to the right result
		Cursor db_cursor = (Cursor) listview.getItemAtPosition(position);
		
		args = SetContacts(db_cursor);		
		v.setSelected(true);
		// if detail on the same view update it or else
		// start contact detail activity
		if(getActivity().findViewById(R.id.contact_details) != null)
		{
			updateContactView(args);
		}
		else
		{
			// start a new activity with a home button if one-pane layout
			Intent intent = new Intent(getActivity(),ContactPic.class);
			intent.putExtras(args);
			startActivity(intent);
		}
		// Notify the parent activity of selected contact's details
		mCallback.onContactSelected(args);
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
		birthday = (TextView)getActivity().findViewById(R.id.contact_birthday);
		
		ImageView img = (ImageView)getActivity().findViewById(R.id.contact_picture);
		String pathName = getActivity().getApplicationContext().getDir("pictures",0).getAbsolutePath() +"/";
		String filename = bundle.getString("name") + " " + bundle.getString("surname") +".png";
		Log.d(null,pathName + filename);
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
	}	
	
	private Bundle SetContacts (Cursor db_cursor)
	{
		Bundle args = new Bundle();		
		
		args.putString("name",db_cursor.getString(db_cursor.getColumnIndex("name")));
		args.putString("surname",db_cursor.getString(db_cursor.getColumnIndex("surname")));
		args.putString("division",db_cursor.getString(db_cursor.getColumnIndex("division")));
		args.putString("dept",db_cursor.getString(db_cursor.getColumnIndex("dept")));
		args.putString("title",db_cursor.getString(db_cursor.getColumnIndex("title")));
		args.putString("email",db_cursor.getString(db_cursor.getColumnIndex("email")));
		args.putString("phone",db_cursor.getString(db_cursor.getColumnIndex("phone")));
		args.putString("region",db_cursor.getString(db_cursor.getColumnIndex("region")));
		args.putString("product",db_cursor.getString(db_cursor.getColumnIndex("product")));
		args.putString("work_int",db_cursor.getString(db_cursor.getColumnIndex("work_int")));
		args.putString("personal",db_cursor.getString(db_cursor.getColumnIndex("personal")));
		args.putString("birthday",db_cursor.getString(db_cursor.getColumnIndex("birthday")));
		
		return args;
	}
}
