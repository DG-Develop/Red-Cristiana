package com.david.redcristianauno.POJOs;

public class Estado {
    private int id_estado;
    private String nombre_estado;

    public Estado() {
    }

    public Estado(int id_estado, String nombre_estado) {
        this.id_estado = id_estado;
        this.nombre_estado = nombre_estado;
    }

    public Estado(String nombre_estado) {
        this.nombre_estado = nombre_estado;
    }

    public int getId_estado() {
        return id_estado;
    }

    public void setId_estado(int id_estado) {
        this.id_estado = id_estado;
    }

    public String getNombre_estado() {
        return nombre_estado;
    }

    public void setNombre_estado(String nombre_estado) {
        this.nombre_estado = nombre_estado;
    }
}
