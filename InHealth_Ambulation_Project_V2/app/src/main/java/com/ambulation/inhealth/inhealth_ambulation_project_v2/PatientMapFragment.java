package com.ambulation.inhealth.inhealth_ambulation_project_v2;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
    /** buttons that each represent a room *****************/
    private static Button button1;
    private static Button button2;
    private static Button button3;
    private static Button button4;
    private static Button button5;
    private static Button button6;
    private static Button button7;
    private static Button button8;
    private static Button button9;
    private static Button button10;
    private static Button button11;
    private static Button button12;
    private static Button button14;
    private static Button button15;
    private static Button button16;
    private static Button button17;
    private static Button button18;
    private static Button button19;
    private static Button button20;
    private static Button button21;
    private static Button button22;
    private static Button button23;
    private static Button button24;
    private static Button button25;
    private static Button button26;
    private static Button button27;
    private static Button button28;
    private static Button button29;
    private static Button button30;
    private static Button button31;
    private static Button button32;
    private static Button button33;
    /*********************************************************/

    private SharedViewModel model;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
     View view = inflater.inflate(R.layout.patient_map_fragment, container, false);
     this.room_list = new ArrayList<>();
     this.patient_list = new HashMap<>();
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


    public void initRoom(View view) {
        button1 = view.findViewById(R.id.button1);
        button2 = view.findViewById(R.id.button2);
        button3 = view.findViewById(R.id.button3);
        button4 =  view.findViewById(R.id.button4);
        button5 =  view.findViewById(R.id.button5);
        button6 =  view.findViewById(R.id.button6);
        button7 =  view.findViewById(R.id.button7);
        button8 =  view.findViewById(R.id.button8);
        button9 =  view.findViewById(R.id.button9);
        button10 =  view.findViewById(R.id.button10);
        button11 =  view.findViewById(R.id.button11);
        button12 =  view.findViewById(R.id.button12);
        button14 =  view.findViewById(R.id.button14);
        button15 =  view.findViewById(R.id.button15);
        button16 =  view.findViewById(R.id.button16);
        button17 =  view.findViewById(R.id.button17);
        button18 =  view.findViewById(R.id.button18);
        button19 =  view.findViewById(R.id.button19);
        button20 =  view.findViewById(R.id.button20);
        button21 =  view.findViewById(R.id.button21);
        button22 =  view.findViewById(R.id.button22);
        button23 =  view.findViewById(R.id.button23);
        button24 =  view.findViewById(R.id.button24);
        button25 =  view.findViewById(R.id.button25);
        button26 =  view.findViewById(R.id.button26);
        button27 =  view.findViewById(R.id.button27);
        button28 =  view.findViewById(R.id.button28);
        button29 =  view.findViewById(R.id.button29);
        button30 =  view.findViewById(R.id.button30);
        button31 = view.findViewById(R.id.button31);
        button32 = view.findViewById(R.id.button32);
        button33 = view.findViewById(R.id.button33);


        room_list.add(button1);
        room_list.add(button2);
        room_list.add(button3);
        room_list.add(button4);
        room_list.add(button5);
        room_list.add(button6);
        room_list.add(button7);
        room_list.add(button8);
        room_list.add(button9);
        room_list.add(button10);
        room_list.add(button11);
        room_list.add(button12);
        room_list.add(button14);
        room_list.add(button15);
        room_list.add(button16);
        room_list.add(button17);
        room_list.add(button18);
        room_list.add(button19);
        room_list.add(button20);
        room_list.add(button21);
        room_list.add(button22);
        room_list.add(button23);
        room_list.add(button24);
        room_list.add(button25);
        room_list.add(button26);
        room_list.add(button27);
        room_list.add(button28);
        room_list.add(button29);
        room_list.add(button30);
        room_list.add(button31);
        room_list.add(button32);
        room_list.add(button33);

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
