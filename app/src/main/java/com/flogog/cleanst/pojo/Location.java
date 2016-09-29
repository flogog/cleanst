package com.flogog.cleanst.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by flogog on 9/26/16.
 */

public class Location implements Parcelable{

    private String locationId;
    private String latitude;
    private String longitude;
    private String type;
    private String comment;

    public Location() {
    }

    public Location(String latitude, String longitude, String type) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
    }

    public Location(String locationId, String latitude, String longitude, String type, String comment) {
        this.locationId = locationId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.comment = comment;
    }

    protected Location(Parcel in) {
        locationId = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        type = in.readString();
        comment = in.readString();
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Location{" +
                "locationId='" + locationId + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", type='" + type + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(locationId);
        parcel.writeString(latitude);
        parcel.writeString(longitude);
        parcel.writeString(type);
        parcel.writeString(comment);
    }
}
