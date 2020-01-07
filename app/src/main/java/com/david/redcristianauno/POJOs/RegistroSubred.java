package com.david.redcristianauno.POJOs;

public class RegistroSubred {
    private String id_registroSubred;
    private String id_usuario;
    private String nombre_usuario;
    private int asistencia_subred;
    private double ofrenda_subred;
    private String fecha_subred;

    public RegistroSubred() {

    }

    public RegistroSubred(String id_usuario, String nombre_usuario, int asistencia_subred,
                          double ofrenda_subred, String fecha_subred) {
        this.id_usuario = id_usuario;
        this.nombre_usuario = nombre_usuario;
        this.asistencia_subred = asistencia_subred;
        this.ofrenda_subred = ofrenda_subred;
        this.fecha_subred = fecha_subred;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getId_registroSubred() {
        return id_registroSubred;
    }

    public void setId_registroSubred(String id_registroSubred) {
        this.id_registroSubred = id_registroSubred;
    }

    public int getAsistencia_subred() {
        return asistencia_subred;
    }

    public void setAsistencia_subred(int asistencia_subred) {
        this.asistencia_subred = asistencia_subred;
    }

    public double getOfrenda_subred() {
        return ofrenda_subred;
    }

    public void setOfrenda_subred(double ofrenda_subred) {
        this.ofrenda_subred = ofrenda_subred;
    }

    public String getFecha_subred() {
        return fecha_subred;
    }

    public void setFecha_subred(String fecha_subred) {
        this.fecha_subred = fecha_subred;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }
}
