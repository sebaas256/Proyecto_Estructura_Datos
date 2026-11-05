/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaalquiler.modelo;

public class Cliente {
    private int id;
    private String codigo;
    private String nombre;
    private String apellido;
    private String dui;
    private int edad;
    private String correo;
    private String telefono;
    private String direccion;

    //El id no va en el constructor porque lo genera postgres
    public Cliente(String codigo, String nombre, String apellido, String dui, int edad, String correo, String telefono, String direccion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dui = dui;
        this.edad = edad;
        this.correo = correo;
        this.telefono = telefono;
        this.direccion = direccion;
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

    public String getApellido() {
        return apellido;
    }

    public String getDui() {
        return dui;
    }

    public int getEdad() {
        return edad;
    }

    public String getCorreo() {
        return correo;
    }
    
    public String getTelefono(){
        return telefono;
    }
    
    public String getDireccion(){
        return direccion;
    }
    
    @Override
    public String toString(){
        return codigo + " | " + nombre + " " + apellido + " | DUI: " + dui;
    }
}
