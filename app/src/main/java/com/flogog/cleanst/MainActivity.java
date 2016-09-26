package com.flogog.cleanst;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.flogog.cleanst.adapter.MapAdapter;
import com.flogog.cleanst.maps.CleanstMap;
import com.flogog.cleanst.maps.CleanstMapSuggestion;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Toolbar cleanstActionBar;
    private TabLayout cleanstTabLayout;
    private ViewPager cleanstViewPager;
    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cleanstActionBar = (Toolbar) findViewById(R.id.cleanstToolBar);
        cleanstActionBar.setTitle(R.string.app_name);
        cleanstActionBar.setLogo(R.drawable.cleanst_logo);


        context = getApplicationContext();

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new CleanstMap());
        fragments.add(new CleanstMapSuggestion());

        cleanstTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        cleanstViewPager = (ViewPager) findViewById(R.id.cleanstViewPager);

        cleanstViewPager.setAdapter(new MapAdapter(getSupportFragmentManager(),fragments));
        cleanstTabLayout.setupWithViewPager(cleanstViewPager);
        cleanstTabLayout.getTabAt(0).setText(getResources().getText(R.string.menu_around_me));
        cleanstTabLayout.getTabAt(1).setText(getResources().getText(R.string.menu_suggest));

    }




}
