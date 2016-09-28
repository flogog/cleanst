package com.flogog.cleanst;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.flogog.cleanst.adapter.MapAdapter;
import com.flogog.cleanst.maps.CleanstMap;
import com.flogog.cleanst.maps.CleanstMapSuggestion;
import com.flogog.cleanst.menu.About;
import com.flogog.cleanst.menu.Settings;

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

        cleanstActionBar = (Toolbar) findViewById(R.id.toolbar);


        context = getApplicationContext();

        if(cleanstActionBar!=null) {
            setSupportActionBar(cleanstActionBar);
        }


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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent ;
        switch (item.getItemId()){
            case R.id.mAbout:
                intent = new Intent(this, About.class);
                startActivity(intent);
                break;
            case R.id.mSettings:
                intent = new Intent(this, Settings.class);
                startActivity(intent);
                break;
            case R.id.iLogin:
                intent = new Intent(this, Login.class);
                startActivity(intent);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    public void addLocation(){
        Intent intent = new Intent(this, NewLocation.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.maps_menu,menu);
        return true;
    }




}
