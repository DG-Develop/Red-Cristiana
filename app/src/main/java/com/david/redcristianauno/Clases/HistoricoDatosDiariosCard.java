package com.david.redcristianauno.Clases;

public class HistoricoDatosDiariosCard {
    private String nombre_card;
    private int asistencia_card;
    private int invitados_card;
    private int ninos_card;
    private double ofrenda_card;
    private String nombre_celula_card;
    private String nombre_subred_card;

    public HistoricoDatosDiariosCard() {
    }

    public HistoricoDatosDiariosCard(String nombre_card, int asistencia_card, int invitados_card, int ninos_card, double ofrenda_card, String nombre_celula_card, String nombre_subred_card) {
        this.nombre_card = nombre_card;
        this.asistencia_card = asistencia_card;
        this.invitados_card = invitados_card;
        this.ninos_card = ninos_card;
        this.ofrenda_card = ofrenda_card;
        this.nombre_celula_card = nombre_celula_card;
        this.nombre_subred_card = nombre_subred_card;
    }

    public String getNombre_card() {
        return nombre_card;
    }

    public void setNombre_card(String nombre_card) {
        this.nombre_card = nombre_card;
    }

    public int getAsistencia_card() {
        return asistencia_card;
    }

    public void setAsistencia_card(int asistencia_card) {
        this.asistencia_card = asistencia_card;
    }

    public int getInvitados_card() {
        return invitados_card;
    }

    public void setInvitados_card(int invitados_card) {
        this.invitados_card = invitados_card;
    }

    public int getNinos_card() {
        return ninos_card;
    }

    public void setNinos_card(int ninos_card) {
        this.ninos_card = ninos_card;
    }

    public double getOfrenda_card() {
        return ofrenda_card;
    }

    public void setOfrenda_card(double ofrenda_card) {
        this.ofrenda_card = ofrenda_card;
    }

    public String getNombre_celula_card() {
        return nombre_celula_card;
    }

    public void setNombre_celula_card(String nombre_celula_card) {
        this.nombre_celula_card = nombre_celula_card;
    }

    public String getNombre_subred_card() {
        return nombre_subred_card;
    }

    public void setNombre_subred_card(String nombre_subred_card) {
        this.nombre_subred_card = nombre_subred_card;
    }
}
