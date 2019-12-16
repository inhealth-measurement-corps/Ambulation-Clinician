package com.ambulation.inhealth.inhealth_ambulation_project_v2;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class PatientListHelper extends AsyncTask {
    private Context context;
    private AsyncResponse delegate;

    public PatientListHelper(Context context, AsyncResponse delegate) {
        this.context = context;
        this.delegate = delegate;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            String link = "http://10.0.2.2:5000/clinician/unit/details";
            URL url = new URL(link);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-length", "0");
            urlConnection.setUseCaches(false);
            urlConnection.setAllowUserInteraction(false);
            urlConnection.setConnectTimeout(100000);
            urlConnection.setReadTimeout(100000);

            urlConnection.connect();

            int responseCode = urlConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                Log.d("All patients values: ", stringBuilder.toString());

                JSONArray jsonArray = new JSONArray(stringBuilder.toString());
                List<Patient> patient_list = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) (jsonArray.get(i));
                    Patient patient = new Patient();
                    patient.setId(Integer.parseInt(""+jsonObject.get("id")));

                    patient.setRoom(Integer.parseInt(""+jsonObject.get("room")));

                    patient.setTotal_distance((int) (Double.parseDouble(""+jsonObject.get("total_dist"))));
                    patient.setTotal_duration(Integer.parseInt(""+jsonObject.get("total_dur")));
                    patient.setTotal_ambulation(Integer.parseInt(""+jsonObject.get("total_amb")));

                    double total_speed = 0.0;
                    if (patient.getTotal_duration() > 0) {
                        total_speed = patient.getTotal_distance() / patient.getTotal_duration();
                    }
                    patient.setTotal_speed(total_speed * 0.0113636 * 60);
                    patient.setPcu_los(Integer.parseInt(""+jsonObject.get("pcu_los")));
                    patient.setTotal_los(Integer.parseInt(""+jsonObject.get("total_los")));

                    patient.setDaily_ambulation((int) (Double.parseDouble(""+jsonObject.get("avg_amb"))));

                    patient.setToday_distance((int) Double.parseDouble(""+jsonObject.get("today_dist")));
                    patient.setToday_duration(Integer.parseInt(""+jsonObject.get("today_dur")));
                    patient.setToday_ambulation(Integer.parseInt(""+jsonObject.get("today_amb")));

                    double today_speed = 0;
                    if (patient.getToday_duration() > 0) {
                        today_speed = patient.getToday_distance() / patient.getToday_duration();
                    }

                    patient.setToday_speed(today_speed * 0.0113636 * 60);

                    patient.setYes_distance((int) Double.parseDouble("" + jsonObject.get("yest_dist")));
                    patient.setYes_duration(Integer.parseInt("" + jsonObject.get("yest_dur")));
                    patient.setYes_ambulation(Integer.parseInt(""+jsonObject.get("yest_amb")));

                    double yes_speed = 0;
                    if (patient.getYes_duration() > 0) {
                        yes_speed = patient.getYes_distance() / patient.getYes_duration();
                    }

                    patient.setYes_speed(yes_speed * 0.0113636 * 60);
                    // For the purpose of testing without changing the data, assume today is the max date
                    // of the patient in the database.
                    patient_list.add(patient);
                }

                return patient_list;
            }

        } catch (MalformedURLException ex) {
            Log.e("PatientListHelper", Log.getStackTraceString(ex));
        } catch (IOException ex) {
            Log.e("PatientListHelper", Log.getStackTraceString(ex));
        } catch (JSONException ex) {
            Log.e("PatientListHelper", Log.getStackTraceString(ex));
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        this.delegate.processFinish(o);
    }

}
