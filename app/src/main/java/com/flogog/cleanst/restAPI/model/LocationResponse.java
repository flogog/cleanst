package com.flogog.cleanst.restAPI.model;

import com.flogog.cleanst.pojo.Location;

import java.util.ArrayList;

/**
 * Created by flogog on 9/25/16.
 */

public class LocationResponse {

    private String locationsData;
    private ArrayList<Location> locations;

    public LocationResponse() {
    }

    public LocationResponse(String locationsData) {
        this.locationsData = locationsData;
     }

    public String getLocationsData() {
        return locationsData;
    }

    public void setLocationsData(String locationsData) {
        this.locationsData = locationsData;
    }

    public ArrayList<Location> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<Location> locations) {
        this.locations = locations;
    }

}
