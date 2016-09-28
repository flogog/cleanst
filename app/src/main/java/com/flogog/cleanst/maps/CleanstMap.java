package com.flogog.cleanst.maps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
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

import com.flogog.cleanst.MainActivity;
import com.flogog.cleanst.Manifest;
import com.flogog.cleanst.NewLocation;
import com.flogog.cleanst.R;
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

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LOCATION_SERVICE;

public class CleanstMap extends Fragment {

    private ViewGroup container;
    private MapView mMapView;
    private GoogleMap googleMap;
    private LatLng location;
    private String locationName;
    private String locationDescription;
    private MapFragmentPresenter mapPresenter;
    private static final int LOCATION_REQUEST_CODE = 101;
    private Context context;
    int locationCount = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity().getApplicationContext();
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
                location = new LatLng(40.289152, -76.654579);

                if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {


                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.setMyLocationEnabled(true);
                LocationManager service = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                String provider = service.getBestProvider(criteria, false);
                Location location = service.getLastKnownLocation(provider);
                LatLng loc = new LatLng(location.getLatitude(),location.getLongitude());



             /*   googleMap.addMarker(new MarkerOptions()
                        .position(location)
                        .title(locationName)
                        .snippet(locationDescription)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));*/
                ArrayList<LatLng> locations = getLocations("1");
                 if(locationCount!=0){
                    String lat = "";
                    String lng = "";
                    // Iterating through all the locations returned by getLocations("1")
                    for(int i=0;i<locations.size();i++){
                        drawMarker(locations.get(i));
                    }
                    // Moving CameraPosition to last clicked position
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng))));
                    // Setting the zoom level in the map on last position  is clicked
                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(17));
                }
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
                locationResponse
                LatLng new location = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng))
                System.out.println("+++++++++ "+locationResponse.getLocations().size());
            }

            @Override
            public void onFailure(Call<LocationResponse> call, Throwable throwable) {
                Toast.makeText(context,"Error en la conextion, Try Again", Toast.LENGTH_LONG).show();
                Log.i("Error en la conextion",throwable.toString());
            }
        });
    }

    public void addLocation(){
        ((MainActivity)getActivity()).addLocation();
    }
    
    private void drawMarker(LatLng point){
        // Creating an instance of MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions();
 
        // Setting latitude and longitude for the marker
        markerOptions.position(point);
 
        // Adding marker on the Google Map
        googleMap.addMarker(markerOptions);
    }
    
    



}
