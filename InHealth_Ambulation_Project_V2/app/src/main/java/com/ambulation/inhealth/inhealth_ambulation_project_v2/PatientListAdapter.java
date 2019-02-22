package com.ambulation.inhealth.inhealth_ambulation_project_v2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PatientListAdapter extends RecyclerView.Adapter<PatientListAdapter.NumberViewHolder> {
    private static final String TAG = PatientListAdapter.class.getSimpleName();

    private int mNumberItems;

    private ArrayList<Patient> mPatients;

    /**
     * Constructor for PatientListAdapter that accepts a number of items to display and the specification
     * for the ListItemClickListener.
     *
     * @param numberOfItems Number of items to display in list
     */
    public PatientListAdapter(int numberOfItems, ArrayList<Patient> objects) {
        mNumberItems = numberOfItems;
        mPatients = objects;
    }

    /**
     *
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new NumberViewHolder that holds the View for each list item
     */
    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.listview_row;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        NumberViewHolder viewHolder = new NumberViewHolder(view);

        return viewHolder;
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the correct
     * indices in the list for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(NumberViewHolder holder, int position) {
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        Patient patient = mPatients.get(position);
        int room = patient.getRoom();
        double pcu_los = patient.getPcu_los();
        double total_los = patient.getTotal_los();
        int today_ambulation = patient.getToday_ambulation();
        int daily_ambulation = patient.getDaily_ambulation();
        int total_ambulation = patient.getTotal_ambulation();
        int today_distance = patient.getToday_distance();
        int yes_distance = patient.getYes_distance();
        int total_distance = patient.getTotal_distance();
        double today_speed = patient.getToday_speed();
        double yes_speed = patient.getYes_speed();
        double total_speed = patient.getTotal_speed();
        int today_duration = patient.getToday_duration();
        int yest_duration = patient.getYes_duration();
        int total_duration = patient.getTotal_duration();

        holder.tvRoom.setText(Integer.toString(room));
        holder.tvLos.setText(pcu_los + "/" + total_los);
        holder.tvAmb.setText(today_ambulation + "/" + daily_ambulation
                + "/" + total_ambulation);
        holder.tvDis.setText(today_distance + "/" +
                yes_distance + "/" + total_distance);
        holder.tvSpd.setText(decimalFormat.format(today_speed)+ "/" +
                decimalFormat.format(yes_speed) + "/" + decimalFormat.format(total_speed));

        SimpleDateFormat df1 = new SimpleDateFormat("m:s");
        SimpleDateFormat df2 = new SimpleDateFormat("mm:ss");
        String todayTime = "00:00:00";
        String yesTime = "00:00:00";
        String totalTime = "00:00:00";

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


        holder.tvTime.setText(todayTime + "/" +
                yesTime + "/" + totalTime);


    }


    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        return mNumberItems;
    }


    /**
     * Cache of the children views for a list item.
     */
    class NumberViewHolder extends RecyclerView.ViewHolder {

        // Will display the position in the list, ie 0 through getItemCount() - 1
        TextView tvRoom;
        TextView tvLos;
        TextView tvAmb;
        TextView tvDis;
        TextView tvSpd;
        TextView tvTime;

        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * TextViews and set an onClickListener to listen for clicks. Those will be handled in the
         * onClick method below.
         * @param itemView The View that you inflated in
         *                 {@link PatientListAdapter#onCreateViewHolder(ViewGroup, int)}
         */
        public NumberViewHolder(View itemView) {
            super(itemView);

            
           tvRoom = (TextView) itemView.findViewById(R.id.textview_list_row_rm);
           tvLos = (TextView) itemView.findViewById(R.id.textview_list_row_los);
           tvAmb = (TextView) itemView.findViewById(R.id.textview_list_row_amb);
           tvDis = (TextView) itemView.findViewById(R.id.textview_list_row_distance);
           tvSpd = (TextView) itemView.findViewById(R.id.textview_list_row_speed);
           tvTime = (TextView) itemView.findViewById(R.id.textview_list_row_time);
        }


    }




}
