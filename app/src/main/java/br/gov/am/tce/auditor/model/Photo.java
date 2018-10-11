package br.gov.am.tce.auditor.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by adrnm on 24/10/2017.
 */

public class Photo implements Parcelable {
    private String mId;
    private String mTitle;
    private double mLatitude;
    private double mLongitude;
    private long mTime;
    private String mBemPublico;
    private String mContrato;
    private String mMedicao;
    private String mAuthor;

    public Photo(String id) {
        mId = id;
        mTitle = "";
        mBemPublico = "";
        mContrato = "";
        mMedicao = "";
    }

    public Photo() {
        /*this(UUID.randomUUID().toString());*/
    }

    public Photo(String id, String title, double latitude, double longitude, long time,
                 String bemPublico, String contrato, String medicao, String author) {
        mId = id;
        mTitle = title;
        mLatitude = latitude;
        mLongitude = longitude;
        mTime = time;
        mBemPublico = bemPublico;
        mContrato = contrato;
        mMedicao = medicao;
        mAuthor = author;
    }

    public Photo(Parcel in) {
        mId = in.readString();
        mTitle = in.readString();
        mLatitude = in.readDouble();
        mLongitude = in.readDouble();
        mTime = in.readLong();
        mBemPublico = in.readString();
        mContrato = in.readString();
        mMedicao = in.readString();
        mAuthor = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mTitle);
        parcel.writeDouble(mLatitude);
        parcel.writeDouble(mLongitude);
        parcel.writeLong(mTime);
        parcel.writeString(mBemPublico);
        parcel.writeString(mContrato);
        parcel.writeString(mMedicao);
        parcel.writeString(mAuthor);
    }

    public static final Parcelable.Creator<Photo> CREATOR = new Parcelable.Creator<Photo>() {
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", mId);
        result.put("title", mTitle);
        result.put("latitude", mLatitude);
        result.put("longitude", mLongitude);
        result.put("dataHora", mTime);
        result.put("bemPublico", mBemPublico);
        result.put("contrato", mContrato);
        result.put("medicao", mMedicao);
        result.put("author", mAuthor);
        return result;
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

    public String getBemPublico() {
        return mBemPublico;
    }

    public void setBemPublico(String bemPublico) {
        this.mBemPublico = bemPublico;
    }

    public String getContrato() {
        return mContrato;
    }

    public void setContrato(String contrato) {
        this.mContrato = contrato;
    }

    public String getMedicao() {
        return mMedicao;
    }

    public void setMedicao(String medicao) {
        this.mMedicao = medicao;
    }

    public String getPhotoFilename() {
        return "IMG_" + getId() + ".jpg";
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

}
