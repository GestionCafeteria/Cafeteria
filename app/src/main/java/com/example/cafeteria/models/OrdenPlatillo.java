package com.example.cafeteria.models;

public class OrdenPlatillo {
    private int idOrden;
    private int numeroMesa;
    private String estatus;
    private String platillo;
    private double precio;
    private int cantidad;
    private String nombreMesero;
    private double total;

    public OrdenPlatillo(int idOrden, int numeroMesa, String estatus, String platillo, double precio, int cantidad, String nombreMesero, double total) {
        this.idOrden = idOrden;
        this.numeroMesa = numeroMesa;
        this.estatus = estatus;
        this.platillo = platillo;
        this.precio = precio;
        this.cantidad = cantidad;
        this.nombreMesero = nombreMesero;
        this.total = total;
    }

    public int getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(int idOrden) {
        this.idOrden = idOrden;
    }

    public int getNumeroMesa() {
        return numeroMesa;
    }

    public void setNumeroMesa(int numeroMesa) {
        this.numeroMesa = numeroMesa;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getPlatillo() {
        return platillo;
    }

    public void setPlatillo(String platillo) {
        this.platillo = platillo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
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
}
