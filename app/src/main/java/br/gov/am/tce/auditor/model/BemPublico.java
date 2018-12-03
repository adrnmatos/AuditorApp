package br.gov.am.tce.auditor.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adriano on 15/03/2018.
 */

public class BemPublico extends ContextObject implements Parcelable {
    private String id;
    private String area;
    private String latitude;
    private String longitude;
    private String tipo;
    private String nome;
    private String jurisdicionado;
    private String endereco;
    private List<ContextObject> contratoLista = new ArrayList<>();

    public BemPublico() {}

    public BemPublico(Parcel in) {
        id = in.readString();
        area = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        tipo = in.readString();
        nome = in.readString();
        jurisdicionado = in.readString();
        endereco = in.readString();
        contratoLista = in.readArrayList(Contrato.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(area);
        parcel.writeString(latitude);
        parcel.writeString(longitude);
        parcel.writeString(tipo);
        parcel.writeString(nome);
        parcel.writeString(jurisdicionado);
        parcel.writeString(endereco);
        parcel.writeList(contratoLista);
    }

    public static final Parcelable.Creator<BemPublico> CREATOR = new Parcelable.Creator<BemPublico>() {
        public BemPublico createFromParcel(Parcel in) {
            return new BemPublico(in);
        }

        public BemPublico[] newArray(int size) {
            return new BemPublico[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getJurisdicionado() {
        return jurisdicionado;
    }

    public void setJurisdicionado(String jurisdicionado) {
        this.jurisdicionado = jurisdicionado;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public List<ContextObject> getContratos() {
        return contratoLista;
    }

    public void setContratos(List<ContextObject> contratoLista) {
        this.contratoLista = contratoLista;
    }

}
