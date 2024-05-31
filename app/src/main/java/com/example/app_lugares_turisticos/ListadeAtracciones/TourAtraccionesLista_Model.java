package com.example.app_lugares_turisticos.ListadeAtracciones;

public class TourAtraccionesLista_Model {

    String municipio, departamento;
    int image;

    public TourAtraccionesLista_Model(String municipio, String departamento, int image) {
        this.municipio = municipio;
        this.departamento = departamento;
        this.image = image;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
