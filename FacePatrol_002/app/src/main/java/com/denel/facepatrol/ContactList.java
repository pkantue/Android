package com.denel.facepatrol;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter; // change to cursoradapter
import android.widget.ListView;
import android.widget.*;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.*;
import javax.crypto.*;
import android.util.*;
import android.view.*;
import android.text.*;


public class ContactList extends ListFragment 
{
	onContactItemListener mCallback;
	SQLiteDatabase db;
	Cursor db_cursor;
    SimpleCursorAdapter cursor_adapter;
		
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
		View v = inflater.inflate(R.layout.fragment_list,container,false);
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
		

		// setup query methods to search through cursor adapter
		cursor_adapter.setFilterQueryProvider(new FilterQueryProvider(){
				public Cursor runQuery(CharSequence constraint){
					Cursor cursor_query = null;
					// determine if string contains white spaces
					if (constraint.toString().contains(" ")){
						//keywords query
						cursor_query = database.KeyWordsQuery(constraint.toString());
						cursor_query.moveToFirst();
						Log.d(null,"filter provider");
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
		
		// Notify the parent activity of selected item
		mCallback.onContactSelected(args);

	}
}
