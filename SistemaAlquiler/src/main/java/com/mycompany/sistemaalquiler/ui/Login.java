/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mycompany.sistemaalquiler.ui;
import com.mycompany.sistemaalquiler.validacion.Validacion;
import com.mycompany.sistemaalquiler.datos.UsuarioDB;
import java.sql.SQLException;

/**
 *
 * @author alexi
 */
public class Login {
    
    
    private static final int MAX_INTENTOS = 3;
    private static final int ANCHO = 62; //ancho de lineas(visual)
 
    // Usuario para cuando no hay base de datos en caso de.
    private static final String[] USUARIOS = {"test"};
    private static final String[] CLAVES = {"test01"};
    private static final String[] ROLES = {"ADMIN"};
    //io
    private static String usuarioActual = "";
    private static String rolActual = "";
 
   // Devuelve true si inicio sesion; false si se acabaron los intentos. 
    public static boolean iniciar() {
        int intentos = 0;
        boolean acceso = false;
 
// PRIMERA VEZ en cada computadora (una sola vez):
// quitar los // de la linea de abajo correr y volver a comentarla.
//UsuarioDB.crearAdminInicial();
 
        mostrarBanner(); //visual
 
        while (intentos < MAX_INTENTOS && !acceso) {
            String usuario = Consola.leerTexto("Usuario: ").trim();
            String clave = Consola.leerTexto("Contrasena: ").trim();
 
            if (!Validacion.textoValido(usuario) || !Validacion.textoValido(clave)) {
                IO.print("Debe llenar usuario y contrasena.\n");
            } else {
                String rol = verificarCredenciales(usuario, clave);
                if (rol != null) {
                    usuarioActual = usuario;
                    rolActual = rol;
                    acceso = true;
                } else {
                    intentos++;
                    IO.print("Usuario o contrasena incorrectos. Intentos restantes: "
                            + (MAX_INTENTOS - intentos) + "\n");
                }
            }
        }
 
        if (acceso) {
            IO.print("\nAcceso concedido. Bienvenido, " + usuarioActual
                    + " (" + rolActual + ").\n\n");
        } else {
            IO.print("\nDemasiados intentos fallidos. Sistema bloqueado");
        }
        return acceso;
    }
 
    public static String getUsuarioActual() {
        return usuarioActual;
    }
 
    public static String getRolActual() {
        return rolActual;
    }
 
    public static boolean esAdmin() {
        return rolActual.equals("ADMIN");
    }
    
   //Busca en la base de datos. Si la base falla usa el usuario de prueba. Devuelve el rol o null. 
    private static String verificarCredenciales(String usuario, String clave) {
        try {
            return UsuarioDB.autenticar(usuario, clave);
        } catch (SQLException e) {
            IO.print("Aviso: no se pudo usar la base de datos (" + e.getMessage() + ").\n");
            IO.print("Se usara el usuario de prueba.\n");
            int posicion = buscarUsuarioPrueba(usuario, clave);
            if (posicion >= 0) {
                return ROLES[posicion];
            }
            return null;
        }
    }
 
   //Devuelve la posicion en los arreglos de prueba, o -1 si no coincide. 
    private static int buscarUsuarioPrueba(String usuario, String clave) {
        for (int i = 0; i < USUARIOS.length; i++) {
            if (USUARIOS[i].equals(usuario) && CLAVES[i].equals(clave)) {
                return i;
            }
        }
        return -1;
    }
    
    
    
 //-========================================================================================
 // print meramente visual (visual)
    private static void mostrarBanner() {
        IO.print("\n" + linea('=') + "\n");
        IO.print(centrar("") + "\n");
        IO.print(centrar("   /\\   ") + "\n");
        IO.print(centrar("  /  \\  ") + "\n");
        IO.print(centrar(" /____\\ ") + "\n");
        IO.print(centrar(" | [] | ") + "\n");
        IO.print(centrar(" |____| ") + "\n");
        IO.print(centrar("") + "\n");
        IO.print(centrar("SISTEMA GERENCIAL DE ALQUILER DE INMUEBLES") + "\n");
        IO.print(centrar("") + "\n");
        IO.print(centrar("INICIO DE SESION") + "\n");
        IO.print(centrar("") + "\n");
        IO.print(linea('=') + "\n\n");
    }
 
    // Linea de ANCHO caracteres repetidos.(visual)
    private static String linea(char simbolo) {
        String resultado = "";
        for (int i = 0; i < ANCHO; i++) {
            resultado += simbolo;
        }
        return resultado;
    }
 
    //Texto centrado entre dos barras "|", con el mismo ancho que la linea.(visual)
    private static String centrar(String texto) {
        int espacios = ANCHO - 2 - texto.length();
        int izquierda = espacios / 2;
        int derecha = espacios - izquierda;
 
        String resultado = "|";
        for (int i = 0; i < izquierda; i++) {
            resultado += " ";
        }
        resultado += texto;
        for (int i = 0; i < derecha; i++) {
            resultado += " ";
        }
        resultado += "|";
        return resultado;
    }
}

 //-========================================================================================