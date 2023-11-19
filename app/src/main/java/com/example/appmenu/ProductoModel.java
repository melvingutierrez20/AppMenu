package com.example.appmenu;

public class ProductoModel {

    private String idProducto;
    private String nombreProducto;
    private String descripcion;
    private String categoriaId;  // Cambiado a identificador de categoría en lugar de nombre
    private double precio;
    private String imageUrl;
   // comentario de prueba
    private String nombreCategoria;

    // Constructor vacío requerido para Firestore
    public ProductoModel(String idProducto, String nombre, String descripcion, double precio, Object o, String categoriaId, String nombreCategoria) {
    }

    public ProductoModel(String idProducto, String nombreProducto, String descripcion, String categoriaId, double precio, String imageUrl, String nombreCategoria) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.descripcion = descripcion;
        this.categoriaId = categoriaId;
        this.precio = precio;
        this.imageUrl = imageUrl;
        this.nombreCategoria = nombreCategoria;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(String categoriaId) {
        this.categoriaId = categoriaId;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }
}
