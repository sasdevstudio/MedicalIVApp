package com.flytesolutions.medicalivapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class HelpActivity  extends Activity {

	Button btnok;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_help);
		btnok = (Button) findViewById(R.id.btn_ok);

		btnok.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view)
			{
				Intent login = new Intent(getApplicationContext(), Navigation.class);
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(login);
                finish();
			}
		});
	}
}