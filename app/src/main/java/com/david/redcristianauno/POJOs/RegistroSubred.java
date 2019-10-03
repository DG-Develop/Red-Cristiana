package com.david.redcristianauno.DTO;

public class RegistroSubred {
    private String id_registroSubred;
    private String nombre_subred;
    private int asistencia_subred;
    private double ofrenda_subred;
    private String fecha_subred;

    public RegistroSubred() {

    }

    public RegistroSubred(String id_registroSubred, String nombre_subred, int asistencia_subred, double ofrenda_subred, String fecha_subred) {
        this.id_registroSubred = id_registroSubred;
        this.nombre_subred = nombre_subred;
        this.asistencia_subred = asistencia_subred;
        this.ofrenda_subred = ofrenda_subred;
        this.fecha_subred = fecha_subred;
    }

    public String getId_registroSubred() {
        return id_registroSubred;
    }

    public void setId_registroSubred(String id_registroSubred) {
        this.id_registroSubred = id_registroSubred;
    }

    public String getNombre_subred() {
        return nombre_subred;
    }

    public void setNombre_subred(String nombre_subred) {
        this.nombre_subred = nombre_subred;
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
}
