package com.denel.facepatrol;

import android.app.*;
import android.database.*;
import android.database.sqlite.*;
import android.os.*;
import android.support.v4.app.*;
import android.text.*;
import android.view.*;
import android.widget.*;

import android.support.v4.app.ListFragment;
import android.view.View.*;


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
	
	// The container Activity must implement this 
	// interface so the frag can deliver messages
    public interface onContactItemListener {
        /** Called by HeadlinesFragment when a list item is selected */
        public void onContactSelected(Bundle bundle);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		
		//create database  based on the parent activity
		final ContactsDatabase database = new ContactsDatabase(getActivity());
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
		EditText text_search = (EditText)v.findViewById(R.id.contact_search);

		// add listener based on text changed
		text_search.addTextChangedListener(new TextWatcher() {
				public void afterTextChanged( Editable s){
					// filter the adapter cursor content based on the changing text				
					cursor_adapter.getFilter().filter(s);				
				}
				public void beforeTextChanged(CharSequence s, int start,int count, int after){}
				public void onTextChanged(CharSequence s, int start,int before, int count){} 
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
		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		

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
						//Log.d(null,"filter provider");
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
		
		// if detail on the same view update it or else
		// start contact detail activity
		if(getActivity().findViewById(R.id.contact_details) != null)
		{
			updateContactView(args);
		}
		else
		{
			// start a new activity with a home button if one-pane layout
			//Intent intent = new Intent(getActivity(),ContactPic.class);
			//intent.putExtras(args);
			//startActivity(intent);
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
