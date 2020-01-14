package com.david.redcristianauno.POJOs;

public class Usuarios {
    private String nombre;
    private String apellido_paterno;
    private String apellido_materno;
    private String municipio;
    private String colonia;
    private String calle;
    private String no_exterior;
    private int codigo_postal;
    private String telefono;
    private String correo;
    private String pass;
    private String tipo_permiso;
    private String subred;

    public Usuarios(String nombre, String apellido_paterno, String apellido_materno,String municipio, String colonia, String calle,
                    String no_exterior, int codigo_postal, String telefono, String correo, String pass,
                    String tipo_permiso, String subred) {
        this.nombre = nombre;
        this.apellido_paterno = apellido_paterno;
        this.apellido_materno = apellido_materno;
        this.municipio = municipio;
        this.colonia = colonia;
        this.calle = calle;
        this.no_exterior = no_exterior;
        this.codigo_postal = codigo_postal;
        this.telefono = telefono;
        this.correo = correo;
        this.pass = pass;
        this.tipo_permiso = tipo_permiso;
        this.subred = subred;
    }

    public Usuarios() {
    }

    public Usuarios(String nombre, String apellido_paterno, String apellido_materno, String municipio, String colonia,
                    String calle, String no_exterior, int codigo_postal, String telefono,
                    String correo, String pass, String tipo_permiso) {
        this.nombre = nombre;
        this.apellido_paterno = apellido_paterno;
        this.apellido_materno = apellido_materno;
        this.municipio = municipio;
        this.colonia = colonia;
        this.calle = calle;
        this.no_exterior = no_exterior;
        this.codigo_postal = codigo_postal;
        this.telefono = telefono;
        this.correo = correo;
        this.pass = pass;
        this.tipo_permiso = tipo_permiso;
    }

    public Usuarios(String nombre, String correo) {
        this.nombre = nombre;
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido_paterno() {
        return apellido_paterno;
    }

    public void setApellido_paterno(String apellido_paterno) {
        this.apellido_paterno = apellido_paterno;
    }

    public String getApellido_materno() {
        return apellido_materno;
    }

    public void setApellido_materno(String apellido_materno) {
        this.apellido_materno = apellido_materno;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNo_exterior() {
        return no_exterior;
    }

    public void setNo_exterior(String no_exterior) {
        this.no_exterior = no_exterior;
    }

    public int getCodigo_postal() {
        return codigo_postal;
    }

    public void setCodigo_postal(int codigo_postal) {
        this.codigo_postal = codigo_postal;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getTipo_permiso() {
        return tipo_permiso;
    }

    public void setTipo_permiso(String tipo_permiso) {
        this.tipo_permiso = tipo_permiso;
    }

    public String getSubred() {
        return subred;
    }

    public void setSubred(String subred) {
        this.subred = subred;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }
}
