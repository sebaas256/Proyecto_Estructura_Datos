/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaalquiler.ui;

/**
 *
 * @author sebastian
 */
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
            
            switch(opccion){
                case 1->{
                    
                }
                case 2 ->{
                    
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
