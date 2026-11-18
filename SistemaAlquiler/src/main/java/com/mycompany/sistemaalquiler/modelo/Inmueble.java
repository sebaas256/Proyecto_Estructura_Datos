
package com.mycompany.sistemaalquiler.modelo;

import java.time.LocalDate;

public class Inmueble {
    private int id;
    private String codigo;
    private String nombre;
    private int tipo_inmueble_id;
    private double precio_mensual;
    private double precio_diario;
    private int capacidad;
    private int habitaciones;
    private int banos;
    private double area_m2;
    private String caracteristicas;
    private String direccion;
    private String estado;
    private LocalDate fechaDisponible;

    public Inmueble( String codigo, String nombre, int tipo_inmueble_id, double precio_mensual, double precio_diario, int capacidad, 
                                    int habitaciones, int banos, double area_m2, String caracteristicas , String direccion, String estado, LocalDate fechaDisponible) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.tipo_inmueble_id = tipo_inmueble_id;
        this.precio_mensual = precio_mensual;
        this.precio_diario = precio_diario;
        this.capacidad = capacidad;
        this.habitaciones = habitaciones;
        this.banos = banos;
        this.area_m2 = area_m2;
        this.caracteristicas = caracteristicas;
        this.direccion = direccion;
        this.estado = estado;
        this.fechaDisponible = fechaDisponible;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getTipo_inmueble_id() {
        return tipo_inmueble_id;
    }

    public double getPrecio_mensual() {
        return precio_mensual;
    }

    public double getPrecio_diario() {
        return precio_diario;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public int getHabitaciones() {
        return habitaciones;
    }

    public int getBanos() {
        return banos;
    }

    public double getArea_m2() {
        return area_m2;
    }
    
    public String getCaracteristicas(){
        return caracteristicas;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado){
        this.estado = estado;
    }
    
    public LocalDate getFechaDisponible(){
        return fechaDisponible;
    }
    
    public void setFechaDisponible(LocalDate f){
        this.fechaDisponible = f;
    }
    
    @Override
    public String toString() {
        return codigo + " | " + nombre + " | $" + precio_mensual + "/mes | " + estado;
}
    
}
