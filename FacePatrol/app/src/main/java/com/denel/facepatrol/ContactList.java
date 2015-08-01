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


public class ContactList extends ListFragment 
{
	onContactItemListener mCallback;
	
	// The container Activity must implement this 
	// interface so the frag can deliver messages
    public interface onContactItemListener {
        /** Called by HeadlinesFragment when a list item is selected */
        public void onContactSelected(int position);
    }

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		
		// Create an array adapter for the 
		// list View, using the sql database
		// In this case in needs to be a simpleCursorAdapter 
		// for SQL implementation
	}

	@Override
	public void onStart()
	{
		// TODO: Implement this method
		super.onStart();
		
		// When in two-pane layout, set the listview 
		// to highlight the selected list item
        // (We do this during onStart because at the point 
		// the listview is available.)
		
		
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
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		// TODO: Implement this method
		super.onListItemClick(l, v, position, id);
		// Notify the parent activity of selected item
		mCallback.onContactSelected(position);

        // Set the item as checked to be highlighted 
		// when in two-pane layout
		getListView().setItemChecked(position,true);
	}
}
