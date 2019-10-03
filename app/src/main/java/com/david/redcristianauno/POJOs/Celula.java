package com.david.redcristianauno.DTO;

public class Celula {
    private String id_celula;
    private String nombre_celula;
    private String nombre_subred;
    private String nombre_usuario;
    private String fecha;

    public Celula(){

    }
    public Celula(String id_celula, String nombre_celula, String nombre_subred, String nombre_usuario, String fecha) {
        this.id_celula = id_celula;
        this.nombre_celula = nombre_celula;
        this.nombre_subred = nombre_subred;
        this.nombre_usuario = nombre_usuario;
        this.fecha = fecha;
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
