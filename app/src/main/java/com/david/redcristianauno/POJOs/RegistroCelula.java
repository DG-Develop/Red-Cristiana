package com.david.redcristianauno.POJOs;

public class RegistroCelula {
    private String id_registroCelula;
    private String id_usuario;
    private String nombre_usuario;
    private String nombre_anfitrion;
    private String domicilio;
    private int asistencia_celula;
    private int invitados_celula;
    private int ninos_celula;
    private double ofrenda_celula;
    private String fecha_celula;

    public RegistroCelula() {
    }

    public RegistroCelula(String id_usuario, String nombre_usuario, String nombre_anfitrion, String domicilio, int asistencia_celula,
                          int invitados_celula, int ninos_celula, double ofrenda_celula, String fecha_celula) {
        this.id_usuario = id_usuario;
        this.nombre_usuario = nombre_usuario;
        this.nombre_anfitrion = nombre_anfitrion;
        this.domicilio = domicilio;
        this.asistencia_celula = asistencia_celula;
        this.invitados_celula = invitados_celula;
        this.ninos_celula = ninos_celula;
        this.ofrenda_celula = ofrenda_celula;
        this.fecha_celula = fecha_celula;
    }

    public RegistroCelula(String nombre_usuario, String nombre_anfitrion, String domicilio,
                          int asistencia_celula, int invitados_celula, int ninos_celula, double ofrenda_celula, String fecha_celula) {
        this.nombre_usuario = nombre_usuario;
        this.nombre_anfitrion = nombre_anfitrion;
        this.domicilio = domicilio;
        this.asistencia_celula = asistencia_celula;
        this.invitados_celula = invitados_celula;
        this.ninos_celula = ninos_celula;
        this.ofrenda_celula = ofrenda_celula;
        this.fecha_celula = fecha_celula;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getId_registroCelula() {
        return id_registroCelula;
    }

    public void setId_registroCelula(String id_registroCelula) {
        this.id_registroCelula = id_registroCelula;
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

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre_anfitrion() {
        return nombre_anfitrion;
    }

    public void setNombre_anfitrion(String nombre_anfitrion) {
        this.nombre_anfitrion = nombre_anfitrion;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }
}
