package com.flogog.cleanst.maps;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
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
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.flogog.cleanst.MainActivity;
import com.flogog.cleanst.Manifest;
import com.flogog.cleanst.NewLocation;
import com.flogog.cleanst.R;
import com.flogog.cleanst.pojo.Location;
import com.flogog.cleanst.presenter.MapFragmentPresenter;
import com.flogog.cleanst.restAPI.IEndpointsAPI;
import com.flogog.cleanst.restAPI.adapter.RestAdapter;
import com.flogog.cleanst.restAPI.model.LocationResponse;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LOCATION_SERVICE;

public class CleanstMap extends Fragment {

    private ViewGroup container;
    private MapView mMapView;
    private GoogleMap googleMap;
    private LatLng ltln;
    private String locationName;
    private String locationDescription;
    private MapFragmentPresenter mapPresenter;
    private static final int LOCATION_REQUEST_CODE = 101;
    private Context context;
    private ArrayList<Location> locations;


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

        CircleImageView imgFavorite = (CircleImageView) rootView.findViewById(R.id.newLocation);
        imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(container.getContext(), NewLocation.class);
                startActivity(intent);
            }
        });


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
                android.location.Location location = service.getLastKnownLocation(provider);
                LatLng loc = new LatLng(location.getLatitude(),location.getLongitude());

             /*   googleMap.addMarker(new MarkerOptions()
                        .position(location)
                        .title(locationName)
                        .snippet(locationDescription)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));*/
//                System.out.print(locations.size());
               /*     for(Location loc: locations){
                        drawMarker(new LatLng(Double.parseDouble(loc.getLatitude()),Double.parseDouble(loc.getLongitude())));
                    }*/

                    // Moving CameraPosition to last clicked position
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                    // Setting the zoom level in the map on last position  is clicked
                   googleMap.animateCamera(CameraUpdateFactory.zoomTo(17));

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                    @Override
                    public void onMapClick(LatLng arg0) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(context, NewLocation.class);
                        intent.putExtra("lat",arg0.latitude);
                        intent.putExtra("lng",arg0.longitude);
                        startActivity(intent);
                        Log.d("arg0", arg0.latitude + "-" + arg0.longitude);
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

    private void getLocations(final String type){
        RestAdapter restAdapter     =  new RestAdapter();
        Gson mediaRecentGson = restAdapter.createGSONDeserializeLocations();
        IEndpointsAPI endpointsAPI    = restAdapter.startConnectionRestAPI(mediaRecentGson);

        Call<LocationResponse> responseCall    = endpointsAPI.getLocations(type);

        //System.out.println("-------------"+responseCall.toString());
        responseCall.enqueue(new Callback<LocationResponse>() {
            @Override
            public void onResponse(Call<LocationResponse> call, Response<LocationResponse> response) {
                LocationResponse locationResponse = response.body();
                locations = locationResponse.getLocations();
                System.out.println("+++++++++ "+locationResponse.getLocations().size());
                // LatLng new location = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng))

            }

            @Override
            public void onFailure(Call<LocationResponse> call, Throwable throwable) {
                Toast.makeText(context,"Error en la conextion, Try Again", Toast.LENGTH_LONG).show();
                Log.i("Error en la conextion",throwable.toString());
            }
        });

    }

    private void getLocationsFirebase(){
        Firebase myFirebaseRef = new Firebase("https://cleanst-ff026.firebaseio.com/locations");

        Query queryRef = myFirebaseRef.orderByChild("type").equalTo("1");
        // System.out.println(dataSnapshot.getKey() + "is" + value.get("socialNumber"));

        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(com.firebase.client.DataSnapshot dataSnapshot, String s) {
                System.out.println("SNAPSHOT "+dataSnapshot.getValue());
                Map<String,Object> value = (Map<String, Object>) dataSnapshot.getValue();
                Location loc = new Location();
                loc.setComment(String.valueOf(value.get("comment")));
                loc.setLatitude(String.valueOf(value.get("lat")));
                loc.setLongitude(String.valueOf(value.get("long")));
                loc.setLocationId(String.valueOf(value.get("location_id")));
                loc.setType(String.valueOf(value.get("type")));
                //locations.add(loc);
                System.out.print(loc.toString());
                drawMarker(new LatLng(Double.parseDouble(loc.getLatitude()),Double.parseDouble(loc.getLongitude())));
            }

            @Override
            public void onChildChanged(com.firebase.client.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(com.firebase.client.DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(com.firebase.client.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }



    private void drawMarker(LatLng point){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(point);
        googleMap.addMarker(markerOptions);
    }

    public void addLocation(){
        ((MainActivity)getActivity()).addLocation();
    }




}
