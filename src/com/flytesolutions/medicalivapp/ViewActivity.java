package com.flytesolutions.medicalivapp;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

public class ViewActivity extends TabActivity
{
	TabHost tabHost;
	TabWidget tabWidget;

	@Override
   protected void onCreate(Bundle savedInstanceState) 
    {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_viewactivity);


		tabHost = getTabHost();
		tabWidget = tabHost.getTabWidget();
		tabHost.setOnTabChangedListener(null);
		TabHost.TabSpec spec;  
		Intent intent;
		
		// Do the same for the other tabs
		View tabView = createTabView(this, "Current Room");
		intent = new Intent().setClass(this, CurrentRoomActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		spec = tabHost.newTabSpec("tab1").setIndicator(tabView).setContent(intent);
		tabHost.addTab(spec);

		// Create an Intent to launch an Activity for the tab (to be reused)
		intent = new Intent().setClass(this, GroupActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		tabView = createTabView(this, "Group View");
		spec = tabHost.newTabSpec("tab2").setIndicator(tabView).setContent(intent);
		tabHost.addTab(spec);
		
  
    }

	
	 public static View createTabView(Context context, String tabText) 
	 {
	        View view = LayoutInflater.from(context).inflate(R.layout.textview, null, false);
	        TextView tv = (TextView) view.findViewById(R.id.tabTitleText);
	        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
	        tv.setText(tabText);
	        return view;
	 }


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		View view = null;
		if (view instanceof ViewGroup) {
		    for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
		        unbindDrawables(((ViewGroup) view).getChildAt(i));
		    }
		    if (!(view instanceof AdapterView<?>))
		        ((ViewGroup) view).removeAllViews();
		}
	}
	
	private void unbindDrawables(View view) {
	    if (view.getBackground() != null) {
	    view.getBackground().setCallback(null);
	}
	}
	 
	 
}