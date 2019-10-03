package com.david.redcristianauno.POJOs;

public class Usuario {
    private String id_usuario;
    private String nombre;
    private String apellido_paterno;
    private String apellido_materno;
    private String correo;
    private String contraseña;
    private int id_estado;
    private int id_municipio;
    private String colonia;
    private String calle;
    private String no_exterior;
    private int codigo_postal;
    private String telefono;
    private int id_permiso;
    private String id_subred;

    public Usuario(){

    }
    public Usuario(String nombre, String correo){
        this.nombre = nombre;
        this.correo = correo;
    }
    public Usuario(String id_usuario, String nombre, String apellido_paterno, String apellido_materno, String correo, String contraseña, int id_estado, int id_municipio, String colonia, String calle, String no_exterior, int codigo_postal, String telefono, int id_permiso, String id_subred) {
        this.id_usuario = id_usuario;
        this.nombre = nombre;
        this.apellido_paterno = apellido_paterno;
        this.apellido_materno = apellido_materno;
        this.correo = correo;
        this.contraseña = contraseña;
        this.id_estado = id_estado;
        this.id_municipio = id_municipio;
        this.colonia = colonia;
        this.calle = calle;
        this.no_exterior = no_exterior;
        this.codigo_postal = codigo_postal;
        this.telefono = telefono;
        this.id_permiso = id_permiso;
        this.id_subred = id_subred;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
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
    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getId_estado() {
        return id_estado;
    }

    public void setId_estado(int id_estado) {
        this.id_estado = id_estado;
    }

    public int getId_municipio() {
        return id_municipio;
    }

    public void setId_municipio(int id_municipio) {
        this.id_municipio = id_municipio;
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

    public int getId_permiso() {
        return id_permiso;
    }

    public void setId_permiso(int id_permiso) {
        this.id_permiso = id_permiso;
    }

    public String getId_subred() {
        return id_subred;
    }

    public void setId_subred(String id_sured) {
        this.id_subred = id_sured;
    }
}
