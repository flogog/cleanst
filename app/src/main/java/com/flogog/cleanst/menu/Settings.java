package com.flogog.cleanst.menu;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.flogog.cleanst.R;

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
        cleanStActionBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpTo(Settings.this, Settings.this.getIntent());
            }
        });
    }
}
