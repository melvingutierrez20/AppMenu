package com.example.appmenu;

public class CuentasModel {
     private String idCompra;
     private String codigoCuenta;
     private String productosSeleccionados;
     private double totalPedido;

    public String getNombrePlato() {
        return nombrePlato;
    }

    public void setNombrePlato(String nombrePlato) {
        this.nombrePlato = nombrePlato;
    }

    private String nombrePlato;

    public CuentasModel() {
    }

    public CuentasModel(String idCompra, String codigoCuenta, String productosSeleccionados, double totalPedido) {
        this.idCompra = idCompra;
        this.codigoCuenta = codigoCuenta;
        this.productosSeleccionados = productosSeleccionados;
        this.totalPedido = totalPedido;
    }

    public String getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(String idCompra) {
        this.idCompra = idCompra;
    }

    public String getCodigoCuenta() {
        return codigoCuenta;
    }

    public void setCodigoCuenta(String codigoCuenta) {
        this.codigoCuenta = codigoCuenta;
    }

    public String getProductosSeleccionados() {
        return productosSeleccionados;
    }

    public void setProductosSeleccionados(String productosSeleccionados) {
        this.productosSeleccionados = productosSeleccionados;
    }

    public double getTotalPedido() {
        return totalPedido;
    }

    public void setTotalPedido(double totalPedido) {
        this.totalPedido = totalPedido;
    }
}
