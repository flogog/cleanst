package com.flogog.cleanst.restAPI.adapter;

import com.flogog.cleanst.restAPI.ConstantsAPI;
import com.flogog.cleanst.restAPI.IEndpointsAPI;
import com.flogog.cleanst.restAPI.deserializer.LocationDeserializer;
import com.flogog.cleanst.restAPI.model.LocationResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by flogog on 9/26/16.
 */

public class RestAdapter {
    public IEndpointsAPI startConnectionRestAPI(Gson gson){

        Retrofit retrofit =  new Retrofit.Builder()
                .baseUrl(ConstantsAPI.CLEANST_ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(IEndpointsAPI.class);
    }

    public Gson createGSONDeserializeLocations(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocationResponse.class,new LocationDeserializer());
        return gsonBuilder.create();
    }

    /**
     * This method will be used to call the WS previously created in heroku
     * @return retrofit
     * */
    public IEndpointsAPI startHerokuRestAPI(){

        Retrofit retrofit =  new Retrofit.Builder()
                .baseUrl(ConstantsAPI.CLEANST_ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(IEndpointsAPI.class);
    }

}
