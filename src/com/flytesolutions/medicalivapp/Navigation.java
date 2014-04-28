package com.flytesolutions.medicalivapp;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Navigation extends Activity
{
    Button btn_patient_room;
    Button btn_nurse_room;

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        btn_patient_room = (Button) findViewById(R.id.btn_patient_room);
        btn_nurse_room = (Button) findViewById(R.id.btn_nurse_room);

        btn_patient_room.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0)
            {
                Intent login = new Intent(getApplicationContext(), ViewActivity.class);
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(login);
                finish();
                
            }
        });
        
        btn_nurse_room.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0)
            {
                Intent login = new Intent(getApplicationContext(), NursingActivity.class);
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(login);
                finish();
            }
        });

    }}