package com.example.app_lugares_turisticos;

public class TuristaLugares_Model {
    int image;
    String nombre, municipio;

    public TuristaLugares_Model(int image, String nombre, String municipio) {
        this.image = image;
        this.nombre = nombre;
        this.municipio = municipio;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }
}
