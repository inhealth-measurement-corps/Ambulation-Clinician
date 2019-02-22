package com.ambulation.inhealth.inhealth_ambulation_project_v2;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class UnitListFragment extends Fragment  {

    private static RecyclerView mRecyclerView;
    private static RecyclerView.Adapter mAdapter;

    private static LinearLayoutManager layoutManager;

    private static ArrayList<Patient> patientList;

    private SharedViewModel model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_unit_list, container, false);


        mRecyclerView = view.findViewById(R.id.my_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        patientList = new ArrayList<>();
        this.model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        model.getPatientList().observe(this, plist -> {
            this.patientList = (ArrayList<Patient>)plist;
            mAdapter = new PatientListAdapter(patientList.size(), patientList);
            setRecycler();
        });
        return view;
    }

    public void setRecycler() {
        // set LayoutManager and Adapter to RecyclerView
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
    }


}
