/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.sistemaalquiler;
/**
 *
 * @author sebastian
 */
import static com.mycompany.sistemaalquiler.datos.ConexionDB.obtener;
import com.mycompany.sistemaalquiler.ui.MenuPrincipal;
import java.sql.Connection;
import java.sql.SQLException;

public class SistemaAlquiler {
    void main() {
        //debug para la BD
         try (Connection con = obtener()) {
            System.out.println("Conectado a: " + con.getCatalog());
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
                 new MenuPrincipal().mostrar();
                 
    }
}
