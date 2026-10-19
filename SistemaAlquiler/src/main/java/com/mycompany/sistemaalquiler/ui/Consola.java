/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaalquiler.ui;
import com.mycompany.sistemaalquiler.validacion.Validacion;
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
    
    public static String leerTexto(String mensaje){
        while (true) {            
            try {
                return IO.readln(mensaje.trim());
            } catch (Exception e) {
                IO.print("Debes ingresar un texto valido\n");
            }
        }
    }
    
    public static Double leerDecimal(String mensaje){
        while (true) {            
            try {
                return Double.parseDouble(IO.readln(mensaje.trim()));
            } catch (NumberFormatException e) {
                IO.print("Debes ingresar un numero decimal valido.\n");
            }
        }
    }
    
    public static String leerDui(String mensaje){
        String dui;
        do {            
            dui = leerTexto(mensaje);
            if (!Validacion.duiValido(dui)){
                IO.print("Dui invalido. Formato correcto: 12345678-9\n");
            }
        } while (!Validacion.duiValido(dui));
        return dui;
    }
    
    public static double leerPrecio(String mensaje){
        double precio;
        do {            
            precio = leerDecimal(mensaje);
            if (!Validacion.precioValido(precio)){
                IO.print("Precio invalido. El precio debe ser mayor a 0");
            }
        } while (!Validacion.precioValido(precio));
        return precio;
    }
    
    public static int leerEdad(String mensaje){
        int edad;
        do {            
            edad = leerEntero(mensaje);
            if(!Validacion.edadValida(edad)){
                IO.print("Edad invalida. Debe ser mayor a 18 años y menor que 100 años");
            }
        } while (!Validacion.edadValida(edad));
        return edad;
    }
    
    public static String leerCorreo(String mensaje){
        String correo;
        do{
            correo = leerTexto(mensaje);
            if (!Validacion.correoValido(correo)){
                IO.print("Correo invalido. Formato correo: example67@exp.com");
            }
        }while (!Validacion.correoValido(correo));
        return correo;
    }
}
