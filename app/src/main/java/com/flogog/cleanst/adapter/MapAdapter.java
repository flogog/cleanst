package com.flogog.cleanst.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by flogog on 9/18/16.
 */
public class MapAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> mapFragments;

    public MapAdapter(FragmentManager fm, ArrayList<Fragment> fragments){
        super(fm);
        this.mapFragments = fragments;
    }
    @Override
    public Fragment getItem(int position) {
        return mapFragments.get(position);
    }

    @Override
    public int getCount() {
        return mapFragments.size();
    }


}
