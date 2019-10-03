package com.david.redcristianauno.POJOs;

public class Permisos {
    private String id_permiso;
    private String tipo_permiso;

    public Permisos() {
    }

    public Permisos(String id_permiso, String tipo_permiso) {
        this.id_permiso = id_permiso;
        this.tipo_permiso = tipo_permiso;
    }

    public String getId_permiso() {
        return id_permiso;
    }

    public void setId_permiso(String id_permiso) {
        this.id_permiso = id_permiso;
    }

    public String getTipo_permiso() {
        return tipo_permiso;
    }

    public void setTipo_permiso(String tipo_permiso) {
        this.tipo_permiso = tipo_permiso;
    }
}
