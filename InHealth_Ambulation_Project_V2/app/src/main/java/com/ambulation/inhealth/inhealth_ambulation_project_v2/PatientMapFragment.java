package com.ambulation.inhealth.inhealth_ambulation_project_v2;

import android.app.ActionBar;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Layout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatientMapFragment extends Fragment implements View.OnClickListener{
    /**
     * declare patient room list for Status Map
     */
    private static ArrayList<Button> room_list;
    private static HashMap<Integer, Patient> patient_list;
    private static ArrayList<HashMap<String, Integer>> coor_list = new ArrayList<>();
    private SharedViewModel model;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
     View view = inflater.inflate(R.layout.patient_map_fragment, container, false);
     this.room_list = new ArrayList<>();
     this.patient_list = new HashMap<>();

     // load JSON coordinates
     try {
         JSONObject obj = new JSONObject(loadJSONFromAsset());
         JSONArray pos_arr = obj.getJSONArray("coordinates");
         for(int i = 0; i < pos_arr.length(); i++) {
             JSONObject c_object = pos_arr.getJSONObject(i);
             HashMap<String, Integer> hm = new HashMap<>();
             hm.put("x", c_object.getInt("x"));
             hm.put("y", c_object.getInt("y"));
             hm.put("width", c_object.getInt("width"));
             hm.put("height", c_object.getInt("height"));
             coor_list.add(hm);
         }
     } catch (JSONException e) {
         e.printStackTrace();
     }

     this.model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
     initRoom(view);
     model.getPatientList().observe(this, plist -> {
           for(Patient p: plist) {
               patient_list.put(p.getRoom(), p);
           }
           colorRoom();
        });

     return view;
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("Z10W.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public void initRoom(View view) {

        int[] Id_list = {R.id.button1, R.id.button2, R.id.button3,
        R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9, R.id.button10,
                R.id.button11, R.id.button12, R.id.button14, R.id.button15, R.id.button16, R.id.button17, R.id.button18,
                R.id.button19, R.id.button20, R.id.button21, R.id.button22, R.id.button23, R.id.button24, R.id.button25,
                R.id.button26, R.id.button27, R.id.button28, R.id.button29, R.id.button30, R.id.button31, R.id.button32, R.id.button33};

        for(int i = 0; i < Id_list.length; i++) {
            Button button = view.findViewById(Id_list[i]);
            FrameLayout.LayoutParams btn_layout = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT
            );
            Resources r = this.getContext().getResources();
            HashMap<String, Integer> hm = coor_list.get(i);
            int lm = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, hm.get("x"), r.getDisplayMetrics());
            btn_layout.leftMargin = lm;
            int tm = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, hm.get("y"), r.getDisplayMetrics());
            btn_layout.topMargin = tm;
            int w = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, hm.get("width"), r.getDisplayMetrics());
            btn_layout.width = w;
            int h = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, hm.get("height"), r.getDisplayMetrics());
            btn_layout.height = h;
            button.setLayoutParams(btn_layout);
            room_list.add(button);
        }

        setTag();
    }

    protected void setTag() {
        for(int i = 0; i < room_list.size(); i++) {
            int room = i;
            room ++;
            if(i > 11) {
                room++;
            }
            room_list.get(i).setTag(room);
            room_list.get(i).setOnClickListener(this);
        }
    }

    public void colorRoom() {
        for(Map.Entry<Integer, Patient> entry: patient_list.entrySet()) {
            Patient patient = entry.getValue();
            int today_amb = patient.getToday_ambulation();
            int room = entry.getKey();
            // find corresponding index in room_list for current room
            room --;
            if(room > 12) {
                room --;
            }

            if(today_amb == 1) {
                (room_list.get(room)).setBackgroundColor(getResources().getColor(R.color.orange));
            } else if (today_amb == 2) {
                (room_list.get(room)).setBackgroundColor(getResources().getColor(R.color.yellow));
            } else if(today_amb >= 3) {
                (room_list.get(room)).setBackgroundColor(getResources().getColor(R.color.green));
            }
        }


    }



    @Override
    public void onClick(View v) {
      int room = (Integer)v.getTag();
      if(patient_list.containsKey(room)) {
          this.model.setPatientInfo(patient_list.get(room));
          this.model.setCurrentState("Patient");
      } else {
          AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
          builder.setMessage("Room #"+room+ " is empty!");
          AlertDialog dialog = builder.create();
          dialog.show();

      }
    }

}
