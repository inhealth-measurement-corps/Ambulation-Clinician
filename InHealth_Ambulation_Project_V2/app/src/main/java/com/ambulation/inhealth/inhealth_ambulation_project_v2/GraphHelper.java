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
            if (this.type.equals("Today")) {
                Log.d("GraphHelper", "Today");
                link = "http://10.162.80.9:90/ambulation/test-clinician-today.php";
            } else if (this.type.equals("Month")){
                Log.d("GraphHelper", "Month");
                link = "http://10.162.80.9:90/ambulation/test-clinician-month.php";
            } else {
                Log.d("GraphHelper", "Week");
                link = "http://10.162.80.9:90/ambulation/test-clinician-week.php";
            }
            // Each objects have two fields, patient_id and date
            String patient_room = (String)(objects[0]);
            Log.d("patient room",patient_room);
            String date = (String) objects[1];
            Log.d("date", date);
            String mode = (String) objects[2];
            Log.d("mode", mode);
            String data = URLEncoder.encode("patient_room", "UTF-8") + "=" +
                    URLEncoder.encode(patient_room, "UTF-8");
            data += "&" + URLEncoder.encode("date", "UTF-8") + "=" +
                    URLEncoder.encode(date, "UTF-8");
            data += "&" + URLEncoder.encode("mode", "UTF-8") + "=" +
                    URLEncoder.encode(mode, "UTF-8");
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

            List<GraphEntry> list = new ArrayList<>();
            if(stringBuilder.toString() != null && !stringBuilder.toString().equals("") && !stringBuilder.toString().equals("null")) {
                Log.d("Patient Room", patient_room);
                Log.d("Details Values", this.type + stringBuilder.toString());
                JSONArray jsonArray = new JSONArray(stringBuilder.toString());

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) (jsonArray.get(i));
                    GraphEntry entry = new GraphEntry(Float.parseFloat((String) (jsonObject.get("date"))), (int) (Float.parseFloat((String) (jsonObject.get("distance")))), Float.parseFloat((String) (jsonObject.get("speed"))), (int) (Float.parseFloat((String) (jsonObject.get("duration")))), (int)(Float.parseFloat((String) (jsonObject.get("num_amb")))));
                    list.add(entry);
                }

            }
            HashMap<String, List<GraphEntry>> map = new HashMap<>();
            Log.d("type", this.type);
            System.out.println(list);
            map.put(this.type, list);

            return map;
        } catch (Exception e) {
            System.out.print(e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        this.delegate.processFinish(o);
    }
}
