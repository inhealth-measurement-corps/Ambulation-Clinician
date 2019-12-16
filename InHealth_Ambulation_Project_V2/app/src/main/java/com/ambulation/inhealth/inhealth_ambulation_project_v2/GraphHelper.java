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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GraphHelper extends AsyncTask {
    private Context context;
    private AsyncResponse delegate;
    private String type;

    public GraphHelper(Context context, AsyncResponse delegate, String type) {
        this.context = context;
        this.delegate = delegate;
        this.type = type;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            String link = "";
            // Each objects have two fields, patient_id and date
            String patient_room = (String) (objects[0]);
            Log.d("patient room", patient_room);
            String date = (String) objects[1];
            Log.d("date", date);
            String mode = (String) objects[2];
            Log.d("mode", mode);
            if (mode == "Patient") {
                if (this.type.equals("Today")) {
                    Log.d("GraphHelper", "Today");
                    link = "http://10.0.2.2:5000/clinician/patient/today/" + patient_room;
                } else if (this.type.equals("Month")) {
                    Log.d("GraphHelper", "Month");
                    link = "http://10.0.2.2:5000/clinician/patient/month/" + patient_room;
                } else {
                    Log.d("GraphHelper", "Week");
                    link = "http://10.0.2.2:5000/clinician/patient/week/" + patient_room;
                }

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

                    List<GraphEntry> list = new ArrayList<>();
                    if (stringBuilder.toString() != null && !stringBuilder.toString().equals("") && !stringBuilder.toString().equals("null")) {
                        Log.d("Patient Room", patient_room);
                        Log.d("Details Values", this.type + stringBuilder.toString());
                        JSONArray jsonArray = new JSONArray(stringBuilder.toString());

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) (jsonArray.get(i));
                            GraphEntry entry = new GraphEntry(Float.parseFloat("" + jsonObject.get("date")), (int) (Float.parseFloat("" + jsonObject.get("distance"))), Float.parseFloat("" + jsonObject.get("speed")), (int) (Float.parseFloat("" + jsonObject.get("duration"))), (int) (Float.parseFloat("" + jsonObject.get("num_amb"))));
                            list.add(entry);
                        }

                    }
                    HashMap<String, List<GraphEntry>> map = new HashMap<>();
                    Log.d("type", this.type);
                    System.out.println(list);
                    map.put(this.type, list);

                    return map;

                }

            } else {
                if (this.type.equals("Month")) {
                    Log.d("GraphHelper", "Month");
                    link = "http://10.0.2.2:5000/clinician/unit/month";
                } else {
                    Log.d("GraphHelper", "Week");
                    link = "http://10.0.2.2:5000/clinician/unit/week";
                }

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

                    List<GraphEntry> list = new ArrayList<>();
                    if (stringBuilder.toString() != null && !stringBuilder.toString().equals("") && !stringBuilder.toString().equals("null")) {
                        JSONArray jsonArray = new JSONArray(stringBuilder.toString());

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) (jsonArray.get(i));
                            GraphEntry entry = new GraphEntry(Float.parseFloat("" + jsonObject.get("date")), (int) (Float.parseFloat("" + jsonObject.get("distance"))), Float.parseFloat("" + jsonObject.get("speed")), (int) (Float.parseFloat("" + jsonObject.get("duration"))), (int) (Float.parseFloat("" + jsonObject.get("num_amb"))));
                            list.add(entry);
                        }
                    }
                    HashMap<String, List<GraphEntry>> map = new HashMap<>();
                    map.put(this.type, list);

                    return map;

                }
            }
        }
        catch (MalformedURLException ex) {
            Log.e("GraphHelper", Log.getStackTraceString(ex));
        }
        catch (IOException ex) {
            Log.e("GraphHelper", Log.getStackTraceString(ex));
        }
        catch (JSONException ex) {
            Log.e("GraphHelper", Log.getStackTraceString(ex));
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        this.delegate.processFinish(o);
    }
}
