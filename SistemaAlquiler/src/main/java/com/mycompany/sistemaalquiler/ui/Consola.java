/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaalquiler.ui;

/**
 *
 * @author sebastian
 */
public class Consola {
    public static int leerEntero(String mensaje){
        while (true) {            
            try {
                return Integer.parseInt(IO.readln(mensaje.trim()));
            } catch (NumberFormatException e) {
                IO.print("Debe ingresar un numero entero.\n");
            }
        }
    }
    
}
