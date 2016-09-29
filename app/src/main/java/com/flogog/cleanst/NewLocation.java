package com.flogog.cleanst;

import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.firebase.client.ChildEventListener;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.flogog.cleanst.pojo.Location;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class NewLocation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_location);

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

