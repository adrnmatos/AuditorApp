package br.gov.am.tce.auditor.domain;


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
    private long mTime;

    public Photo(String id) {
        mId = id;
    }

    public Photo() {
        this(UUID.randomUUID().toString());
    }

    public Photo(String id, String title, double latitude, double longitude, long time) {
        mId = id;
        mTitle = title;
        mLatitude = latitude;
        mLongitude = longitude;
        mTime = time;
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

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        this.mTime = time;
    }

    public String getPhotoFilename() {
        return "IMG_" + getId().toString() + ".jpg";
    }
}
