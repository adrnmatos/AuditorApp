package br.gov.am.tce.auditor.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Adriano on 15/03/2018.
 */

public class Medicao implements Parcelable{
    private String id;
    private String name;

    public Medicao() {}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
    }

    public static final Parcelable.Creator<Medicao> CREATOR = new Parcelable.Creator<Medicao>() {
        public Medicao createFromParcel(Parcel in) {
            return new Medicao(in);
        }
        public Medicao[] newArray(int size) {
            return new Medicao[size];
        }
    };

    public Medicao(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
