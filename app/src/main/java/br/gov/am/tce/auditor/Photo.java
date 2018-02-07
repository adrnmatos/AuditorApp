package br.gov.am.tce.auditor;


import java.io.Serializable;
import java.util.UUID;

/**
 * Created by adrnm on 24/10/2017.
 */

public class Photo implements Serializable {

    private String mId;
    private String mTitle;
    private double mLatitude;
    private double mLongitude;

    public Photo(String id) {
        mId = id;
    }

    public Photo() {
        this(UUID.randomUUID().toString());
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
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
