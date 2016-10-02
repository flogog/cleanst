package com.flogog.cleanst.menu;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.flogog.cleanst.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by flogog on 9/26/16.
 */

public class Settings extends AppCompatActivity {

    private Spinner sItems;

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
        spinnerArray.add(getString(R.string.spinner_value1));
        spinnerArray.add(getString(R.string.spinner_value2));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sItems = (Spinner) findViewById(R.id.spinner);
        sItems.setAdapter(adapter);



    }

    public void savePreference(View v){
        SharedPreferences preferences =  getSharedPreferences(getString(R.string.settings), Context.MODE_PRIVATE);

        String selected = sItems.getSelectedItem().toString();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(getString(R.string.item_units),selected);

        editor.apply();

    }
}
