package com.flogog.cleanst;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.flogog.cleanst.adapter.MapAdapter;
import com.flogog.cleanst.maps.CleanstMap;
import com.flogog.cleanst.maps.CleanstMapSuggestion;
import com.flogog.cleanst.menu.About;
import com.flogog.cleanst.menu.Settings;
import com.flogog.cleanst.pojo.Location;
import com.flogog.cleanst.restAPI.IEndpointsAPI;
import com.flogog.cleanst.restAPI.adapter.RestAdapter;
import com.flogog.cleanst.restAPI.model.LocationResponse;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Toolbar cleanstActionBar;
    private TabLayout cleanstTabLayout;
    private ViewPager cleanstViewPager;
    public Context context;
    private ArrayList<Location> locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cleanstActionBar = (Toolbar) findViewById(R.id.toolbar);

        context = getApplicationContext();

        if(cleanstActionBar!=null) {
            setSupportActionBar(cleanstActionBar);
        }


        ArrayList<Fragment> fragments = new ArrayList<>();

        fragments.add(new CleanstMap());
        fragments.add(new CleanstMapSuggestion());

        cleanstTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        cleanstViewPager = (ViewPager) findViewById(R.id.cleanstViewPager);

        cleanstViewPager.setAdapter(new MapAdapter(getSupportFragmentManager(),fragments));
        cleanstTabLayout.setupWithViewPager(cleanstViewPager);
        cleanstTabLayout.getTabAt(0).setText(getResources().getText(R.string.menu_around_me));
        cleanstTabLayout.getTabAt(1).setText(getResources().getText(R.string.menu_suggest));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent ;
        switch (item.getItemId()){
            case R.id.mAbout:
                intent = new Intent(this, About.class);
                startActivity(intent);
                break;
            case R.id.mSettings:
                intent = new Intent(this, Settings.class);
                startActivity(intent);
                break;
            case R.id.iLogin:
                intent = new Intent(this, Login.class);
                startActivity(intent);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    public void addLocation(){
        Intent intent = new Intent(this, NewLocation.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.maps_menu,menu);
        return true;
    }


    private void createLocation(Location newLocation){
        RestAdapter restAdapter     =  new RestAdapter();
        IEndpointsAPI endpointsAPI    = restAdapter.startHerokuRestAPI();

        Call<LocationResponse> responseCall    = endpointsAPI.createLocation(newLocation.getLocationId()
                                                                            ,newLocation.getLatitude()
                                                                            ,newLocation.getLongitude()
                                                                            ,newLocation.getType()
                                                                            ,newLocation.getComment());

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
        System.out.print(loc.toString());

    }


}
