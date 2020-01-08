package com.david.redcristianauno.POJOs;

public class HistoricoSemanal {
    private String id_historico;
    private int total_asistencia;
    private int total_invitados;
    private int total_ninos;
    private  double total_ofrenda;
    private String fecha;

    public HistoricoSemanal() {
    }

    public HistoricoSemanal(String id_historico, int total_asistencia, int total_invitados, int total_ninos, double total_ofrenda, String fecha) {
        this.id_historico = id_historico;
        this.total_asistencia = total_asistencia;
        this.total_invitados = total_invitados;
        this.total_ninos = total_ninos;
        this.total_ofrenda = total_ofrenda;
        this.fecha = fecha;
    }

    public HistoricoSemanal(int total_asistencia, int total_invitados, int total_ninos, double total_ofrenda, String fecha) {
        this.total_asistencia = total_asistencia;
        this.total_invitados = total_invitados;
        this.total_ninos = total_ninos;
        this.total_ofrenda = total_ofrenda;
        this.fecha = fecha;
    }

    public double getTotal_ofrenda() {
        return total_ofrenda;
    }

    public void setTotal_ofrenda(double total_ofrenda) {
        this.total_ofrenda = total_ofrenda;
    }

    public String getId_historico() {
        return id_historico;
    }

    public void setId_historico(String id_historico) {
        this.id_historico = id_historico;
    }

    public int getTotal_asistencia() {
        return total_asistencia;
    }

    public void setTotal_asistencia(int total_asistencia) {
        this.total_asistencia = total_asistencia;
    }

    public int getTotal_invitados() {
        return total_invitados;
    }

    public void setTotal_invitados(int total_invitados) {
        this.total_invitados = total_invitados;
    }

    public int getTotal_ninos() {
        return total_ninos;
    }

    public void setTotal_ninos(int total_ninos) {
        this.total_ninos = total_ninos;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
