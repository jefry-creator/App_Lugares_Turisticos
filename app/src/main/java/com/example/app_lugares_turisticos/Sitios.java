package com.example.app_lugares_turisticos;

public class Sitios {

    private String key;

    String URLimagen,nombreSitio, descripcionSitio, horaApertura, horaCierre,
            tarifaSitio, actividadesSitio, direccionSitio;
    double Latitud, Longitud;

    boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getURLimagen() {
        return URLimagen;
    }

    public void setURLimagen(String URLimagen) {
        this.URLimagen = URLimagen;
    }

    public String getNombreSitio() {
        return nombreSitio;
    }

    public void setNombreSitio(String nombreSitio) {
        this.nombreSitio = nombreSitio;
    }

    public String getDescripcionSitio() {
        return descripcionSitio;
    }

    public void setDescripcionSitio(String descripcionSitio) {
        this.descripcionSitio = descripcionSitio;
    }

    public String getHoraApertura() {
        return horaApertura;
    }

    public void setHoraApertura(String horaApertura) {
        this.horaApertura = horaApertura;
    }

    public String getHoraCierre() {
        return horaCierre;
    }

    public void setHoraCierre(String horaCierre) {
        this.horaCierre = horaCierre;
    }

    public String getTarifaSitio() {
        return tarifaSitio;
    }

    public void setTarifaSitio(String tarifaSitio) {
        this.tarifaSitio = tarifaSitio;
    }

    public String getActividadesSitio() {
        return actividadesSitio;
    }

    public void setActividadesSitio(String actividadesSitio) {
        this.actividadesSitio = actividadesSitio;
    }

    public double getLatitud() {
        return Latitud;
    }

    public void setLatitud(double latitud) {
        Latitud = latitud;
    }

    public double getLongitud() {
        return Longitud;
    }

    public void setLongitud(double longitud) {
        Longitud = longitud;
    }

    public String getDireccionSitio() {
        return direccionSitio;
    }

    public void setDireccionSitio(String direccionSitio) {
        this.direccionSitio = direccionSitio;
    }
}
