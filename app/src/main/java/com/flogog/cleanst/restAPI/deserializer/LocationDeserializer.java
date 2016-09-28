package com.flogog.cleanst.restAPI.deserializer;

import com.flogog.cleanst.pojo.Location;
import com.flogog.cleanst.restAPI.JsonKeys;
import com.flogog.cleanst.restAPI.model.LocationResponse;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by flogog on 9/26/16.
 */

public class LocationDeserializer implements JsonDeserializer<LocationResponse>{
    @Override
    public LocationResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        Gson gson = new Gson();
        LocationResponse locationResponse             = gson.fromJson(json, LocationResponse.class);

        JsonArray locationResponseData         = json.getAsJsonObject().getAsJsonArray(JsonKeys.RESPONSE);
        System.out.println("****************************"+locationResponseData);
        locationResponse.setLocations(deserializeLocationsJSON(locationResponseData));

        return locationResponse;

    }

    private ArrayList<Location> deserializeLocationsJSON(JsonArray locationResponseData){
        ArrayList<Location> locations = new ArrayList<>();
        for(int i=0; i<locationResponseData.size(); i++){
            JsonObject locationResponseDataObject   = locationResponseData.get(i).getAsJsonObject();
            String          type                  = locationResponseDataObject.get(JsonKeys.TYPE).getAsString();
            String          lat               = locationResponseDataObject.get(JsonKeys.LATITUDE).getAsString();
            String          lng               = locationResponseDataObject.get(JsonKeys.LOGITUDE).getAsString();
            Location newLocation =  new Location(type,lat,lng);
            locations.add(newLocation);
            System.out.println("****************************"+newLocation.toString());
        }
        return locations;
    }

}
