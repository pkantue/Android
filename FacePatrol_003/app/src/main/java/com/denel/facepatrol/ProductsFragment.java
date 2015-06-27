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

	ImageView dyna,dls,mechem,pmp,ism,dta,aero,avia,otr,prop;
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
		
		dyna = (ImageView)getView().findViewById(R.id.productsoverview_dynamics);
		dls = (ImageView)getView().findViewById(R.id.productsoverview_landsys);
		mechem = (ImageView)getView().findViewById(R.id.productsoverview_mechem);
		pmp = (ImageView)getView().findViewById(R.id.productsoverview_pmp);
		aero = (ImageView)getView().findViewById(R.id.productsoverview_aero);
		avia = (ImageView)getView().findViewById(R.id.productsoverview_aviation);
		ism = (ImageView)getView().findViewById(R.id.productsoverview_ism);
		otr = (ImageView)getView().findViewById(R.id.productsoverview_otr);
		dta = (ImageView)getView().findViewById(R.id.productsoverview_dta);
		prop = (ImageView)getView().findViewById(R.id.productsoverview_prop);
		
		dyna.setOnClickListener(new OnClickListener() {
				public void onClick(View v){
					// start activity
					Intent intent = new Intent(getActivity(),dummypage.class);
					intent.putExtra("title","Dynamics");
					startActivity(intent);
				}
			});
		
		avia.setOnClickListener(new OnClickListener() {
				public void onClick(View v){
					// start activity
					Intent intent = new Intent(getActivity(),dummypage.class);
					intent.putExtra("title","Aviation");
					startActivity(intent);
				}
			});
		
		dls.setOnClickListener(new OnClickListener() {
				public void onClick(View v){
					// start activity
					Intent intent = new Intent(getActivity(),dummypage.class);
					intent.putExtra("title","Land Systems");
					startActivity(intent);
				}
		});
		
		mechem.setOnClickListener(new OnClickListener() {
				public void onClick(View v){
					// start activity
					Intent intent = new Intent(getActivity(),dummypage.class);
					intent.putExtra("title","Mechem");
					startActivity(intent);
				}
			});
			
		pmp.setOnClickListener(new OnClickListener() {
				public void onClick(View v){
					// start activity
					Intent intent = new Intent(getActivity(),dummypage.class);
					intent.putExtra("title","PMP");
					startActivity(intent);
				}
			});
			
		aero.setOnClickListener(new OnClickListener() {
				public void onClick(View v){
					// start activity
					Intent intent = new Intent(getActivity(),dummypage.class);
					intent.putExtra("title","Aerostructures");
					startActivity(intent);
				}
			});
			
		ism.setOnClickListener(new OnClickListener() {
				public void onClick(View v){
					// start activity
					Intent intent = new Intent(getActivity(),dummypage.class);
					intent.putExtra("title","ISM");
					startActivity(intent);
				}
			});
			
		otr.setOnClickListener(new OnClickListener() {
				public void onClick(View v){
					// start activity
					Intent intent = new Intent(getActivity(),dummypage.class);
					intent.putExtra("title","OTR");
					startActivity(intent);
				}
			});
		
		dta.setOnClickListener(new OnClickListener() {
				public void onClick(View v){
					// start activity
					Intent intent = new Intent(getActivity(),dummypage.class);
					intent.putExtra("title","Training Academy");
					startActivity(intent);
				}
			});
		
		prop.setOnClickListener(new OnClickListener() {
				public void onClick(View v){
					// start activity
					Intent intent = new Intent(getActivity(),dummypage.class);
					intent.putExtra("title","Properties");
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
