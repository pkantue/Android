package com.denel.facepatrol;

import android.content.*;
import android.os.*;
import android.support.v4.app.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.text.*;

public class ProductsFragment extends Fragment
{

	GridLayout img;
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
		View v = inflater.inflate(R.layout.products_overview,container,false);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onActivityCreated(savedInstanceState);
		img = (GridLayout) getView().findViewById(R.id.productsoverviewGridLayout);
		img.setOnClickListener(new OnClickListener() {
			public void onClick(View v){
				// start activity
				Intent intent = new Intent(getActivity(),dummypage.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onStop()
	{
		// TODO: Implement this method
		super.onStop();
	}
	
	
}
