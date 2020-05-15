package com.example.a20200514_arielapplebaum_nycschools.pl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.a20200514_arielapplebaum_nycschools.R;
import com.example.a20200514_arielapplebaum_nycschools.bll.SchoolDataImplementer;
import com.example.a20200514_arielapplebaum_nycschools.bll.School;
import com.example.a20200514_arielapplebaum_nycschools.dbl.SchoolRetriever;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SchoolDataImplementer {

    private ListView NYCListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NYCListView = findViewById(R.id.NYCListView);
        SchoolRetriever schoolRetriever = new SchoolRetriever(MainActivity.this);
        String schoolsAPI = "https://data.cityofnewyork.us/resource/s3k6-pzi2.json";
        schoolRetriever.execute(schoolsAPI);
    }

    @Override
    public void AttachData(ArrayList<School> schools) {
        // create the list of schools
        List<HashMap<String, String>> schoolList = new ArrayList<>();
        for (School school : schools) {
            HashMap<String, String> schoolMap = new HashMap<>();

            // School information
            schoolMap.put(School.Fields.DBN.getValue(), school.getDbn());
            schoolMap.put(School.Fields.NAME.getValue(), school.getName());
            if (school.getPhone() != null) {
                schoolMap.put(School.Fields.PHONE.getValue(), school.getPhone());
            }else{
                schoolMap.put(School.Fields.PHONE.getValue(),null);
            }
            if (school.getEmail() != null) {
                schoolMap.put(School.Fields.EMAIL.getValue(), school.getEmail());
            }else{
                schoolMap.put(School.Fields.EMAIL.getValue(), null);
            }
            schoolList.add(schoolMap);
        }

        String[] from = {School.Fields.DBN.getValue(), School.Fields.NAME.getValue(), School.Fields.PHONE.getValue(), School.Fields.EMAIL.getValue()};
        int[] to = {0, R.id.item_name, R.id.item_description, 0};
        if (NYCListView != null) {
            SimpleAdapter adapter = new SimpleAdapter(this, schoolList, R.layout.list_item, from, to);
            NYCListView.setAdapter(adapter);
            NYCListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    // Retrieve data from the list
                    HashMap<String, String> schoolMapResult = (HashMap<String, String>) NYCListView.getItemAtPosition(position);

                    // Send user to the details screen based on the item they pressed
                    Intent intent = new Intent(MainActivity.this, SchoolDetail.class);

                    // Send the details to populate the details screen
                    intent.putExtra(School.Fields.DBN.getValue(), schoolMapResult.get(School.Fields.DBN.getValue()));
                    intent.putExtra(School.Fields.NAME.getValue(), schoolMapResult.get(School.Fields.NAME.getValue()));
                    intent.putExtra(School.Fields.PHONE.getValue(), schoolMapResult.get(School.Fields.PHONE.getValue()));
                    intent.putExtra(School.Fields.EMAIL.getValue(), schoolMapResult.get(School.Fields.EMAIL.getValue()));

                    // Start Detail Activity with intent
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void HandleError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        Log.e("Error",error);
    }
}
