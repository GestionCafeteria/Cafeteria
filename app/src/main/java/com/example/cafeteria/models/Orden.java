package com.example.cafeteria.models;

public class Orden {
    private int idOrden;
    private int mesa;
    private int meseroID;
    private String nombreMesero;
    private double total;
    private int estatus;

    public Orden(int idOrden, int mesa, int meseroID, String nombreMesero, double total, int estatus) {
        this.idOrden = idOrden;
        this.mesa = mesa;
        this.meseroID = meseroID;
        this.nombreMesero = nombreMesero;
        this.total = total;
        this.estatus = estatus;
    }

    public int getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(int id) {
        this.idOrden = id;
    }

    public int getMesa() {
        return mesa;
    }

    public void setMesa(int mesa) {
        this.mesa = mesa;
    }

    public int getMeseroID() {
        return meseroID;
    }

    public void setMeseroID(int meseroID) {
        this.meseroID = meseroID;
    }

    public String getNombreMesero() {
        return nombreMesero;
    }

    public void setNombreMesero(String nombreMesero) {
        this.nombreMesero = nombreMesero;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }
}
