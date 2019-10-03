package com.david.redcristianauno.POJOs;

public class Subred {
    private String id_subred;
    private String nombre_subred;
    private String id_red;
    private String id_usuario;
    private String fecha;

    public Subred(){

    }
    public Subred(String id_subred, String nombre_subred, String id_red, String id_usuario, String fecha) {
        this.id_subred = id_subred;
        this.nombre_subred = nombre_subred;
        this.id_red = id_red;
        this.id_usuario = id_usuario;
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

    public String getId_red() {
        return id_red;
    }

    public void setId_red(String id_red) {
        this.id_red = id_red;
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
