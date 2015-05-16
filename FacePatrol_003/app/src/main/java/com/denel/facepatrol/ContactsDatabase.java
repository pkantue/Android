package com.denel.facepatrol;

import android.content.*;
import android.database.sqlite.*;
import android.support.v4.widget.*;
import android.widget.*;

import android.support.v4.widget.SimpleCursorAdapter;
import android.database.*;
import java.util.*;
import android.util.*;

public class ContactsDatabase extends SQLiteOpenHelper
{
	public SimpleCursorAdapter cursor_adapter;

	static final String dbname = "ContactsDB";
	static final String dbtable = "contacts_denel"; //contacts_example

	// columns for Overview Table
	static final String contact_name = "name";
	
	public ContactsDatabase(Context context) 
	{
		super(context, dbname, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		// TODO: Implement this method
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
		"region TEXT, " + 		
		"product TEXT, " +
		"work_int TEXT, " + 
		"personal  TEXT, " +		
		"birthday TEXT);");
		
		initialData(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int p2, int p3)
	{
		// TODO: Implement this method
		// delete table before moving on
		db.execSQL("DROP TABLE if exists " + dbtable);
		
		this.onCreate(db);
	}
	
	// get all contacts into cursor
	public Cursor getAllContacts ()
	{
		SQLiteDatabase db = this.getReadableDatabase();
		String table_found = db.findEditTable(dbtable);
		//if (table_found != null)
		//{
		Log.d("tag","Table found: " + dbtable);
		//}
		return db.query(dbtable,null,null,null,null,null,"name");
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

	private int[] getUserId(Cursor cursor){
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


	private String getIndex(int a[], int b[] ){

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
		SQLiteDatabase db = this.getReadableDatabase();
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
						"OR personal like '%" + constraint + "%' " +
						"OR birthday like '%" + constraint + "%' "
						,null,null,null,"name");
	}

	private Cursor QueryWithUserID(String constraint, String group_by,
	String userid_sequence){
		SQLiteDatabase db = this.getReadableDatabase();
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
						"OR personal like '%" + constraint + "%' " +
						"OR birthday like '%" + constraint + "%' "
						,null,group_by, group_by + " in " + userid_sequence,"name");
	}
	
	private void initialData(SQLiteDatabase db)
	{
		String dataCols = "(name, surname, division, dept, title, email, phone, region, personal, birthday)";
		db.execSQL("INSERT INTO " + dbtable + dataCols + " VALUES('Vinnesh', 'Singh', 'Land Systems', 'Integration', 'Engineer', 'vinnesh@gmail.com','+23593392','Americas', 'running, reading books, superbikes, motoracing, braaie','20-Aug');");
		db.execSQL("INSERT INTO " + dbtable + dataCols + " VALUES('Paulin', 'Kantu', 'Dynamics', 'Engineering', 'Flight Control Engineer', 'pkantue@gmail.com','+27834940168','All', 'basketball, braaie, model aircraft, camping','9-Sept');");
		db.execSQL("INSERT INTO " + dbtable + dataCols + " VALUES('Pamela', 'Nozipho', 'DCO', 'Communications', 'External Specialist', 'pam.e@gmail.com','+23339392','Middle-East', 'painting, baking, boxing, camping','23-Nov');");
		db.execSQL("INSERT INTO " + dbtable + dataCols + " VALUES('Tebogo', 'Ramaile', 'Aviation', 'Quality', 'Quality Engineer', 'tebogoR@gmail.com','+23321392','Europe', 'running, braaie, motoracing, tennis','12-Jul');");
		db.execSQL("INSERT INTO " + dbtable + dataCols + " VALUES('Marlene', 'Nel', 'Land Systems', 'Finance', 'Manager', 'MarleneNel@gmail.com','+0033234','South-East Asia', 'books, braaie, baking, river-rafting','6-Jun');");
		db.execSQL("INSERT INTO " + dbtable + dataCols + " VALUES('Geraldine', 'Pillay', 'PMP', 'Legal', 'Industrial Manager', 'geralPillay@gmail.com','+24321595','South Africa', 'squash, sky-diving, running, movies','9-Mar');");
	}
}
