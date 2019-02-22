package com.ambulation.inhealth.inhealth_ambulation_project_v2;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

// Instances of this class are fragments representing a single
// object in our collection.
public class DetailObjectFragment extends Fragment implements AsyncResponse {
    public static final String ARG_OBJECT = "object";
    private String date;
    private int patient_room;
    private Patient patient;
    private CombinedChart today_graph;
    private SharedViewModel model;

    Button pin_button;
    Boolean isToday;
    Boolean isWeek;
    Boolean isMonth;

    /**
     * the max value in today's distance graph
     */
    float today_max_left;
    /**
     * the max value in today's avg speed graph
     */
    float today_max_right;
    /**
     * the max value in this week's distance graph
     */
    float week_max_left;
    /**
     * the max value in this week's avg speed graph
     */
    float week_max_right;
    /**
     * the max value in this month's distance graph
     */
    float month_max_left;
    /**
     * the max value in this month's avg speed graph
     */
    float month_max_right;

    /**
     * first day on unit
     */
    int start_date;


    /**
     * data to plot
     */
    ArrayList<Entry> today_distance = new ArrayList<Entry>();
    ArrayList<Entry> today_speed = new ArrayList<Entry>();
    ArrayList<BarEntry> today_cumulative_distance = new ArrayList<BarEntry>();

    ArrayList<Entry> week_distance = new ArrayList<Entry>();
    ArrayList<Entry> week_speed = new ArrayList<Entry>();
    ArrayList<BarEntry> week_cumulative_distance = new ArrayList<BarEntry>();

    ArrayList<Entry> month_distance = new ArrayList<Entry>();
    ArrayList<Entry> month_speed = new ArrayList<Entry>();
    ArrayList<BarEntry> month_cumulative_distance = new ArrayList<BarEntry>();


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(
                R.layout.fragment_collection_object, container, false);

        Date d = new Date();
        date = (String) android.text.format.DateFormat.format("yyyy-MM-dd", d.getTime());

        today_graph = rootView.findViewById(R.id.today_graph);
        // default: today's graph
        isToday = true;
        isWeek = false;
        isMonth = false;


        // set summary data
        this.model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        model.getPatientList().observe(this, plist -> {
            Bundle args = getArguments();
            int index = args.getInt(ARG_OBJECT);
            this.patient = plist.get(Math.min(plist.size()-1, index));
            this.patient_room = patient.getRoom();
            ((TextView) rootView.findViewById(R.id.room_num)).setText("Room #" + patient_room);
            displaySummary();
            displayTable();
            displayGraph();
        });



