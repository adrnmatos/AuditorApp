package br.gov.am.tce.auditor;


import java.util.Date;
import java.util.UUID;

/**
 * Created by adrnm on 24/10/2017.
 */

public class Photo {

    private UUID mId;
    private String mTitle;
    private double mLatitude;
    private double mLongitude;

    public Photo(UUID id) {
        mId = id;
    }

    public Photo() {
        this(UUID.randomUUID());
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String description) {
        mTitle = description;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }

    public String getPhotoFilename() {
        return "IMG_" + getId().toString() + ".jpg";
    }
}
