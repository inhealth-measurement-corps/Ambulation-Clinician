package com.ambulation.inhealth.inhealth_ambulation_project_v2;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
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
            String date = (String) objects[0];
            String data = URLEncoder.encode("today", "UTF-8") + "=" +
                    URLEncoder.encode(date, "UTF-8");
            String link = "http://10.162.80.9:90/ambulation/test-clinician-all-patient.php";
            URL url = new URL(link);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
            writer.write(data);
            writer.flush();
            writer.close();
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
                // Each element will be a patient so we need to split the components out
                // Format: id<>room<>distance<>duration<>ambulation
                JSONObject jsonObject = (JSONObject) (jsonArray.get(i));
                Patient patient = new Patient();
                patient.setId(Integer.parseInt((String) (jsonObject.get("id"))));

                patient.setRoom(Integer.parseInt((String) (jsonObject.get("room"))));

                patient.setTotal_distance((int) (Double.parseDouble((String) (jsonObject.get("total_dist")))));
                patient.setTotal_duration(Integer.parseInt((String) (jsonObject.get("total_dur"))));
                patient.setTotal_ambulation(Integer.parseInt((String) (jsonObject.get("total_amb"))));

                double total_speed = 0.0;
                if (patient.getTotal_duration() > 0) {
                    total_speed = patient.getTotal_distance() / patient.getTotal_duration();
                }
                patient.setTotal_speed(total_speed * 0.0113636 * 60);
                patient.setPcu_los(Integer.parseInt((String) (jsonObject.get("pcu_los"))));
                patient.setTotal_los(Integer.parseInt((String) (jsonObject.get("total_los"))));

                patient.setDaily_ambulation((int)(Double.parseDouble((String) (jsonObject.get("avg_amb")))));

                patient.setToday_distance((int) Double.parseDouble((String) (jsonObject.get("today_dist"))));
                patient.setToday_duration(Integer.parseInt((String) (jsonObject.get("today_dur"))));
                patient.setToday_ambulation(Integer.parseInt((String) (jsonObject.get("today_amb"))));

                double today_speed = 0;
                if (patient.getToday_duration() > 0) {
                    today_speed = patient.getToday_distance() / patient.getToday_duration();
                }

                patient.setToday_speed(today_speed * 0.0113636 * 60);

                patient.setYes_distance((int) Double.parseDouble((String) (jsonObject.get("yest_dist"))));
                patient.setYes_duration(Integer.parseInt((String) (jsonObject.get("yest_dur"))));
                patient.setYes_ambulation(Integer.parseInt((String) (jsonObject.get("yest_amb"))));

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
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }

    }

    @Override
    protected void onPostExecute(Object o) {
        this.delegate.processFinish(o);
    }

}
