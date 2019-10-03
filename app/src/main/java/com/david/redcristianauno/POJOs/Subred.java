package com.david.redcristianauno.DTO;

import java.util.Date;

public class Subred {
    private String id_subred;
    private String nombre_subred;
    private String nombre_red;
    private String nombre_usuario;
    private String fecha;

    public Subred(){

    }
    public Subred(String id_subred,String nombre_subred, String nombre_red, String nombre_usuario, String fecha) {
        this.id_subred = id_subred;
        this.nombre_subred = nombre_subred;
        this.nombre_red = nombre_red;
        this.nombre_usuario = nombre_usuario;
        this.fecha = fecha;
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

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
