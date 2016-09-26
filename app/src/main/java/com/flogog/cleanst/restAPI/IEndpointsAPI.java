package com.flogog.cleanst.restAPI;

import com.flogog.cleanst.restAPI.model.LocationResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by flogog on 9/25/16.
 */

public interface IEndpointsAPI {

    @FormUrlEncoded
    @POST(ConstantsAPI.CLEANST_POST_CREATE_LOCATION)
    Call<LocationResponse> createLocation(@Field("locationId") String locationId, @Field("lat") String lat, @Field("lon") String lon, @Field("type") String type, @Field("comment") String comment);

    @GET(ConstantsAPI.CLEANST_GET_LOCATIONS)
    Call<LocationResponse> getLocations(@Path("type") String type);

    @FormUrlEncoded
    @POST(ConstantsAPI.CLEANST_POST_COMMENT)
    Call<LocationResponse> addComment(@Field("locationId") String locationId, @Field("comment") String comment);


}
