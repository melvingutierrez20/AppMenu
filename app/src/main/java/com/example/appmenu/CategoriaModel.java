package com.example.appmenu;


public class CategoriaModel {
    private String id;
    private String title;
    private String imageUrl;

    public CategoriaModel() {
        // Constructor vacío requerido por Firebase
    }

    public CategoriaModel(String nombre, String imageUrl) {
        this.title = nombre;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