        return rootView;
    }

    private void displaySummary() {
        DecimalFormat decimalFormat = new DecimalFormat("0.0");

        TextView los_num = getView().findViewById(R.id.ur_los);
        TextView amb_num = getView().findViewById(R.id.ur_amb);
        TextView dis_num = getView().findViewById(R.id.ur_cumdis);
        TextView spd_num = getView().findViewById(R.id.ur_spd);

        los_num.setText(decimalFormat.format(patient.getPcu_los()));
        amb_num.setText(Integer.toString(patient.getTotal_ambulation()));
        dis_num.setText(Integer.toString(patient.getTotal_distance()));
        spd_num.setText(decimalFormat.format(patient.getTotal_speed()));
    }

    private void displayTable() {
        DecimalFormat decimalFormat = new DecimalFormat("0.0");

        TextView tb_today_amb = getView().findViewById(R.id.tb_today_amb);
        TextView tb_total_amb = getView().findViewById(R.id.tb_total_amb);
        TextView tb_yest_amb = getView().findViewById(R.id.tb_yest_amb);
        TextView tb_today_spd = getView().findViewById(R.id.tb_today_spd);
        TextView tb_total_spd = getView().findViewById(R.id.tb_total_spd);
        TextView tb_yest_spd = getView().findViewById(R.id.tb_yest_spd);
        TextView tb_today_dist = getView().findViewById(R.id.tb_today_dist);
        TextView tb_total_dist = getView().findViewById(R.id.tb_total_dist);
        TextView tb_yest_dist = getView().findViewById(R.id.tb_yest_dist);
        TextView tb_today_dur = getView().findViewById(R.id.tb_today_dur);
        TextView tb_total_dur = getView().findViewById(R.id.tb_total_dur);
        TextView tb_yest_dur = getView().findViewById(R.id.tb_yest_dur);

        tb_today_amb.setText(Integer.toString(patient.getToday_ambulation()));
        tb_total_amb.setText(Integer.toString(patient.getTotal_ambulation()));
        tb_yest_amb.setText(Integer.toString(patient.getYes_ambulation()));
        tb_today_spd.setText(decimalFormat.format(patient.getToday_speed()));
        tb_total_spd.setText(decimalFormat.format(patient.getTotal_speed()));
        tb_yest_spd.setText(decimalFormat.format(patient.getYes_speed()));
        tb_today_dist.setText(Integer.toString(patient.getToday_distance()));
        tb_total_dist.setText(Integer.toString(patient.getTotal_distance()));
        tb_yest_dist.setText(Integer.toString(patient.getYes_distance()));


        SimpleDateFormat df1 = new SimpleDateFormat("m:s");
        SimpleDateFormat df2 = new SimpleDateFormat("mm:ss");
        String todayTime = "00:00:00";
        String yesTime = "00:00:00";
        String totalTime = "00:00:00";

        int today_duration = patient.getToday_duration();
        int yest_duration = patient.getYes_duration();
        int total_duration = patient.getTotal_duration();

        // time: hh:mm:ss
        int hour = today_duration/3600;
        int left_duration = today_duration - hour*3600;
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

        tb_today_dur.setText(todayTime);

        // time: hh:mm:ss
        hour = yest_duration/3600;
        left_duration = yest_duration - hour*3600;
        min = left_duration / 60;
        sec = left_duration % 60;
        if(hour < 10) {
            try {
                Date date = df1.parse(min + ":" + sec);
                yesTime = "0"+hour+":"+df2.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Date date = df1.parse(min + ":" + sec);
                yesTime = hour+":"+df2.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        tb_yest_dur.setText(yesTime);

        // time: hh:mm:ss
        hour = total_duration/3600;
        left_duration = total_duration - hour*3600;
        min = left_duration / 60;
        sec = left_duration % 60;
        if(hour < 10) {
            try {
                Date date = df1.parse(min + ":" + sec);
                totalTime = "0"+hour+":"+df2.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Date date = df1.parse(min + ":" + sec);
                totalTime = hour+":"+df2.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        tb_total_dur.setText(totalTime);

    }

    private void displayGraph() {
        new GraphHelper(getContext(), this, "Today").execute(Integer.toString(patient_room), date, "Patient");
        new GraphHelper(getContext(), this, "Week").execute(Integer.toString(patient_room), date, "Patient");
        new GraphHelper(getContext(), this, "Month").execute(Integer.toString(patient_room), date, "Patient");
    }

    @Override
    public void processFinish(Object o) {
        HashMap<String, List<GraphEntry>> map = (HashMap<String, List<GraphEntry>>)o;

       if (map.containsKey("Month")) {

            List<GraphEntry> receive = map.get("Month");

           if(receive.size() > 0) {
               this.month_max_left = 0;
               this.month_max_right = 0;

               float cumulative_distance = 0;
               for (int i = 0; i < receive.size(); i++) {
                   // Each element will have format: day<>distance<>speed
                   GraphEntry daily_data = receive.get(i);
                   float day = daily_data.getDay();
                   if (i == 0) {
                       this.start_date = (int) day;
                       Log.d("start_date", Integer.toString(start_date));
                   }
                   int distance = daily_data.getDistance();
                   float speed = daily_data.getSpeed();
                   cumulative_distance += distance;

                   //compare and reset the maxRight (speed)
                   if (speed > month_max_right) {
                       month_max_right = speed;
                   }
                   day = day - start_date + 1;
                   Entry distanceEntry = new Entry(day, distance);
                   Entry speedEntry = new Entry(day, speed);
                   BarEntry cumulativeEntry = new BarEntry(day, cumulative_distance);
                   month_cumulative_distance.add(cumulativeEntry);
                   month_distance.add(distanceEntry);
                   month_speed.add(speedEntry);
               }
               month_max_left = cumulative_distance;
           }

        } else if (map.containsKey("Week")) {
           List<GraphEntry> receive = map.get("Week");
           if(receive.size() > 0) {

               this.week_max_left = 0;
               this.week_max_right = 0;

               float cumulative_distance = 0;
               for (int i = 0; i < receive.size(); i++) {
                   GraphEntry daily_data = receive.get(i);
                   float day = daily_data.getDay();
                   int distance = daily_data.getDistance();
                   float speed = (float) (daily_data.getSpeed() * 60 * 0.0113636);
                   cumulative_distance += distance;

                   //compare and reset the maxRight (speed)
                   if (speed > week_max_right) {
                       week_max_right = speed;
                   }

                   Entry distanceEntry = new Entry(day, distance);
                   Entry speedEntry = new Entry(day, speed);
                   BarEntry cumulativeEntry = new BarEntry(day, cumulative_distance);
                   week_cumulative_distance.add(cumulativeEntry);
                   week_distance.add(distanceEntry);
                   week_speed.add(speedEntry);
               }
               week_max_left = cumulative_distance;

           }

        } else {
           List<GraphEntry> receive = map.get("Today");
           if (receive.size() > 0) {
               this.today_max_left = 0;
               this.today_max_right = 0;
               // For Today's data, remember to graph that as well.
               float cumulative_distance = 0;

               for (int i = 0; i < receive.size(); i++) {
                   GraphEntry daily_data = receive.get(i);
                   float day = daily_data.getDay();
                   float distance = daily_data.getDistance();
                   // Speed from data is good to go, no need to convert
                   float speed = daily_data.getSpeed();
                   cumulative_distance += distance;

                   //compare and reset maxSpeed
                   if (today_max_right < speed) {
                       today_max_right = speed;
                   }

                   Entry distanceEntry = new Entry(day, distance);
                   Entry speedEntry = new Entry(day, speed);
                   BarEntry cumulativeEntry = new BarEntry(day, cumulative_distance);
                   today_cumulative_distance.add(cumulativeEntry);
                   today_distance.add(distanceEntry);
                   today_speed.add(speedEntry);
               }
               today_max_left = cumulative_distance;
               graph(today_max_left, today_max_right); // default graph
           }
       }
        initButtons(getView());
    }

    private void graph(float maxLeft, float maxRight) {
        int upperLeft;
        int countLeft;
        float upperRight;
        int countRight;

        //set the scale of Yleft and YRight based on maxLeft and maxRight
        // 2 < the number of labels < 10
        if (maxLeft <= 500) { //too small (lose detail): set upper to be round-up-to-nearest-100n and label interval to be 100
            countLeft = (int) (maxLeft / 100) + 2;
            upperLeft = (countLeft - 1) * 100;
        } else if (maxLeft >= 5000) {//too large (labels squeezed):set upper to round-up-to-nearest-5000n and label interval to be 5000
            countLeft = (int) (maxLeft / 5000) + 2;
            upperLeft = (countLeft - 1) * 5000;
        } else { //if 500 < maxLeft < 5000 , round up to the nearest 500n and set interval to be 500
            countLeft = (int) (maxLeft / 500) + 2;
            upperLeft = (countLeft - 1) * 500;
        }

        //for speed, set interval to be 0.5
        countRight = (int) (maxRight * 2) + 2;
        upperRight = (countRight - 1) * 0.5f;

        //MPAndroidChart
        CombinedData data = new CombinedData();

        //set left Y axis
        YAxis leftYAxis = today_graph.getAxisLeft();
        leftYAxis.setAxisMinimum(0f);
        leftYAxis.setAxisMaximum(upperLeft);
        leftYAxis.setLabelCount(countLeft, true);
        leftYAxis.setDrawGridLines(false);
        leftYAxis.setAxisLineWidth(3);
        leftYAxis.setTextColor(Color.WHITE);
        leftYAxis.setTextSize(15);
        leftYAxis.setXOffset(20);

        //set right Y axis
        YAxis rightYAxis = today_graph.getAxisRight();
        rightYAxis.setAxisMinimum(0f);
        rightYAxis.setAxisMaximum(upperRight);
        rightYAxis.setLabelCount(countRight, true);
        rightYAxis.setDrawGridLines(false);
        rightYAxis.setAxisLineWidth(3);
        rightYAxis.setTextColor(Color.RED);
        rightYAxis.setTextSize(15);
        rightYAxis.setXOffset(20);

        //set X axis
        XAxis xAxis = today_graph.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisLineWidth(3);
        xAxis.setTextColor(getResources().getColor(R.color.black));
        xAxis.setTextSize(13);
        xAxis.setYOffset(10);
        if (isToday) {
            xAxis.setValueFormatter(new AxisValueFormatter(0));
            xAxis.setAxisMaximum(24f);
            xAxis.setLabelCount(3, true);
            data.setData(generateLineData(today_distance, today_speed));
            data.setData(generateBarData(today_cumulative_distance));
        } else if (isWeek) {
            //set X-axis labels to be Monday - Sunday instead of integers
            xAxis.setValueFormatter(new AxisValueFormatter(1));
            xAxis.setAxisMaximum(8f);
            xAxis.setLabelCount(9, true);
            xAxis.setDrawLabels(true);
            data.setData(generateBarData(week_cumulative_distance));
            data.setData(generateLineData(week_distance, week_speed));
        } else if (isMonth) {
            xAxis.setValueFormatter(new AxisValueFormatter(2));
            xAxis.setAxisMaximum(30f - start_date + 1); //x axis label start from the day before starting date
            xAxis.setLabelCount((int) ((30 - start_date) / 5) + 1, true);
            data.setData(generateLineData(month_distance, month_speed));
            data.setData(generateBarData(month_cumulative_distance));
        }
        today_graph.setData(data);
        today_graph.invalidate();
        //set legend to be gone
        today_graph.getLegend().setEnabled(false);
        today_graph.getDescription().setEnabled(false);
        today_graph.setExtraBottomOffset(15);

    }

    private LineData generateLineData(ArrayList<Entry> entries1, ArrayList<Entry> entries2) {
        LineData d = new LineData();

        LineDataSet set1 = new LineDataSet(entries1, "Line");
        set1.setColors(Color.WHITE);
        set1.setLineWidth(2.5f);
        set1.setCircleRadius(5f);
        set1.setCircleColor(Color.WHITE);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setDrawValues(false);

        LineDataSet set2 = new LineDataSet(entries2, "Line");
        set2.setColors(Color.RED);
        set2.setLineWidth(2.5f);
        set2.setCircleRadius(5f);
        set2.setCircleColor(Color.RED);
        set2.setAxisDependency(YAxis.AxisDependency.RIGHT);
        set2.setDrawValues(false);
        d.addDataSet(set2);
        d.addDataSet(set1);

        return d;

    }


    private BarData generateBarData(ArrayList<BarEntry> entries) {
        BarDataSet set = new BarDataSet(entries, "Bar");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setDrawValues(false);
        BarData b = new BarData(set);
        b.setBarWidth(0.7f);
        return b;
    }


    /**
     * customize X-axis label
     */
    public class AxisValueFormatter implements IAxisValueFormatter {

        final String[] days = new String[9];

        int mRange;

        public AxisValueFormatter(int range) {
            this.mRange = range;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {

            if (this.mRange == 0) { // day
                return Float.toString(value);
            } else if (this.mRange == 1) { // week
                days[0] = "";
                days[1] = "Sun";
                days[2] = "Mon";
                days[3] = "Tue";
                days[4] = "Wed";
                days[5] = "Thur";
                days[6] = "Fri";
                days[7] = "Sat";
                days[8] = "";

                return days[Math.round(value)];

            } else { //month

                DateFormat dateFormat = new SimpleDateFormat("MMM");
                Date date = new Date();
                Log.d("Month", dateFormat.format(date));
                String month = dateFormat.format(date);
                return month + " " + Integer.toString((int) (value + start_date - 1));
            }

        }


    }

    /**
     * initialize buttons; each today/week/month button controls the switch of graphs
     */
    public void initButtons(View view) {

        pin_button = view.findViewById(R.id.pinButton);

        final Button today_button = view.findViewById(R.id.today_button);
        final Button week_button = view.findViewById(R.id.week_button);
        final Button month_button = view.findViewById(R.id.month_button);

        today_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                today_graph.invalidate();
                today_graph.clear();
                isToday = true;
                isWeek = false;
                isMonth = false;
                week_button.setBackgroundColor(Color.WHITE);
                month_button.setBackgroundColor(Color.WHITE);
                today_button.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                if(today_distance.size() > 0) {
                    graph(today_max_left, today_max_right);
                }
            }

        });

        week_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                today_graph.invalidate();
                today_graph.clear();
                isToday = false;
                isWeek = true;
                isMonth = false;
                today_button.setBackgroundColor(Color.WHITE);
                month_button.setBackgroundColor(Color.WHITE);
                week_button.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                if(week_distance.size() > 0) {
                    graph(week_max_left, week_max_right);
                }
            }

        });

        month_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                today_graph.invalidate();
                today_graph.clear();
                isToday = false;
                isWeek = false;
                isMonth = true;
                today_button.setBackgroundColor(Color.WHITE);
                week_button.setBackgroundColor(Color.WHITE);
                month_button.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                if(month_distance.size() > 0) {
                    graph(month_max_left, month_max_right);
                }
            }

        });

        pin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int fav_room = patient.getRoom();
                pin_button.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                model.setFav_room(fav_room);
                Toast.makeText(getActivity(), "You have pinned this room!",
                                  Toast.LENGTH_SHORT).show();


            }
        });


    }
}
