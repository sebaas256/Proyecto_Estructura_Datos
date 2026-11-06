/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaalquiler.datos;


//db conecction
import java.sql.Connection;
//sql para parametros evita ineyection
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//sql errors e try/catch
import java.sql.SQLException;
//sql
import java.sql.Statement;

/**
 *
 * @author alexi
 */

//rs result set, st statment, pt prepared statement (evita inyection)
//  Acceso a la tabla usuarios. La contrasena se guarda cifrada: el cifrado hash ()
//  lo hace PostgreSQL con la extension pgcrypto (funciones crypt y gen_salt),
//  asi que en Java solo se ejecutan consultas.

public class UsuarioDB {
 
 //Devuelve el rol del usuario si usuario y contrasena son correctos; si no, null.
    //solamente para confirmar.
    //preparedstatement
    public static String autenticar(String usuario, String clave) throws SQLException {
        String sql = "SELECT r.nombre AS rol "
                + "FROM usuarios u JOIN roles r ON r.id = u.rol_id "
                + "WHERE u.nombre_usuario = ? AND u.activo = true "
                + "AND u.contrasena_hash = crypt(?, u.contrasena_hash)";
        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario);
            ps.setString(2, clave);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("rol");
                }
            }
        }
        return null;
    }
 

//Creacion de los los roles ADMIN y VENDEDOR y el usuario inicial test / test01 (ADMIN).
//Se puede ejecutar varias veces sin problema: si ya existen, no pasa nada
    //REVISAR LOGIN.java!
    public static void crearAdminInicial() {
        try (Connection con = ConexionDB.obtener();
             Statement st = con.createStatement()) {
 
            st.execute("CREATE EXTENSION IF NOT EXISTS pgcrypto");
 
            st.executeUpdate("INSERT INTO roles (nombre) VALUES ('ADMIN'), ('VENDEDOR') "
                    + "ON CONFLICT (nombre) DO NOTHING");
 //guardar password cifrada usando , cryp y gensalt en la DB
            st.executeUpdate("INSERT INTO usuarios (nombre_completo, nombre_usuario, contrasena_hash, rol_id) "
                    + "SELECT 'Administrador de Prueba', 'test', crypt('test01', gen_salt('bf')), id "
                    + "FROM roles WHERE nombre = 'ADMIN' "
                    + "ON CONFLICT (nombre_usuario) DO NOTHING");
 
            IO.print("Usuario inicial listo (test / test01).\n");
        } catch (SQLException e) {
            IO.print("No se pudo crear el usuario inicial: " + e.getMessage() + "\n");
        }
    }
}
