/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaalquiler.validacion;
import com.mycompany.sistemaalquiler.ui.Consola;

/**
 *
 * @author sebastian
 */
public class Validacion {
    public static boolean duiValido (String dui){
    return dui != null && dui.matches("\\d{8}-\\d");
}
    
    public static boolean precioValido (double precio){
        return precio > 0;
    }
    
    public static boolean edadValida(int edad){
        return edad >= 18 && edad <= 100;
    }
    
    public static boolean correoValido(String correo){
        return correo != null && correo.matches("/^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$/");
    }
    
    public static boolean textoValido(String texto){
        return texto != null && !texto.trim().isEmpty();
    }
}
