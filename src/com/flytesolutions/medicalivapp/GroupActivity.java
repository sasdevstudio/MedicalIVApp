package com.flytesolutions.medicalivapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import cam.flytesolutions.groupview.GetDataFromDB;
import cam.flytesolutions.groupview.Users;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class GroupActivity extends Activity {

	String data = "";
	TableLayout tl;
	TableRow tr;
	TextView label,email;
	ArrayList<Users> users ;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_groupview);

		tl = (TableLayout) findViewById(R.id.maintable);
		email = (TextView) findViewById(R.id.tv_username);
		email.setText(LoginActivity.EmailAddress);
		start();
	}

	private void start() {
		final GetDataFromDB getdb = new GetDataFromDB();
		new Thread(new Runnable() {
			public void run() {
				
				 Date dNow = new Date( );
				 SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
			     String date = ft.format(dNow);
				
				//String email = "1345@gmail.com";
				String email = LoginActivity.EmailAddress;
				data = getdb.getDataFromDB(email, date);
			
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						ArrayList<Users> users = parseJSON(data);
						addData(users);						
					}
				});
				
			}
		}).start();
	}

	public ArrayList<Users> parseJSON(String result) {
		ArrayList<Users> users = new ArrayList<Users>();
		try {
			JSONArray jArray = new JSONArray(result);
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject json_data = jArray.getJSONObject(i);
				Users user = new Users();
				user.setId(json_data.getInt("id"));
				user.setName(json_data.getString("roomno"));
				user.setPlace(json_data.getString("timer"));
				users.add(user);
			}
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());  
		}
		return users;
	}

	void addHeader(){
		/** Create a TableRow dynamically **/
		tr = new TableRow(this);

		/** Creating a TextView to add to the row **/
		label = new TextView(this);
		label.setText("Room No #");
		label.setTextColor(Color.BLACK);
		label.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		label.setPadding(5, 5, 5, 5);
		label.setBackgroundColor(Color.GRAY);
		
		LinearLayout Ll = new LinearLayout(this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 2, 2, 2);
		//Ll.setPadding(10, 5, 5, 5);
		Ll.addView(label,params);
		tr.addView((View)Ll); // Adding textView to tablerow.

		/** Creating Qty Button **/
		TextView place = new TextView(this);
		place.setText("Time Remaining (HH:MM)");
		place.setTextColor(Color.BLACK);
		place.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		place.setPadding(5, 5, 5, 5);
		place.setBackgroundColor(Color.GRAY);
		Ll = new LinearLayout(this);
		params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 2, 2, 2);
		//Ll.setPadding(10, 5, 5, 5);
		Ll.addView(place,params);
		tr.addView((View)Ll); // Adding textview to tablerow.

		
		 // Add the TableRow to the TableLayout
        tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	}
	
	@SuppressWarnings({ "rawtypes" })
	public void addData(ArrayList<Users> users) {

		addHeader();
		
		for (Iterator i = users.iterator(); i.hasNext();) {

			Users p = (Users) i.next();

			/** Create a TableRow dynamically **/
			tr = new TableRow(this);

			/** Creating Qty Button **/
			TextView place = new TextView(this);
			place.setText(p.getPlace());
			
			
			/** Creating a TextView to add to the row **/
			label = new TextView(this);
			label.setText(p.getName());
			
			label.setId(p.getId());
			label.setTextColor(Color.BLACK);
			label.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
			label.setPadding(5, 5, 5, 5);
			
			
			if(p.getPlace().equals("00:00"))
			{
				place.setBackgroundColor(Color.RED);
				label.setBackgroundColor(Color.RED);
			}
			else if(p.getPlace().equals("00:01") || p.getPlace().equals("00:02") || p.getPlace().equals("00:03") || p.getPlace().equals("00:04") || p.getPlace().equals("00:05") || p.getPlace().equals("00:06") || p.getPlace().equals("00:07") || p.getPlace().equals("00:08") || p.getPlace().equals("00:09") || p.getPlace().equals("00:10"))
			{
				place.setBackgroundColor(Color.parseColor("#FF7F00"));
				label.setBackgroundColor(Color.parseColor("#FF7F00"));
			}
			else
			{
				place.setBackgroundColor(Color.parseColor("#CCCCCC"));
				label.setBackgroundColor(Color.parseColor("#CCCCCC"));
			}
			
			
			LinearLayout Ll = new LinearLayout(this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
			params.setMargins(0, 2, 2, 2);
			//Ll.setPadding(10, 5, 5, 5);
			Ll.addView(label,params);
			tr.addView((View)Ll); // Adding textView to tablerow.

			
			place.setTextColor(Color.BLACK);
			place.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
			place.setPadding(5, 5, 5, 5);
			Ll = new LinearLayout(this);
			params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
			params.setMargins(0, 2, 2, 2);
			//Ll.setPadding(10, 5, 5, 5);
			Ll.addView(place,params);
			tr.addView((View)Ll); // Adding textview to tablerow.

			 // Add the TableRow to the TableLayout
            tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		}
	}
}
