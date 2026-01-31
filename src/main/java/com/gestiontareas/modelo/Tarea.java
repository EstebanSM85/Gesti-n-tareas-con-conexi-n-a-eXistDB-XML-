package com.gestiontareas.modelo;

import java.time.LocalDate;

public class Tarea {
    private String id;
    private String titulo;
    private String descripcion;
    private String estado; // "pendiente", "en_proceso", "completada"
    private String prioridad; // "baja", "media", "alta"
    private LocalDate fechaCreacion;
    private LocalDate fechaVencimiento;

    // Constructor vacío
    public Tarea() {
        this.fechaCreacion = LocalDate.now();
        this.estado = "pendiente";
    }

    // Constructor completo
    public Tarea(String id, String titulo, String descripcion, String estado, 
                 String prioridad, LocalDate fechaVencimiento) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.estado = estado;
        this.prioridad = prioridad;
        this.fechaCreacion = LocalDate.now();
        this.fechaVencimiento = fechaVencimiento;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    @Override
    public String toString() {
        return "Tarea{" +
                "id='" + id + '\'' +
                ", titulo='" + titulo + '\'' +
                ", estado='" + estado + '\'' +
                ", prioridad='" + prioridad + '\'' +
                ", fechaVencimiento=" + fechaVencimiento +
                '}';
    }
}