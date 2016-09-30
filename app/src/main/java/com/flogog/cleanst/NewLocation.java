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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_location);

        Bundle param        = getIntent().getExtras();
        lat       = (Double) param.get("lat");
        lng       = (Double) param.get("lng");

        CircleImageView recycle = (CircleImageView) findViewById(R.id.recycle);
        recycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location location = new Location();
                location.setType(getString(R.string.table_locations_recycle_type));
                location.setLocationId("132145");
                location.setLatitude(lat.toString());
                location.setLongitude(lng.toString());
                createLocationFirebase(location);
            }
        });

        CircleImageView waste = (CircleImageView) findViewById(R.id.trash_can);
        waste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location location = new Location();
                location.setType(getString(R.string.table_locations_waste_type));
                location.setLocationId("132145");
                location.setLatitude(lat.toString());
                location.setLongitude(lng.toString());
                createLocationFirebase(location);
            }
        });

    }

    private void createLocationFirebase(Location loc){
        Firebase myFirebaseRef = new Firebase(getString(R.string.firebase_database_locations));
        Firebase locationsTable = myFirebaseRef.child(getString(R.string.firebase_locations)).push();
        locationsTable.child(getString(R.string.firebase_locations_comment)).setValue(loc.getComment());
        locationsTable.child(getString(R.string.firebase_locations_latitude)).setValue(loc.getLatitude());
        locationsTable.child(getString(R.string.firebase_locations_id)).setValue(loc.getLocationId());
        locationsTable.child(getString(R.string.firebase_locations_longitude)).setValue(loc.getLongitude());
        locationsTable.child(getString(R.string.firebase_locations_type)).setValue(loc.getType());
    }

}


