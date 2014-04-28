package com.flytesolutions.medicalivapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.flytesolutions.library.DatabaseHandler;
import com.flytesolutions.library.UserFunctions;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends Activity {

	Button btnLogin;
	TextView Btnregister;
	// Button passreset;
	EditText inputEmail;
	EditText inputPassword;
	
	public static String EmailAddress = ""; 
	private static String KEY_SUCCESS = "success";
	private static String KEY_UID = "uid";
	private static String KEY_USERNAME = "uname";
	private static String KEY_FIRSTNAME = "fname";
	private static String KEY_LASTNAME = "lname";
	private static String KEY_EMAIL = "email";
	private static String KEY_CREATED_AT = "created_at";

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		inputEmail = (EditText) findViewById(R.id.et_email);
		inputPassword = (EditText) findViewById(R.id.et_password);

		Btnregister = (TextView) findViewById(R.id.tv_create_new_account);

		btnLogin = (Button) findViewById(R.id.btn_login);

		// passreset = (Button)findViewById(R.id.passres);
		
		// passreset.setOnClickListener(new View.OnClickListener() {
		// public void onClick(View view) {
		// Intent myIntent = new Intent(view.getContext(), PasswordReset.class);
		// startActivityForResult(myIntent, 0);
		// finish();
		// }});
		
		
		Btnregister.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent myIntent = new Intent(view.getContext(), RegisterActivity.class);
				startActivityForResult(myIntent, 0);
				finish();
			}
		});

		/**
		 * Login button click event A Toast is set to alert when the Email and
		 * Password field is empty
		 **/
		btnLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

				if ((!inputEmail.getText().toString().equals("")) && (!inputPassword.getText().toString().equals(""))) 
				{
					NetAsync(view);
				}
				else if ((!inputEmail.getText().toString().equals(""))) {
					Toast.makeText(getApplicationContext(),"Password field empty", Toast.LENGTH_SHORT).show();
				} else if ((!inputPassword.getText().toString().equals(""))) {
					Toast.makeText(getApplicationContext(),"Email field empty", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(),"Email and Password field are empty",Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private class NetCheck extends AsyncTask<String, String, Boolean> {
		private ProgressDialog nDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			nDialog = new ProgressDialog(LoginActivity.this);
			nDialog.setTitle("Checking Internet Connection");
			nDialog.setMessage("Loading...Please Wait...");
			nDialog.setIndeterminate(false);
			nDialog.setCancelable(true);
			nDialog.show();
		}

		@Override
		protected Boolean doInBackground(String... args) {

			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getActiveNetworkInfo();
			if (netInfo != null && netInfo.isConnected()) {
				try
				{
					URL url = new URL("http://www.google.com");
					HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
					urlc.setConnectTimeout(3000);
					urlc.connect();
					if (urlc.getResponseCode() == 200) {
						return true;
					}
				} catch (MalformedURLException e1) 
				{
					Toast.makeText(getApplicationContext(),e1.getMessage(), Toast.LENGTH_SHORT).show();
					e1.printStackTrace();
				} 
				catch (IOException e) 
				{
					Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			}
			return false;

		}

		@Override
		protected void onPostExecute(Boolean th) {

			if (th == true) {
				nDialog.dismiss();
				new ProcessLogin().execute();
			} else {
				nDialog.dismiss();
			
			}
		}
	}

	/**
	 * Async Task to get and send data to My Sql database through JSON respone.
	 **/
	private class ProcessLogin extends AsyncTask<String, String, JSONObject> {

		private ProgressDialog pDialog;

		String email, password;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			inputEmail = (EditText) findViewById(R.id.et_email);
			inputPassword = (EditText) findViewById(R.id.et_password);
			email = inputEmail.getText().toString();
			EmailAddress = inputEmail.getText().toString();
			password = inputPassword.getText().toString();
			pDialog = new ProgressDialog(LoginActivity.this);
			pDialog.setTitle("Contacting To Servers");
			pDialog.setMessage("Logging in...Please Wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... args) {

			UserFunctions userFunction = new UserFunctions();
			JSONObject json = userFunction.loginUser(email, password);
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			try {
				if (json.getString(KEY_SUCCESS) != null) {

					String res = json.getString(KEY_SUCCESS);

					if (Integer.parseInt(res) == 1) {
						pDialog.setMessage("Loading User Space");
						pDialog.setTitle("Getting Data");
						DatabaseHandler db = new DatabaseHandler(getApplicationContext());
						JSONObject json_user = json.getJSONObject("user");
						/**
						 * Clear all previous data in SQlite database.
						 **/
						UserFunctions logout = new UserFunctions();
						logout.logoutUser(getApplicationContext());
						db.addUser(json_user.getString(KEY_FIRSTNAME),
								json_user.getString(KEY_LASTNAME),
								json_user.getString(KEY_EMAIL),
								json_user.getString(KEY_USERNAME),
								json_user.getString(KEY_UID),
								json_user.getString(KEY_CREATED_AT));
						/**
						 * If JSON array details are stored in SQlite it
						 * launches the User Panel.
						 **/
						Intent upanel = new Intent(getApplicationContext(),HelpActivity.class);
						upanel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						pDialog.dismiss();
						startActivity(upanel);
						/**
						 * Close Login Screen
						 **/
						finish();
					} else {

						pDialog.dismiss();
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public void NetAsync(View view) {
		new NetCheck().execute();
	}
}