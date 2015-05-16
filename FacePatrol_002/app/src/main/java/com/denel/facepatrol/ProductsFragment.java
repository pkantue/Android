package com.denel.facepatrol;

import android.support.v4.app.*;
import android.os.*;
import android.view.*;

public class ProductsFragment extends Fragment
{

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.dummy_page,container,false);
		return v;
	}

	@Override
	public void onStop()
	{
		// TODO: Implement this method
		super.onStop();
	}
	
	
}
