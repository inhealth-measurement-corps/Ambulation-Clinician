package com.ambulation.inhealth.inhealth_ambulation_project_v2;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class DetailFragment extends Fragment {
    // When requested, this adapter returns a DetailObjectFragment,
    // representing an object in the collection.
    DetailCollectionPagerAdapter mCollectionPagerAdapter;
    ViewPager mViewPager;
    private FragmentActivity myContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        Bundle args = getArguments();
        int index = args.getInt("index");
        int length = args.getInt("length");

        mCollectionPagerAdapter =
                new DetailCollectionPagerAdapter(
                        myContext.getSupportFragmentManager(), length);

        mViewPager = (ViewPager) view.findViewById(R.id.pager);

        mViewPager.setAdapter(mCollectionPagerAdapter);

        mViewPager.setCurrentItem(index);

        return view;
    }



    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

}
