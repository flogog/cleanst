package com.flogog.cleanst;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.firebase.client.ChildEventListener;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.flogog.cleanst.pojo.Location;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewLocation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_location);

        CircleImageView recycle = (CircleImageView) findViewById(R.id.recycle);
        recycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    newRecycleBin();
            }
        });

        CircleImageView waste = (CircleImageView) findViewById(R.id.trash_can);
        waste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    public void newRecycleBin() {
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = service.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        android.location.Location location = service.getLastKnownLocation(provider);
        LatLng loc = new LatLng(location.getLatitude(),location.getLongitude());
        System.out.print(location.getLatitude());

    }


    private void createLocationFirebase(Location loc){
        Firebase myFirebaseRef = new Firebase("https://cleanst-ff026.firebaseio.com");
        myFirebaseRef.child("locations").child("comment").setValue(loc.getComment());
        myFirebaseRef.child("locations").child("lat").setValue(loc.getLatitude());
        myFirebaseRef.child("locations").child("location_id").setValue(loc.getLocationId());
        myFirebaseRef.child("locations").child("long").setValue(loc.getLongitude());
        myFirebaseRef.child("locations").child("type").setValue(loc.getType());

    }
}

