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

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actionbar_cleanst);

        Toolbar cleanStActionBar = (Toolbar) findViewById(R.id.toolbar);
        cleanStActionBar.setSaveFromParentEnabled(true);
        cleanStActionBar.setNavigationIcon(R.drawable.cleanst_logo_nt);
        cleanStActionBar.setTitle(R.string.app_name);
        cleanStActionBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpTo(About.this, About.this.getIntent());
            }
        });
    }
}
