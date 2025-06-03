package com.carlosdev.lab5_20210535;

import java.io.Serializable;
import java.util.Date;

public class Habito implements Serializable {
    private String nombre;
    private String categoria;
    private int frecuenciaHoras;
    private Date fechaHoraInicio;

    public Habito() {}

    public Habito(String nombre, String categoria, int frecuenciaHoras, Date fechaHoraInicio) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.frecuenciaHoras = frecuenciaHoras;
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getFrecuenciaHoras() {
        return frecuenciaHoras;
    }

    public void setFrecuenciaHoras(int frecuenciaHoras) {
        this.frecuenciaHoras = frecuenciaHoras;
    }

    public Date getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(Date fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }
}