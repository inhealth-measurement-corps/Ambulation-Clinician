package com.ambulation.inhealth.inhealth_ambulation_project_v2;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

// Since this is an object collection, use a FragmentStatePagerAdapter,
// and NOT a FragmentPagerAdapter.
public class DetailCollectionPagerAdapter extends FragmentStatePagerAdapter {
    private int length;
    public DetailCollectionPagerAdapter(FragmentManager fm, int length) {
        super(fm);
        this.length = length;
    }

    /**
     * return the Fragment associated with a specified position
     * @param i
     * @return
     */
    @Override
    public Fragment getItem(int i) {
        Log.d("PagerAdapter", Integer.toString(i));
        Fragment fragment = new DetailObjectFragment();
        Bundle args = new Bundle();
        args.putInt(DetailObjectFragment.ARG_OBJECT, i);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Return the number of views available
     * @return
     */
    @Override
    public int getCount() {
        return length;
    }



}
