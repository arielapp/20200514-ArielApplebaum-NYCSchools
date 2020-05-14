package com.example.a20200514_arielapplebaum_nycschools.pl;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a20200514_arielapplebaum_nycschools.R;
import com.example.a20200514_arielapplebaum_nycschools.bll.School;
import com.example.a20200514_arielapplebaum_nycschools.bll.SchoolDataImplementer;
import com.example.a20200514_arielapplebaum_nycschools.dbl.SATRetriever;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class SchoolDetail extends AppCompatActivity implements SchoolDataImplementer {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_detail);

        // get Intent from List
        Intent intent = getIntent();

        if (intent != null) {

            // get data from Intent
            String dbn = intent.getStringExtra(School.Fields.DBN.getValue());
            String name = intent.getStringExtra(School.Fields.NAME.getValue());
            final String phone = intent.getStringExtra(School.Fields.PHONE.getValue());
            final String email = intent.getStringExtra(School.Fields.EMAIL.getValue());

            // Initialize text Views
            TextView schoolTitle = findViewById(R.id.detailSchoolName);
            TextView schoolPhone = findViewById(R.id.detailSchoolPhone);
            TextView schoolEmail = findViewById(R.id.detailSchoolEmail);

            // Initialize Action Buttons
            FloatingActionButton phoneButton = findViewById(R.id.detailPhoneActionButton);
            phoneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Make phone call
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + phone));
                    startActivity(callIntent);
                }
            });
            FloatingActionButton emailButton = findViewById(R.id.detailEmailActionButton);
            emailButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setDataAndType(Uri.parse("mailto:" + email), "text/plain");

                    try {
                        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                        finish();
                        Log.i("Finished sending email...", "");
                    } catch (android.content.ActivityNotFoundException ex) {
                        HandleError("There is no email client installed.");
                    }
                }
            });

            // add data to views
            schoolTitle.setText(name);
            if (phone != null) {
                schoolPhone.setText(phone);
            } else {
                phoneButton.setVisibility(View.INVISIBLE);
            }
            if (email != null) {
                schoolEmail.setText(email);
            } else {
                emailButton.setVisibility(View.INVISIBLE);
            }

            // begin AsyncTask to retrieve data from the API
            SATRetriever satRetriever = new SATRetriever(SchoolDetail.this);
            String satAPI = "https://data.cityofnewyork.us/resource/f9bf-2cp4.json";
            satRetriever.execute(satAPI + "?dbn=" + dbn);
        }
    }

    @Override
    public void AttachData(ArrayList<School> schools) {

        // Get School from Array
        School school = schools.get(0);

        // Initialize the List View
        ListView SATListView = findViewById(R.id.satListView);

        // create the list of schools
        List<HashMap<String, String>> schoolList = new ArrayList<>();

        // Iterate through the School Fields
        for (School.Fields field : School.Fields.values()) {

            // Create new HashMap to hold Titles and Values
            HashMap<String, String> schoolMap = new HashMap<>();

            // Add SAT titles and scores to List
            switch (field) {
                case CRITICALREADING:
                    schoolMap.put(School.Fields.DBN.getValue(), getResources().getString(R.string.SATCriticalReading));
                    schoolMap.put(School.Fields.NAME.getValue(), school.getSATCriticalReading());
                    schoolList.add(schoolMap);
                    break;
                case MATH:
                    schoolMap.put(School.Fields.DBN.getValue(), getResources().getString(R.string.SATMath));
                    schoolMap.put(School.Fields.NAME.getValue(), school.getSATMath());
                    schoolList.add(schoolMap);
                    break;
                case WRITING:
                    schoolMap.put(School.Fields.DBN.getValue(), getResources().getString(R.string.SATWriting));
                    schoolMap.put(School.Fields.NAME.getValue(), school.getSATWriting());
                    schoolList.add(schoolMap);
                    break;
            }

        }

        String[] from = {School.Fields.DBN.getValue(), School.Fields.NAME.getValue()};
        int[] to = {R.id.item_name, R.id.item_description};
        if (SATListView != null) {
            SimpleAdapter adapter = new SimpleAdapter(this, schoolList, R.layout.list_item, from, to);
            SATListView.setAdapter(adapter);
        }
    }

    @Override
    public void HandleError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        Log.e("Error", error);
    }
}