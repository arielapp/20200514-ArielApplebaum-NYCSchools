package com.example.a20200514_arielapplebaum_nycschools.dbl;

import android.content.Context;
import android.os.AsyncTask;

import com.example.a20200514_arielapplebaum_nycschools.bll.School;
import com.example.a20200514_arielapplebaum_nycschools.bll.SchoolDataImplementer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;

public class SATRetriever extends AsyncTask<String, Void, ArrayList<School>> {

    private WeakReference<Context> contextRef;
    private String error = "";

    public SATRetriever(Context context) {
        this.contextRef = new WeakReference<>(context);
    }


    @Override
    protected ArrayList<School> doInBackground(String... urls) {

        try {
            ArrayList<School> schools = new ArrayList<>();
            URL url = new URL(urls[0]);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line).append("\n");
                }
                reader.close();
                // make JSON array from Builder
                JSONArray jsonBuilder = new JSONArray(builder.toString());

                // get current JSON Object
                JSONObject jsonObject = jsonBuilder.getJSONObject(0);

                // Construct new School from current JSON Object
                School currentSchool = new School(jsonObject.getString(School.Fields.DBN.getValue()));

                // Add Data to current School
                currentSchool.setName(jsonObject.getString(School.Fields.NAME.getValue()));
                if (jsonObject.has(School.Fields.CRITICALREADING.getValue())) {
                    if (!jsonObject.getString(School.Fields.CRITICALREADING.getValue()).equals("")) {
                        currentSchool.setSATCriticalReading(jsonObject.getString(School.Fields.CRITICALREADING.getValue()));
                    }
                }
                if (jsonObject.has(School.Fields.MATH.getValue())) {
                    if (!jsonObject.getString(School.Fields.MATH.getValue()).equals("")) {
                        currentSchool.setSATMath(jsonObject.getString(School.Fields.MATH.getValue()));
                    }
                }
                if (jsonObject.has(School.Fields.WRITING.getValue())) {
                    if (!jsonObject.getString(School.Fields.WRITING.getValue()).equals("")) {
                        currentSchool.setSATWriting(jsonObject.getString(School.Fields.WRITING.getValue()));
                    }

                }

                // Add current School to Array of Schools
                schools.add(currentSchool);

                return schools;
            } finally {
                connection.disconnect();
            }
        } catch (IOException | JSONException e) {
            error = e.toString();
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<School> schools) {
        Context context = this.contextRef.get();
        if (schools != null) {
            if (schools.size() != 0) {
                if (context != null) {
                    ((SchoolDataImplementer) context).AttachData(schools);
                }
            } else {
                if (context != null) {
                    ((SchoolDataImplementer) context).HandleError(error);
                }
            }
        }
        super.onPostExecute(schools);
    }
}
