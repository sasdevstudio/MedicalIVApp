package com.flytesolutions.medicalivapp;

import java.util.UUID;
import org.json.JSONObject;
import com.flytesolutions.library.UserFunctions;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;


public class CurrentRoomActivity extends Activity implements OnClickListener
{
	LinearLayout layout_timer, layout_start_reset, lv_bac;
	EditText et_room_number, et_rate, et_vtbi;
	TextView tv_timer;
	Button btn_start_timer, btn_reset_timer, btn_pause_timer, btn_restart_timer;
	String room_number = "", rate = "", vtbi = "", timer = "", uniqueID = "";
	long total = 0, totalBac = 0, tag = 0;
	public CountDownTimer countDownTimer; 
	//private long totalTimeCountInMilliseconds;
	private long timeBlinkInMilliseconds;
	private boolean blink; 
	ViewSwitcher roomno_switcher, rate_switcher, vtbi_switcher;
	private boolean check = false;
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) 
    {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_currentroom);
	
		//getWindow().getDecorView().setBackgroundColor(Color.RED);
		
		uniqueID = UUID.randomUUID().toString();
		
		lv_bac = (LinearLayout) findViewById(R.id.lv_bac);
		
		layout_timer = (LinearLayout) findViewById(R.id.layout_timer);
		layout_start_reset = (LinearLayout) findViewById(R.id.layout_start_reset);
		
		et_room_number = (EditText) findViewById(R.id.et_room_number);
		et_rate = (EditText) findViewById(R.id.et_rate);
		et_vtbi = (EditText) findViewById(R.id.et_vtbi);
		
		tv_timer = (TextView) findViewById(R.id.tv_timer);
		
		
		btn_start_timer = (Button) findViewById(R.id.btn_start_timer);
		btn_pause_timer = (Button) findViewById(R.id.btn_pause_timer);
		btn_restart_timer = (Button) findViewById(R.id.btn_restart_timer);
		btn_reset_timer = (Button) findViewById(R.id.btn_reset_timer);
		
		roomno_switcher = (ViewSwitcher) findViewById(R.id.roomno_switcher);
		rate_switcher = (ViewSwitcher) findViewById(R.id.rate_switcher);
		vtbi_switcher = (ViewSwitcher) findViewById(R.id.vtbi_switcher);
		
		layout_timer.setVisibility(LinearLayout.GONE);
		//layout_start_reset.setVisibility(LinearLayout.GONE);

		btn_start_timer.setOnClickListener(this);
		btn_pause_timer.setOnClickListener(this);
		btn_restart_timer.setOnClickListener(this);
		btn_reset_timer.setOnClickListener(this);
    }
	
	@Override
	public void onClick(View v)
    {
		room_number = et_room_number.getText().toString();
		rate = et_rate.getText().toString();
		vtbi = et_vtbi.getText().toString();
		if(!room_number.equals("") && !rate.equals("") && !vtbi.equals(""))
		{
	    	if (v.getId() == R.id.btn_start_timer)
	    	{
	    		btn_pause_timer.setVisibility(View.VISIBLE);
	    		btn_start_timer.setVisibility(View.GONE);
	    		btn_restart_timer.setVisibility(View.GONE);
	    		
				int time = calculateTime();
				layout_timer.setVisibility(LinearLayout.VISIBLE);
				tv_timer.setTextAppearance(getApplicationContext(),R.style.normalText);
				//totalTimeCountInMilliseconds = 60 * time*1000;
				total = Long.valueOf(time) * 60 * 1000;
				timeBlinkInMilliseconds = 30 * 20 * 1000;
				
				// Here room number switcher change works
				roomno_switcher.showNext();
				TextView tv_room_number = (TextView) roomno_switcher.findViewById(R.id.tv_room_number);
				tv_room_number.setText(room_number);
				
				rate_switcher.showNext(); 
				TextView tv_rate_number = (TextView) rate_switcher.findViewById(R.id.tv_rate);
				tv_rate_number.setText(et_rate.getText().toString());

				vtbi_switcher.showNext(); 
				TextView tv_vtbi_number = (TextView) vtbi_switcher.findViewById(R.id.tv_vtbi);
				tv_vtbi_number.setText(et_vtbi.getText().toString());
				
				startTimer();
	    	}
	    	else if (v.getId() == R.id.btn_pause_timer)
	    	{
	    		try
	        	{
	    			btn_pause_timer.setVisibility(View.GONE);
		    		btn_start_timer.setVisibility(View.GONE);
		    		btn_restart_timer.setVisibility(View.VISIBLE);
		    		
		    		total = totalBac;
		    		countDownTimer.cancel();
	
	        	}
	    		catch(NullPointerException ex)
	    	    {
	    	       	ex.printStackTrace();
	    	       	Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
	    	    }
 	    		catch(Exception ex)
	    	    {
	    	       	ex.printStackTrace();
	    	       	Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
	    	    }
	    	}
	    	else if (v.getId() == R.id.btn_restart_timer)
	    	{
	    		btn_pause_timer.setVisibility(View.VISIBLE);
	    		btn_start_timer.setVisibility(View.GONE);
	    		btn_restart_timer.setVisibility(View.GONE);
	    		startTimer();
	    	}
	    	else if (v.getId() == R.id.btn_reset_timer)
	    	{
	    		if (btn_start_timer.getVisibility() != View.VISIBLE)
	    		{
		    		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
			    	{
				    	@Override
				    	public void onClick(DialogInterface dialog, int which) 
				    	{
				    	    switch (which)
				    	    {	
				    	    	case DialogInterface.BUTTON_POSITIVE:
				    	    		try
					    	        {
				    	    			roomno_switcher.showNext();
						    	        rate_switcher.showNext();
						    	        vtbi_switcher.showNext();
						    	        total = 0;
						    	        layout_timer.setVisibility(LinearLayout.GONE);
						    	        
						    	        btn_pause_timer.setVisibility(View.GONE);
							    		btn_start_timer.setVisibility(View.VISIBLE);
							    		btn_restart_timer.setVisibility(View.GONE);
							    		
						    	        et_room_number.setText("");
						    	        et_rate.setText("");
						    	        et_vtbi.setText("");
						    	        check = false;
						    	        tag = 0;
						    	        countDownTimer.cancel();
						    	        uniqueID = UUID.randomUUID().toString();
					    	        }
					    	        catch(NullPointerException ex)
					    	        {
					    	        	ex.printStackTrace();
					    	        	Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
					    	        }
				    	    		catch(Exception ex)
					    	        {
					    	        	ex.printStackTrace();
					    	        	Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
					    	        }
					    	        break;
				    	        case DialogInterface.BUTTON_NEGATIVE:
				    	        		//No button clicked
					    	            break;
				    	    }
				    	}
			    	};
			    	AlertDialog.Builder builder = new AlertDialog.Builder(CurrentRoomActivity.this);
			    	builder.setMessage("Are you sure you want to reset this paitent's Rate/VTBI Timer??").setPositiveButton("Yes, Reset", dialogClickListener).setNegativeButton("No, Cancel", dialogClickListener).show();
		    	}
	    	}
		}
		else if(room_number.equals("") && rate.equals("") && vtbi.equals(""))
		{
			Toast.makeText(getApplicationContext(), "Please fill up the fields first!!!.", Toast.LENGTH_LONG).show();
		}
    }
	

	private void startTimer() 
	{
		countDownTimer = new CountDownTimer(total, 1000) {

			public void onTick(long millisUntilFinished) 
            {
				try
				{
					totalBac = millisUntilFinished;
		            new ProcessRegister().execute(); ////////saving data to the database
		          	if (millisUntilFinished < timeBlinkInMilliseconds) 
					{
						tv_timer.setTextAppearance(getApplicationContext(),R.style.blinkText);
						if (blink)
						{
							tv_timer.setVisibility(View.VISIBLE);
							//btn_reset_timer.setBackgroundColor(Color.parseColor("#B6D7A8"));
							btn_reset_timer.setBackgroundResource(R.drawable.button_curve_green);
							lv_bac.setBackgroundColor(Color.parseColor("#FF7F00"));
		            		//final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
		            	     //tg.startTone(ToneGenerator.TONE_PROP_BEEP);
						} 
						else 
						{
							tv_timer.setVisibility(View.INVISIBLE);
						}
						blink = !blink; 
					}
					timer = String.format("%02d", ((millisUntilFinished / (1000*60*60)) % 24)) + ":" + String.format("%02d", ((millisUntilFinished / (1000*60)) % 60));
					//long s = millisUntilFinished/1000;
					//tv_timer.setText(String.format("%02d", s / 60) + ":" + String.format("%02d", s % 60));
					tv_timer.setText(timer);
				}
				catch(Exception ex)
				{
					Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
				}
            }

            public void onFinish() {
            	tv_timer.setVisibility(View.VISIBLE);
            	tv_timer.setText("00:00");
            	lv_bac.setBackgroundColor(Color.parseColor("#EA9999"));
            	btn_reset_timer.setBackgroundResource(R.drawable.button_curve_green);
            }
         }.start();
	}
	
	
	
	private class ProcessRegister extends AsyncTask<String, String, JSONObject> {
        String email, roomno;
        @Override
        
        protected void onPreExecute() {
            super.onPreExecute();
            email = LoginActivity.EmailAddress;
            //email = "1345@gmail.com";
            roomno = et_room_number.getText().toString();;
        }

        @Override
        protected JSONObject doInBackground(String... args)
        {
        	JSONObject json = null;
        	try
        	{
		        UserFunctions userFunction = new UserFunctions();
		        if(!check)
		        {
		        	json = userFunction.registerTimer(uniqueID, email, roomno, timer);
		        	check = true;
		        }
		        else
		        	json = userFunction.updateTimer(uniqueID, email, roomno, timer);
        	}
        	catch(Exception ex)
        	{
        		Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        	}
	        return json;
	        
        }
        @Override
        protected void onPostExecute(JSONObject json) 
        {
        	
        }
     }

	private int calculateTime() 
	{
		double diffrence = 0;
		int ret = 0;
		try 
		{
			String rateString = et_rate.getText().toString();
			String vtbiString = et_vtbi.getText().toString();
			if(rateString != null && vtbiString != null)
			{
				double rateInt = Double.parseDouble(rateString);
				double vtbiInt = Double.parseDouble(vtbiString);
				double decimalTimeFLoat = vtbiInt/rateInt;
				int decimalTimeint = (int)vtbiInt/(int)rateInt;
				diffrence = decimalTimeFLoat - decimalTimeint;
				diffrence = diffrence * 6 *10;
				ret = 60 * decimalTimeint + (int)diffrence;
			}
		}
		catch(Exception ex)
		{
			Toast.makeText(getApplicationContext(),	ex.getMessage(),Toast.LENGTH_SHORT).show();
		}
		return ret;
	}
		
		@Override
	    protected void onResume() {
	        super.onResume();
	    }

		
	    @Override
	    protected void onPause() {
	        super.onPause();
	    }

	    @Override
	    protected void onStop() {
	        super.onStop();
	    }

	    @Override
	    protected void onDestroy() {
	        super.onDestroy();
	       //countDownTimer.cancel();
	    }
}