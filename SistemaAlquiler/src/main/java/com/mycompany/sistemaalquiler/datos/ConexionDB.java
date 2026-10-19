/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaalquiler.datos;

/**
 *
 * @author sebastian
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    private static final String URL = "jdbc:postgresql://127.0.0.1:5432/inmuebles";
    private static final String USUARIO = "postgres";
    private static final String CLAVE = "Sebasqr456#";
    
    public static Connection obtener() throws SQLException{
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }
}
