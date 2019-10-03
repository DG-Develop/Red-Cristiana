package com.david.redcristianauno.POJOs;

public class HistoricoSemanalSubred {
    private String id_historico;
    private int total_asistencia;
    private  double total_ofrenda;
    private String fecha;

    public HistoricoSemanalSubred() {

    }

    public HistoricoSemanalSubred(String id_historico, int total_asistencia, double total_ofrenda, String fecha) {
        this.id_historico = id_historico;
        this.total_asistencia = total_asistencia;
        this.total_ofrenda = total_ofrenda;
        this.fecha = fecha;
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

    public double getTotal_ofrenda() {
        return total_ofrenda;
    }

    public void setTotal_ofrenda(double total_ofrenda) {
        this.total_ofrenda = total_ofrenda;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
