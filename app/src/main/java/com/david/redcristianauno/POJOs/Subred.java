package com.david.redcristianauno.POJOs;

public class Subred {
    private String id_subred;
    private String nombre_subred;
    private String nombre_red;
    private int capacidad;
    private String id_usuario;
    private String fecha;

    public Subred(){

    }

    public Subred(String nombre_subred, String nombre_red, int capacidad, String id_usuario, String fecha) {
        this.nombre_subred = nombre_subred;
        this.nombre_red = nombre_red;
        this.capacidad = capacidad;
        this.id_usuario = id_usuario;
        this.fecha = fecha;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getId_subred() {
        return id_subred;
    }

    public void setId_subred(String id_subred) {
        this.id_subred = id_subred;
    }

    public String getNombre_subred() {
        return nombre_subred;
    }

    public void setNombre_subred(String nombre_subred) {
        this.nombre_subred = nombre_subred;
    }

    public String getNombre_red() {
        return nombre_red;
    }

    public void setNombre_red(String nombre_red) {
        this.nombre_red = nombre_red;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
