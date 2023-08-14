package com.example.cafeteria.models;

public class Mesa {

    private String id;
    private int estado; // 0->Disponible, 1->Ocupada
    private int idMesero;
    private String nombreMesero;

    public Mesa(String id, int estado, int idMesero, String nombreMesero) {
        this.id = id;
        this.estado = estado;
        this.idMesero = idMesero;
        this.nombreMesero = nombreMesero;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getIdMesero() {
        return idMesero;
    }

    public void setIdMesero(int idMesero) {
        this.idMesero = idMesero;
    }

    public String getNombreMesero() {
        return nombreMesero;
    }

    public void setNombreMesero(String nombreMesero) {
        this.nombreMesero = nombreMesero;
    }
}
