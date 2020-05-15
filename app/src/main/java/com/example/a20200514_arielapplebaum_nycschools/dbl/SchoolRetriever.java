package com.example.a20200514_arielapplebaum_nycschools.dbl;

import android.content.Context;
import android.os.AsyncTask;

import com.example.a20200514_arielapplebaum_nycschools.bll.SchoolDataImplementer;
import com.example.a20200514_arielapplebaum_nycschools.bll.School;

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

public class SchoolRetriever extends AsyncTask<String, Void, ArrayList<School>> {

    private WeakReference<Context> contextRef;
    private String error;


    public SchoolRetriever(Context context) {
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

                // Iterate through the array to get each JSON Object
                for (int i = 0; i < jsonBuilder.length(); i++) {

                    // get current JSON Object
                    JSONObject jsonObject = jsonBuilder.getJSONObject(i);

                    // Construct new School from current JSON Object
                    School currentSchool = new School(jsonObject.getString(School.Fields.DBN.getValue()));

                    // Add Data to current School
                    currentSchool.setName(jsonObject.getString(School.Fields.NAME.getValue()));
                    if (jsonObject.has(School.Fields.PHONE.getValue())) {
                        if (!jsonObject.getString(School.Fields.PHONE.getValue()).equals("")) {
                            currentSchool.setPhone(jsonObject.getString(School.Fields.PHONE.getValue()));
                        }
                    }
                    if (jsonObject.has(School.Fields.EMAIL.getValue())) {
                        if (!jsonObject.getString(School.Fields.EMAIL.getValue()).equals("")) {
                            currentSchool.setEmail(jsonObject.getString(School.Fields.EMAIL.getValue()));
                        }
                    }
                    // Add current School to Array of Schools
                    schools.add(currentSchool);
                }
                return schools;
            } finally {
                connection.disconnect();
            }
        } catch (IOException e) {
            error = e.toString();
            return null;
        } catch (JSONException e) {
            error = "JSON TYPE: " + e.toString();
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<School> schools) {
        Context context = this.contextRef.get();
        if (schools.size() != 0) {
            if (context != null) {
                ((SchoolDataImplementer) context).AttachData(schools);
            }
        } else {
            if (context != null) {
                ((SchoolDataImplementer) context).HandleError(error);
            }
        }
        super.onPostExecute(schools);
    }

}
