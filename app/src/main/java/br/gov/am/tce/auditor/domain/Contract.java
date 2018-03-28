package br.gov.am.tce.auditor.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adriano on 14/03/2018.
 */

public class Contract implements Parcelable {
    private String id;
    private String numero;
    private String prazo;
    private String dataInicio;
    private String bemPublico;
    private String contratado;
    private List<Medicao> medicaoLista = new ArrayList<>();

    public Contract() {}

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(id);
        out.writeString(numero);
        out.writeString(prazo);
        out.writeString(dataInicio);
        out.writeString(bemPublico);
        out.writeString(contratado);
        out.writeList(medicaoLista);
    }

    public static final Parcelable.Creator<Contract> CREATOR = new Parcelable.Creator<Contract>() {
        public Contract createFromParcel(Parcel in) {
            return new Contract(in);
        }

        public Contract[] newArray(int size) {
            return new Contract[size];
        }
    };

    public Contract(Parcel in) {
        id = in.readString();
        numero = in.readString();
        prazo = in.readString();
        dataInicio = in.readString();
        bemPublico = in.readString();
        contratado = in.readString();
        medicaoLista = in.readArrayList(Medicao.class.getClassLoader());
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

    public String getPrazo() {
        return prazo;
    }

    public void setPrazo(String prazo) {
        this.prazo = prazo;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getBemPublico() {
        return bemPublico;
    }

    public void setBemPublico(String bemPublico) {
        this.bemPublico = bemPublico;
    }

    public String getContratado() {
        return contratado;
    }

    public void setContratado(String contratado) {
        this.contratado = contratado;
    }

    public List<Medicao> getMedicaoLista() {
        return medicaoLista;
    }

    public void setMedicaoLista(List<Medicao> medicaoLista) {
        this.medicaoLista = medicaoLista;
    }
}
