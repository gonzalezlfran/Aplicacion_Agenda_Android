package com.franciscogonzalez_agenda_online.Objetos;

public class Nota {

    String id, uid_usuario, correo_usuario, fecha_hora_actual, titulo, descripcion, fecha_nota, estado;


    public Nota() {
    }

    public Nota(String id, String uid_usuario, String correo_usuario, String fecha_hora_actual, String titulo, String descripcion, String fecha_nota, String estado) {
        this.id = id;
        this.uid_usuario = uid_usuario;
        this.correo_usuario = correo_usuario;
        this.fecha_hora_actual = fecha_hora_actual;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha_nota = fecha_nota;
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid_usuario() {
        return uid_usuario;
    }

    public void setUid_usuario(String uid_usuario) {
        this.uid_usuario = uid_usuario;
    }

    public String getCorreo_usuario() {
        return correo_usuario;
    }

    public void setCorreo_usuario(String correo_usuario) {
        this.correo_usuario = correo_usuario;
    }

    public String getFecha_hora_actual() {
        return fecha_hora_actual;
    }

    public void setFecha_hora_actual(String fecha_hora_actual) {
        this.fecha_hora_actual = fecha_hora_actual;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha_nota() {
        return fecha_nota;
    }

    public void setFecha_nota(String fecha_nota) {
        this.fecha_nota = fecha_nota;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
