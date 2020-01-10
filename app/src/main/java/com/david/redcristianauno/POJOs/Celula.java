package com.david.redcristianauno.POJOs;

public class Celula {
    private String id_celula;
    private  String id_subred;
    private String nombre_celula;
    private String nombre_subred;
    private String id_usuario;
    private String fecha;

    public Celula(){

    }

    public Celula(String nombre_celula, String nombre_subred, String id_usuario, String fecha) {
        this.nombre_celula = nombre_celula;
        this.nombre_subred = nombre_subred;
        this.id_usuario = id_usuario;
        this.fecha = fecha;
    }

    public String getId_subred() {
        return id_subred;
    }

    public void setId_subred(String id_subred) {
        this.id_subred = id_subred;
    }

    public String getId_celula() {
        return id_celula;
    }

    public void setId_celula(String id_celula) {
        this.id_celula = id_celula;
    }

    public String getNombre_celula() {
        return nombre_celula;
    }

    public void setNombre_celula(String nombre_celula) {
        this.nombre_celula = nombre_celula;
    }

    public String getNombre_subred() {
        return nombre_subred;
    }

    public void setNombre_subred(String nombre_subred) {
        this.nombre_subred = nombre_subred;
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
