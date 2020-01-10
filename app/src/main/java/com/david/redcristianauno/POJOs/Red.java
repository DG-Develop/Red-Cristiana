package com.david.redcristianauno.POJOs;

public class Red {
    private String nombre_red;
    private int capacidad;
    private String id_usuario;
    private String fecha;

    public Red(){

    }


    public Red(String nombre_red, int capacidad, String id_usuario, String fecha) {
        this.nombre_red = nombre_red;
        this.capacidad = capacidad;
        this.id_usuario = id_usuario;
        this.fecha = fecha;
    }

    public String getNombre_red() {
        return nombre_red;
    }

    public void setNombre_red(String nombre_red) {
        this.nombre_red = nombre_red;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }
}
