package com.david.redcristianauno.POJOs;

public class Municipio {
    private int id_municipio;
    private String nombre_municipio;
    private int id_estado;

    public Municipio() {
    }

    public Municipio(int id_municipio, String nombre_municipio, int id_estado) {
        this.id_municipio = id_municipio;
        this.nombre_municipio = nombre_municipio;
        this.id_estado = id_estado;
    }

    public int getId_municipio() {
        return id_municipio;
    }

    public void setId_municipio(int id_municipio) {
        this.id_municipio = id_municipio;
    }

    public String getNombre_municipio() {
        return nombre_municipio;
    }

    public void setNombre_municipio(String nombre_municipio) {
        this.nombre_municipio = nombre_municipio;
    }

    public int getId_estado() {
        return id_estado;
    }

    public void setId_estado(int id_estado) {
        this.id_estado = id_estado;
    }
}
