package com.flogog.cleanst;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.firebase.client.Firebase;
import com.flogog.cleanst.pojo.Location;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewLocation extends AppCompatActivity {

    private Double lat;
    private Double lng;
    private String table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_location);

        Bundle param        = getIntent().getExtras();
        table    = (String)param.get("table");
        lat       = (Double) param.get("lat");
        lng       = (Double) param.get("lng");

        CircleImageView recycle = (CircleImageView) findViewById(R.id.recycle);
        recycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location location = new Location();
                location.setType(getString(R.string.table_locations_recycle_type));
                location.setLocationId("Recycle Bin");
                location.setLatitude(lat);
                location.setLongitude(lng);
                location.setComment("");
                createLocationFirebase(location,table);
            }
        });

        CircleImageView waste = (CircleImageView) findViewById(R.id.trash_can);
        waste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location location = new Location();
                location.setType(getString(R.string.table_locations_waste_type));
                location.setLocationId("Waste Bin");
                location.setLatitude(lat);
                location.setLongitude(lng);
                location.setComment("");
                createLocationFirebase(location,table);
            }
        });

    }

    private void createLocationFirebase(Location loc,String table){
        Firebase myFirebaseRef = new Firebase(getString(R.string.firebase_database));
        Firebase locationsTable = myFirebaseRef.child(table).push();
        locationsTable.child(getString(R.string.firebase_locations_comment)).setValue(loc.getComment());
        locationsTable.child(getString(R.string.firebase_locations_latitude)).setValue(loc.getLatitude().toString());
        locationsTable.child(getString(R.string.firebase_locations_id)).setValue(loc.getLocationId());
        locationsTable.child(getString(R.string.firebase_locations_longitude)).setValue(loc.getLongitude().toString());
        locationsTable.child(getString(R.string.firebase_locations_type)).setValue(loc.getType());
        System.out.print("Record inserted "+loc.toString());
    }

}


