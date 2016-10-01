package com.flogog.cleanst.maps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.flogog.cleanst.NewLocation;
import com.flogog.cleanst.R;
import com.flogog.cleanst.pojo.Location;
import com.flogog.cleanst.presenter.MapFragmentPresenter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Map;


import static android.content.Context.LOCATION_SERVICE;

public class CleanstMap extends Fragment implements LocationListener {

    private ViewGroup container;
    private MapView mMapView;
    private GoogleMap googleMap;
    private String locationName;
    private String locationDescription;
    private MapFragmentPresenter mapPresenter;
    private static final int LOCATION_REQUEST_CODE = 101;
    private Context context;
    private ArrayList<Location> locations;
    public LocationManager locationManager;
    public double latitude;
    public double longitude;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity().getApplicationContext();
        Firebase.setAndroidContext(context);
        getLocationsFirebase();
        requestPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_REQUEST_CODE);
        final View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        this.container = container;
        // civCreate = (CircularImageView) rootView.findViewById(R.id.civProfilePic);

        mMapView = (MapView) rootView.findViewById(R.id.cleanstMap);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {

                googleMap = mMap;

                if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                googleMap.setMyLocationEnabled(true);

                LocationManager service = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                String provider = service.getBestProvider(criteria, false);
                android.location.Location currentLocation = service.getLastKnownLocation(provider);
                LatLng currentLatLng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());

                // Moving CameraPosition to last clicked position
                  googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
                // Setting the zoom level in the map on last position  is clicked
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(17));

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                    @Override
                    public void onMapClick(LatLng arg0) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(context, NewLocation.class);
                        intent.putExtra("lat", arg0.latitude);
                        intent.putExtra("lng", arg0.longitude);
                        startActivity(intent);
                        Log.d("arg0", arg0.latitude + "-" + arg0.longitude);
                    }
                });

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        return false;
                    }
                });
            }
        });

        return rootView;
    }

    protected void requestPermission(String permissionType, int
            requestCode) {
        int permission = ContextCompat.checkSelfPermission(getActivity(),
                permissionType);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{permissionType}, requestCode
            );
        }
    }


    private void getLocationsFirebase() {
        Firebase myFirebaseRef = new Firebase(getString(R.string.firebase_database_locations));

        Query queryRef = myFirebaseRef.orderByChild(getString(R.string.firebase_locations_type));
        queryRef.keepSynced(true);

        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Map<String, Object> values = (Map<String, Object>) child.getValue();
                        Location loc = new Location();
                        if(validateData(values)){
                            loc.setComment(String.valueOf(values.get(getString(R.string.firebase_locations_comment))));
                            loc.setLatitude(Double.parseDouble(String.valueOf(values.get(getString(R.string.firebase_locations_latitude)))));
                            loc.setLongitude(Double.parseDouble(String.valueOf(values.get(getString(R.string.firebase_locations_longitude)))));
                            loc.setLocationId(String.valueOf(values.get(getString(R.string.firebase_locations_id))));
                            loc.setType(String.valueOf(values.get(getString(R.string.firebase_locations_type))));
                            System.out.print(loc.toString());

                            drawMarker(loc);
                        }

                    }

            }

            private boolean validateData(Map<String, Object> map){
                if(map.get(getString(R.string.firebase_locations_latitude)) != null
                    && map.get(getString(R.string.firebase_locations_longitude)) != null
                                && map.get(getString(R.string.firebase_locations_type)) != null){
                    return true;
                }
                return false;
            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.err.println("Listener was cancelled");
            }
        });

    }

    private void drawMarker(Location location) {
        MarkerOptions markerOptions = new MarkerOptions();
        LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
        BitmapDescriptor location_trash;
        if (location.getType().equals("2")) {
            location_trash = BitmapDescriptorFactory.fromResource(R.drawable.rsz_recycle_loc);
        } else {
            location_trash = BitmapDescriptorFactory.fromResource(R.drawable.rsz_waste_loc);
        }

        markerOptions.position(point)
                .title(locationName)
                .snippet(locationDescription)
                .icon(location_trash);
        googleMap.addMarker(markerOptions);
    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        //Hey, a non null location! Sweet!

        //remove location callback:
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(this);

        //open the map:
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Toast.makeText(context.getApplicationContext(), "latitude:" + latitude + " longitude:" + longitude, Toast.LENGTH_SHORT).show();
        //searchNearestPlace(voice2text);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
