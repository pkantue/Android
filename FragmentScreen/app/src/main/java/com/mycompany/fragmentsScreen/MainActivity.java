package com.mycompany.fragmentsScreen;

import android.os.Bundle; 
import android.app.Activity; 
import android.app.FragmentManager; 
import android.app.FragmentTransaction; 
import android.content.res.Configuration; 
import android.view.WindowManager;
import android.view.*;
import android.app.Fragment;

public class MainActivity extends Activity {

	@Override 
	protected void onCreate(Bundle savedInstanceState) { 
	super.onCreate(savedInstanceState);

		Configuration config = getResources().getConfiguration();

		FragmentManager fragmentManager = getFragmentManager(); 
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

		/** * Check the device orientation and act accordingly */
		if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) { 
		/** * Landscape mode of the device */
		LM_Fragment ls_fragment = new LM_Fragment(); 
		fragmentTransaction.replace(android.R.id.content, ls_fragment); 
		}
		else{ 
		/** * Portrait mode of the device */
		PM_Fragment pm_fragment = new PM_Fragment(); 
		fragmentTransaction.replace(android.R.id.content, pm_fragment); 
		} 
		fragmentTransaction.commit(); 
	}
	
	public class LM_Fragment extends Fragment{
		
		/*Default constructor. Every fragment must have an empty constructor, 
		so it can be instantiated when restoring its activity's state. 
		It is strongly recommended that subclasses do not have other
		constructors with parameters, since these constructors 
		will not be called when the fragment is re-instantiated */
		
		public LM_Fragment(){}
		
		@Override 
		public View onCreateView(LayoutInflater inflater, 
		ViewGroup container, Bundle savedInstanceState) { 
		/** * Inflate the layout for this fragment */
			return inflater.inflate( R.layout.lm_fragment, container, false); 
		}
	}
	
	public class PM_Fragment extends Fragment{ 

		/*Default constructor. Every fragment must have an empty constructor, 
		 so it can be instantiated when restoring its activity's state. 
		 It is strongly recommended that subclasses do not have other
		 constructors with parameters, since these constructors 
		 will not be called when the fragment is re-instantiated */

		public PM_Fragment(){}
		
		@Override 
		public View onCreateView(LayoutInflater inflater, 
		ViewGroup container, Bundle savedInstanceState) { 
		/** * Inflate the layout for this fragment */
			return inflater.inflate( R.layout.pm_fragment, container, false); 
		} 	
	}	

}
