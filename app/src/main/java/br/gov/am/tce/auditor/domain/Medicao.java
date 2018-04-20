package br.gov.am.tce.auditor.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Adriano on 15/03/2018.
 */

public class Medicao implements Parcelable{
    private String id;
    private String numero;
    private String dataInicio;
    private String dataFim;
    private String contratoId;

    public Medicao() {}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(numero);
        parcel.writeString(dataInicio);
        parcel.writeString(dataFim);
        parcel.writeString(contratoId);
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
        numero = in.readString();
        dataInicio = in.readString();
        dataFim = in.readString();
        contratoId = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDataFim() {
        return dataFim;
    }

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }

    public String getContratoId() {
        return contratoId;
    }

    public void setContratoId(String contratoId) {
        this.contratoId = contratoId;
    }

    @Override
    public String toString() {
        return this.numero;
    }
}
