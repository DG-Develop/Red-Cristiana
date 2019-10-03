package com.david.redcristianauno.DTO;

public class RegistroCelula {
    private String id_registroCelula;
    private String nombre_celula;
    private int asistencia_celula;
    private int invitados_celula;
    private int ninos_celula;
    private double ofrenda_celula;
    private String fecha_celula;

    public RegistroCelula() {
    }

    public RegistroCelula(String id_registroCelula, String nombre_celula, int asistencia_celula, int invitados_celula, int ninos_celula, double ofrenda_celula, String fecha_celula) {
        this.id_registroCelula = id_registroCelula;
        this.nombre_celula = nombre_celula;
        this.asistencia_celula = asistencia_celula;
        this.invitados_celula = invitados_celula;
        this.ninos_celula = ninos_celula;
        this.ofrenda_celula = ofrenda_celula;
        this.fecha_celula = fecha_celula;
    }

    public String getId_registroCelula() {
        return id_registroCelula;
    }

    public void setId_registroCelula(String id_registroCelula) {
        this.id_registroCelula = id_registroCelula;
    }

    public String getNombre_celula() {
        return nombre_celula;
    }

    public void setNombre_celula(String nombre_celula) {
        this.nombre_celula = nombre_celula;
    }

    public int getAsistencia_celula() {
        return asistencia_celula;
    }

    public void setAsistencia_celula(int asistencia_celula) {
        this.asistencia_celula = asistencia_celula;
    }

    public int getInvitados_celula() {
        return invitados_celula;
    }

    public void setInvitados_celula(int invitados_celula) {
        this.invitados_celula = invitados_celula;
    }

    public int getNinos_celula() {
        return ninos_celula;
    }

    public void setNinos_celula(int ninos_celula) {
        this.ninos_celula = ninos_celula;
    }

    public double getOfrenda_celula() {
        return ofrenda_celula;
    }

    public void setOfrenda_celula(double ofrenda_celula) {
        this.ofrenda_celula = ofrenda_celula;
    }

    public String getFecha_celula() {
        return fecha_celula;
    }

    public void setFecha_celula(String fecha_celula) {
        this.fecha_celula = fecha_celula;
    }
}
