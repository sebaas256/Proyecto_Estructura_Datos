/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaalquiler.ui;

import com.mycompany.sistemaalquiler.modelo.Cliente;
import com.mycompany.sistemaalquiler.servicio.ClienteServicio;

public class MenuPrincipal {
    public void mostrar(){
        int opccion;
        do {            
            IO.print("==============SISTEMA GERENCIAL DE ALQUILER DE INMUEBLES==============\n");
            IO.print("1- Registrar inmueble.\n");
            IO.print("2- Registrar cliente.\n");
            IO.print("3- Buscar inmueble / cliente.\n");
            IO.print("4- Ver catalogo disponible.\n");
            IO.print("5- Nueva venta / alquiler.\n");
            IO.print("6- Reporte de ventas.\n");
            IO.print("7- Gestionar usuarios.\n");
            IO.print("0- Salir.\n");
            IO.print("======================================================================\n");
            
            opccion = Consola.leerEntero("Ingrese una opccion: ");
            ClienteServicio clienteServicio = new ClienteServicio();
            
            switch(opccion){
                case 1->{
                    
                }
                case 2 ->{
                    String codigo = clienteServicio.generarCodigo();
                    String nombre = Consola.leerTexto("Nombre: ");
                    String apellido = Consola.leerTexto("Apellido: ");
                    String dui = Consola.leerDui("DUI: ");
                    int edad = Consola.leerEdad("Edad: ");
                    String correo = Consola.leerCorreo("Correo: ");
                    String telefono = Consola.leerTexto("Telefono: ");
                    String direccion = Consola.leerTexto("Direccion: ");
                    
                    Cliente c = new Cliente(codigo, nombre, apellido, dui, edad, correo, telefono, direccion);
                    if (clienteServicio.registrar(c)){
                        IO.print("Cliente registrado con codigo " + codigo + "\n");
                    }
                }
                case 3->{
                    
                }
                case 4->{
                    
                }
                case 5->{
                    
                }
                case 6->{
                    
                }
                case 7->{
                    
                }
                case 0->{
                    
                }
                default ->{
                    IO.print("Ingres una opccion valida.\n");
                }
            }
        } while (opccion != 0);
    }
}
