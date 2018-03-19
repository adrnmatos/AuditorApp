package br.gov.am.tce.auditor.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adriano on 15/03/2018.
 */

public class Commonweal {
    private String chave;
    private String area;
    private String coordenada;
    private String tipo;
    private String nome;
    private String jurisdicionado;
    private String endereco;
    private List<Contract> contracts = new ArrayList<>();

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCoordenada() {
        return coordenada;
    }

    public void setCoordenada(String coordenada) {
        this.coordenada = coordenada;
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
}
