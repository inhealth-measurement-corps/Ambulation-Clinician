package com.ambulation.inhealth.inhealth_ambulation_project_v2;

import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PatientInfoFragment extends Fragment implements View.OnClickListener, AsyncResponse{
    private static HashMap<Integer, Patient> patient_list;

    TextView num_of_amb;
    TextView num_of_dis;
    TextView num_of_avg_spd;
    TextView num_of_dur;
    TextView room_num;
    private SharedViewModel model;
    private int patient_room;
    private String date;

    Button today;
    Button week;
    Button month;

    private String mode ="Unit";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.patient_info_fragment, container, false);
        Date d = new Date();
        date = (String) DateFormat.format("yyyy-MM-dd", d.getTime());

        this.model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        this.patient_list = new HashMap<>();
        this.num_of_amb = view.findViewById(R.id.num_of_amb);
        this.num_of_dis = view.findViewById(R.id.num_of_dis);
        this.num_of_avg_spd = view.findViewById(R.id.num_of_avg_spd);
        this.num_of_dur = view.findViewById(R.id.num_of_dur);
        this.room_num = view.findViewById(R.id.room_num);

        model.setCurrentState("Unit");
        model.getCurrentState().observe(this, state -> {
            if(state.equals("Unit")) {
                model.getPatientList().observe(this, plist -> {
                    for (Patient p : plist) {
                        patient_list.put(p.getRoom(), p);
                    }
                    displayUnitAverage();
                    mode = "Unit";
                    setButtons(view);
                });
            } else {
                model.getPatientInfo().observe(this, patient -> {
                    this.patient_room = patient.getRoom();
                    room_num.setText("Room #"+this.patient_room);
                    mode = "Patient";
                    new GraphHelper(getContext(), this, "Today").execute(Integer.toString(patient_room), date, "Patient");
                    setButtons(view);
                });

            }

        });


        return view;
    }


    private void displayUnitAverage() {

        int daily_amb = 0;
        int daily_distance = 0;
        int daily_duration = 0;

        // Getting average over the whole list
        for (Patient patient : patient_list.values()) {
            daily_amb += patient.getToday_ambulation();
            daily_distance += patient.getToday_distance();
            daily_duration += patient.getToday_duration();
        }

        DecimalFormat decimalFormat = new DecimalFormat("0.0");

        double daily_speed = 0.0;
        if (daily_duration > 0) {
            daily_speed = daily_distance / daily_duration * 0.0113636 * 60;
        }
        daily_amb /= patient_list.size();
        daily_distance /= patient_list.size();
        daily_duration /= patient_list.size();

        SimpleDateFormat df1 = new SimpleDateFormat("m:s");
        SimpleDateFormat df2 = new SimpleDateFormat("mm:ss");
        String todayTime = "00:00:00";

            // time: hh:mm:ss
            int hour = daily_duration/3600;
            int left_duration = daily_duration - hour*3600;
            int min = left_duration / 60;
            int sec = left_duration % 60;
            if(hour < 10) {
                try {
                    Date date = df1.parse(min + ":" + sec);
                    todayTime = "0"+hour+":"+df2.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Date date = df1.parse(min + ":" + sec);
                    todayTime = hour+":"+df2.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }


        num_of_amb.setText(Integer.toString(daily_amb));
        num_of_dis.setText(Integer.toString(daily_distance));
        num_of_avg_spd.setText(decimalFormat.format(daily_speed));
        num_of_dur.setText(todayTime);

    }


    private void setButtons(View view) {
        this.today = view.findViewById(R.id.today_button);
        today.setOnClickListener(this);
        this.week = view.findViewById(R.id.week_button);
        week.setOnClickListener(this);
        this.month = view.findViewById(R.id.month_button);
        month.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
       Log.d("onClick", mode);
       if(((String)view.getTag()).equals("today")) {
           if(mode.equals("Unit")) {
               displayUnitAverage();
           } else {
               new GraphHelper(getContext(), this, "Today").execute(Integer.toString(patient_room), date, mode);
           }
       } else if(view.getTag().equals("week")) {
           new GraphHelper(getContext(), this, "Week").execute(Integer.toString(patient_room), date, mode);
       } else if(view.getTag().equals("month")) {
           new GraphHelper(getContext(), this, "Month").execute(Integer.toString(patient_room), date, mode);
       }
    }

    @Override
    public void processFinish(Object o) {
        HashMap<String, List<GraphEntry>> map = (HashMap<String, List<GraphEntry>>)o;

        if (map.containsKey("Month")) {
            num_of_amb.setText("0");
            num_of_dis.setText("0");
            num_of_avg_spd.setText("0.0");
            num_of_dur.setText("00:00:00");

            List<GraphEntry> receive = map.get("Month");
            // receive is not null
            if(receive.size() > 0) {
                int total_amb = 0;
                int total_dist = 0;
                int total_dur = 0;
                double speed = 0.0;
                if(mode.equals("Unit")) {
                    GraphEntry daily_data = receive.get(0);
                    total_amb = daily_data.getNum_amb();
                    total_dist = daily_data.getDistance();
                    total_dur = daily_data.getDuration();
                    speed = daily_data.getSpeed();
                } else {
                    for (int i = 0; i < receive.size(); i++) {
                        GraphEntry daily_data = receive.get(i);

                        int distance = daily_data.getDistance();
                        total_dist += distance;
                        int duration = daily_data.getDuration();
                        total_dur += duration;
                        int num_amb = daily_data.getNum_amb();
                        Log.d("month_num_amb", Integer.toString(num_amb));
                        total_amb += num_amb;
                    }
                    if (total_dur > 0) {
                        speed = total_dist / total_dur * 0.0113636 * 60;
                    }
                }

                String month_dur = "00:00:00";
                SimpleDateFormat df1 = new SimpleDateFormat("m:s");
                SimpleDateFormat df2 = new SimpleDateFormat("mm:ss");

                // time: hh:mm:ss
                int hour = total_dur/3600;
                int left_duration = total_dur - hour*3600;
                int min = left_duration / 60;
                int sec = left_duration % 60;
                if(hour < 10) {
                    try {
                        Date date = df1.parse(min + ":" + sec);
                        month_dur = "0"+hour+":"+df2.format(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Date date = df1.parse(min + ":" + sec);
                        month_dur = hour+":"+df2.format(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                DecimalFormat decimalFormat = new DecimalFormat("0.0");

                num_of_amb.setText(Integer.toString(total_amb));
                num_of_dis.setText(Integer.toString(total_dist));
                num_of_avg_spd.setText(decimalFormat.format(speed));
                num_of_dur.setText(month_dur);

            }

        } else if (map.containsKey("Week")) {
            List<GraphEntry> receive = map.get("Week");
            num_of_amb.setText("0");
            num_of_dis.setText("0");
            num_of_avg_spd.setText("0.0");
            num_of_dur.setText("00:00:00");
            if(receive.size() > 0) {
                    int total_amb = 0;
                    int total_dist = 0;
                    int total_dur = 0;
                    double speed = 0.0;
                if(mode.equals("Unit")) {
                    GraphEntry daily_data = receive.get(0);
                    total_amb = daily_data.getNum_amb();
                    total_dist = daily_data.getDistance();
                    total_dur = daily_data.getDuration();
                    speed = daily_data.getSpeed();
                } else {
                    for (int i = 0; i < receive.size(); i++) {
                        GraphEntry daily_data = receive.get(i);

                        int distance = daily_data.getDistance();
                        total_dist += distance;
                        int duration = daily_data.getDuration();
                        total_dur += duration;
                        int num_amb = daily_data.getNum_amb();
                        total_amb += num_amb;
                    }
                    if (total_dur > 0) {
                        speed = total_dist / total_dur * 0.0113636 * 60;
                    }
                }

                String week_dur = "00:00:00";
                SimpleDateFormat df1 = new SimpleDateFormat("m:s");
                SimpleDateFormat df2 = new SimpleDateFormat("mm:ss");

                // time: hh:mm:ss
                int hour = total_dur/3600;
                int left_duration = total_dur - hour*3600;
                int min = left_duration / 60;
                int sec = left_duration % 60;
                if(hour < 10) {
                    try {
                        Date date = df1.parse(min + ":" + sec);
                        week_dur = "0"+hour+":"+df2.format(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Date date = df1.parse(min + ":" + sec);
                        week_dur = hour+":"+df2.format(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                DecimalFormat decimalFormat = new DecimalFormat("0.0");

                    num_of_amb.setText(Integer.toString(total_amb));
                    num_of_dis.setText(Integer.toString(total_dist));
                    num_of_avg_spd.setText(decimalFormat.format(speed));
                    num_of_dur.setText(week_dur);

            }

        } else {
            List<GraphEntry> receive = map.get("Today");

            num_of_amb.setText("0");
            num_of_dis.setText("0");
            num_of_avg_spd.setText("0.0");
            num_of_dur.setText("00:00:00");

            if (receive.size() > 0) {
                int total_amb = receive.size();
                int total_dist = 0;
                int total_dur = 0;
                double speed = 0.0;
                for (int i = 0; i < receive.size(); i++) {
                    GraphEntry daily_data = receive.get(i);

                    int distance = daily_data.getDistance();
                    total_dist += distance;
                    int duration = daily_data.getDuration();
                    total_dur += duration;
                }

                if(total_dur > 0) {
                    speed = (total_dist/total_dur) * 0.0113636 * 60;
                }

                String todayTime = "00:00:00";
                SimpleDateFormat df1 = new SimpleDateFormat("m:s");
                SimpleDateFormat df2 = new SimpleDateFormat("mm:ss");

                // time: hh:mm:ss
                int hour = total_dur/3600;
                int left_duration = total_dur - hour*3600;
                int min = left_duration / 60;
                int sec = left_duration % 60;
                if(hour < 10) {
                    try {
                        Date date = df1.parse(min + ":" + sec);
                        todayTime = "0"+hour+":"+df2.format(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Date date = df1.parse(min + ":" + sec);
                        todayTime = hour+":"+df2.format(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                DecimalFormat decimalFormat = new DecimalFormat("0.0");

                num_of_amb.setText(Integer.toString(total_amb));
                num_of_dis.setText(Integer.toString(total_dist));
                num_of_avg_spd.setText(decimalFormat.format(speed));
                num_of_dur.setText(todayTime);
            }
        }

    }

}
