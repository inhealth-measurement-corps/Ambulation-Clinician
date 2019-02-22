package com.ambulation.inhealth.inhealth_ambulation_project_v2;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends ViewModel {

    private final MutableLiveData<List<Patient>> plist = new MutableLiveData<List<Patient>>();
    private final MutableLiveData<Patient> patient_info = new MutableLiveData<Patient>();
    private final MutableLiveData<Patient> patient_detail = new MutableLiveData<Patient>();
    private final MutableLiveData<String> current_state = new MutableLiveData<String>();
    private final MutableLiveData<Integer> fav_room = new MutableLiveData<>();


    public void setPatientList(List<Patient> data) {
        plist.setValue(data);
    }

    public LiveData<List<Patient>> getPatientList() {
        return plist;
    }

    public void setPatientInfo(Patient patient) {
        patient_info.setValue(patient);
    }

    public LiveData<Patient> getPatientInfo() {
        return patient_info;
    }

    public void setPatientDetail(Patient patient) {
        patient_detail.setValue(patient);
    }

    public LiveData<Patient> getPatientDetail() {
        return patient_detail;
    }

    public void setCurrentState(String state) {
        current_state.setValue(state);
    }

    public LiveData<String> getCurrentState() {
        return current_state;
    }

    public void setFav_room(int room) {fav_room.setValue(room); }

    public LiveData<Integer> getFav_room() {return fav_room;}

    public void removeFav_room() {fav_room.postValue(null);}


}


