package com.ambulation.inhealth.inhealth_ambulation_project_v2;

import android.os.Parcel;
import android.os.Parcelable;


public class Patient implements Parcelable {

        private int id;
        private int room;
        private int pcu_los;
        private int total_los;
        private int today_ambulation;
        private int daily_ambulation;
        private int total_ambulation;
        private int today_distance;
        private int yes_distance;
        private int total_distance;
        private double today_speed;
        private double yes_speed;
        private double total_speed;
        private int today_duration;
        private int yes_duration;
        private int total_duration;
        private int yes_ambulation;

        private int current_distance;
        private double current_speed;
        private int current_duration;

        // Default constructor
        public Patient() {
            this.id = 0;
            this.room = 0;
            this.pcu_los = 0;
            this.total_los = 0;
            this.today_ambulation = 0;
            this.daily_ambulation = 0;
            this.total_ambulation = 0;
            this.today_distance = 0;
            this.yes_distance = 0;
            this.total_distance = 0;
            this.today_speed = 0;
            this.yes_speed = 0;
            this.total_speed = 0;
            this.today_duration = 0;
            this.yes_duration = 0;
            this.total_duration = 0;
            this.current_distance = 0;
            this.current_speed = 0;
            this.current_duration = 0;
        }

        public Patient(int id, int room, int pcu_los, int total_los, int today_ambulation, int daily_ambulation,
                       int total_ambulation, int today_distance, int yes_distance, int total_distance,
                       double today_speed, double yes_speed, double total_speed, int today_duration,
                       int yes_duration, int total_duration, int yes_ambulation) {
            this.id = id;
            this.room = room;
            this.pcu_los = pcu_los;
            this.total_los = total_los;
            this.today_ambulation = today_ambulation;
            this.daily_ambulation = daily_ambulation;
            this.total_ambulation = total_ambulation;
            this.today_distance = today_distance;
            this.yes_distance = yes_distance;
            this.total_distance = total_distance;
            this.today_speed = today_speed;
            this.yes_speed = yes_speed;
            this.total_speed = total_speed;
            this.today_duration = today_duration;
            this.yes_duration = yes_duration;
            this.total_duration = total_duration;
            this.yes_ambulation = yes_ambulation;
        }

        // example constructor that takes a Parcel and gives you an object populated with it's values
        private Patient(Parcel in) {
            this.id = in.readInt();
            this.room = in.readInt();
            this.pcu_los = in.readInt();
            this.total_los = in.readInt();
            this.today_ambulation = in.readInt();
            this.daily_ambulation = in.readInt();
            this.total_ambulation = in.readInt();
            this.today_distance = in.readInt();
            this.yes_distance = in.readInt();
            this.total_distance = in.readInt();
            this.today_speed = in.readDouble();
            this.yes_speed = in.readDouble();
            this.total_speed = in.readDouble();
            this.today_duration = in.readInt();
            this.yes_duration = in.readInt();
            this.total_duration = in.readInt();
            this.yes_ambulation = in.readInt();
        }

        /* everything below here is for implementing Parcelable */

        // 99.9% of the time you can just ignore this
        @Override
        public int describeContents() {
            return 0;
        }

        // write your object's data to the passed-in Parcel
        @Override
        public void writeToParcel(Parcel out, int flags) {
            out.writeInt(this.id);
            out.writeInt(this.room);
            out.writeInt(this.pcu_los);
            out.writeInt(this.total_los);
            out.writeInt(this.today_ambulation);
            out.writeInt(this.daily_ambulation);
            out.writeInt(this.total_ambulation);
            out.writeInt(this.today_distance);
            out.writeInt(this.yes_distance);
            out.writeInt(this.total_distance);
            out.writeDouble(this.today_speed);
            out.writeDouble(this.yes_speed);
            out.writeDouble(this.total_speed);
            out.writeInt(this.today_duration);
            out.writeInt(this.yes_duration);
            out.writeInt(this.total_duration);
            out.writeInt(this.yes_ambulation);
        }

        // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
        public static final Parcelable.Creator<Patient> CREATOR = new Parcelable.Creator<Patient>() {
            @Override
            public Patient createFromParcel(Parcel in) {
                return new Patient(in);
            }

            @Override
            public Patient[] newArray(int size) {
                return new Patient[size];
            }
        };

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getRoom() {
            return room;
        }

        public void setRoom(int room) {
            this.room = room;
        }

        public int getPcu_los() {
            return pcu_los;
        }

        public void setPcu_los(int pcu_los) {
            this.pcu_los = pcu_los;
        }

        public int getTotal_los() {
            return total_los;
        }

        public void setTotal_los(int total_los) {
            this.total_los = total_los;
        }

        public int getToday_ambulation() {
            return today_ambulation;
        }

        public void setToday_ambulation(int today_ambulation) {
            this.today_ambulation = today_ambulation;
        }

        public int getDaily_ambulation() {
            return daily_ambulation;
        }

        public void setDaily_ambulation(int daily_ambulation) {
            this.daily_ambulation = daily_ambulation;
        }

        public int getTotal_ambulation() {
            return total_ambulation;
        }

        public void setTotal_ambulation(int total_ambulation) {
            this.total_ambulation = total_ambulation;
        }

        public int getYes_ambulation() {return yes_ambulation;}

        public void setYes_ambulation(int yes_ambulation) {
            this.yes_ambulation = yes_ambulation;
        }

        public int getToday_distance() {
            return today_distance;
        }

        public void setToday_distance(int today_distance) {
            this.today_distance = today_distance;
        }

        public int getYes_distance() {
            return yes_distance;
        }

        public void setYes_distance(int yes_distance) {
            this.yes_distance = yes_distance;
        }

        public int getTotal_distance() {
            return total_distance;
        }

        public void setTotal_distance(int total_distance) {
            this.total_distance = total_distance;
        }

        public double getToday_speed() {
            return today_speed;
        }

        public void setToday_speed(double today_speed) {
            this.today_speed = today_speed;
        }

        public double getYes_speed() {
            return yes_speed;
        }

        public void setYes_speed(double yes_speed) {
            this.yes_speed = yes_speed;
        }

        public double getTotal_speed() {
            return total_speed;
        }

        public void setTotal_speed(double total_speed) {
            this.total_speed = total_speed;
        }

        public int getToday_duration() {
            return today_duration;
        }

        public void setToday_duration(int today_duration) {
            this.today_duration = today_duration;
        }

        public int getYes_duration() {
            return yes_duration;
        }

        public void setYes_duration(int yes_duration) {
            this.yes_duration = yes_duration;
        }

        public int getTotal_duration() {
            return total_duration;
        }

        public void setTotal_duration(int total_duration) {
            this.total_duration = total_duration;
        }

        public double getCurrent_speed() {
            return current_speed;
        }

        public void setCurrent_speed(double current_speed) {
            this.current_speed = current_speed;
        }

        public int getCurrent_duration() {
            return current_duration;
        }

        public void setCurrent_duration(int current_duration) {
            this.current_duration = current_duration;
        }

        public int getCurrent_distance() {
            return current_distance;
        }

        public void setCurrent_distance(int current_distance) {
            this.current_distance = current_distance;
        }
    }
