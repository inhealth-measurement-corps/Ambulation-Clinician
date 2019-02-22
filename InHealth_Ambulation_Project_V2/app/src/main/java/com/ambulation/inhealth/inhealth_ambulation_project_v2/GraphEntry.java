package com.ambulation.inhealth.inhealth_ambulation_project_v2;

public class GraphEntry {
    float day;
    int distance;
    float speed;
    int duration;
    int num_amb;

    public GraphEntry(float day, int distance, float speed, int duration, int num_amb) {
        this.day = day;
        this.distance = distance;
        this.speed = speed;
        this.duration = duration;
        this.num_amb = num_amb;
    }

    public float getDay() {
        return day;
    }

    public int getDistance() {
        return distance;
    }

    public float getSpeed() {
        return speed;
    }

    public int getDuration() { return duration; }

    public int getNum_amb() {return num_amb; }
}
