package com.flogog.cleanst.menu;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.flogog.cleanst.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by flogog on 9/26/16.
 */

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar cleanStActionBar = (Toolbar) findViewById(R.id.toolbar);
        cleanStActionBar.setSaveFromParentEnabled(true);
        cleanStActionBar.setNavigationIcon(R.drawable.left);
        cleanStActionBar.setTitle(R.string.app_name);
        cleanStActionBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpTo(Settings.this, Settings.this.getIntent());
            }
        });


        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("meters");
        spinnerArray.add("miles");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = (Spinner) findViewById(R.id.spinner);
        sItems.setAdapter(adapter);

        String selected = sItems.getSelectedItem().toString();
        savePreference(selected);


    }

    public void savePreference(String selected){
        SharedPreferences preferences =  getSharedPreferences("settings", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("units",selected);

        editor.apply();

    }
}
